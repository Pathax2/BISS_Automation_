// ===================================================================================================================================
// File          : BambooIntegration.java
// Package       : reporting
// Description   : Utility class for Bamboo CI/CD REST API integration.
//                 Provides methods to:
//                   1. Attach the HTML report as a build artifact via Bamboo REST API
//                   2. Post a pass/fail summary comment on the build result
//                   3. Upload the execution history JSON sidecar for trend tracking
//
// Prerequisites :
//   - Bamboo REST API credentials configured as system properties or environment variables:
//       bamboo.rest.url       = https://bamboo.yourorg.com/rest/api/latest
//       bamboo.rest.username  = <service-account>
//       bamboo.rest.password  = <token-or-password>
//   - The build plan must allow artifact definitions via the REST API
//
// Integration Points:
//   - ReportManager.java     : reads suite metadata and results for the build comment
//   - TestRunner.java        : calls BambooIntegration.publishReport() after HtmlReportGenerator
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Logger;

public class BambooIntegration
{
    private static final Logger log = Logger.getLogger(BambooIntegration.class.getName());

    // Configuration — can be overridden via system properties or environment variables
    private static final String iBaseUrl   = resolveConfig("bamboo.rest.url",      "BAMBOO_REST_URL",      "http://localhost:8085/rest/api/latest");
    private static final String iUsername  = resolveConfig("bamboo.rest.username",  "BAMBOO_REST_USERNAME", "");
    private static final String iPassword  = resolveConfig("bamboo.rest.password",  "BAMBOO_REST_PASSWORD", "");
    private static final String iBuildKey  = System.getProperty("bamboo.buildResultKey", "");

    // ===============================================================================================================================
    // PUBLIC API
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : publishReport
    // Description   : Orchestrates the full Bamboo integration workflow:
    //                 1) Attaches the HTML report as a downloadable artifact
    //                 2) Posts a summary comment with pass/fail counts
    //                 3) Saves execution history JSON for trend charting
    // Parameters    : pReportPath  (String) - path to the generated HTML report file
    //                 pHistoryPath (String) - path to the JSON history sidecar file
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void publishReport(String pReportPath, String pHistoryPath)
    {
        if (iBuildKey.isEmpty())
        {
            log.info("[BambooIntegration] No bamboo.buildResultKey set — skipping Bamboo integration (likely local run).");
            return;
        }
        if (iUsername.isEmpty() || iPassword.isEmpty())
        {
            log.warning("[BambooIntegration] Bamboo credentials not configured — skipping integration.");
            return;
        }

        log.info("[BambooIntegration] Publishing report for build: " + iBuildKey);

        try
        {
            attachArtifact(pReportPath, "BISS_Execution_Report.html", "text/html");
            postBuildComment(buildSummaryComment());

            if (pHistoryPath != null)
            {
                saveExecutionHistory(pHistoryPath);
            }

            log.info("[BambooIntegration] Integration complete.");
        }
        catch (Exception e)
        {
            log.severe("[BambooIntegration] Integration failed: " + e.getMessage());
            // Non-fatal — do not fail the build because of report integration issues
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : attachArtifact
    // Description   : Uploads a file as a shared artifact on the Bamboo build result.
    //                 Uses the Bamboo REST API endpoint:
    //                   POST /rest/api/latest/result/{buildKey}/artifact/{artifactName}
    // Parameters    : pFilePath     (String) - local path to the file
    //                 pArtifactName (String) - name to assign the artifact in Bamboo
    //                 pContentType  (String) - MIME type of the file
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void attachArtifact(String pFilePath, String pArtifactName, String pContentType) throws Exception
    {
        Path iPath = Paths.get(pFilePath);
        if (!Files.exists(iPath))
        {
            log.warning("[BambooIntegration] File not found: " + pFilePath);
            return;
        }

        byte[] iFileBytes = Files.readAllBytes(iPath);

        String iUrl = iBaseUrl + "/result/" + iBuildKey + "/artifact/shared/" + pArtifactName;

        HttpURLConnection iConn = openConnection(iUrl, "PUT");
        iConn.setRequestProperty("Content-Type", pContentType);
        iConn.setDoOutput(true);

        try (OutputStream os = iConn.getOutputStream())
        {
            os.write(iFileBytes);
            os.flush();
        }

        int iStatus = iConn.getResponseCode();
        if (iStatus >= 200 && iStatus < 300)
        {
            log.info("[BambooIntegration] Artifact uploaded: " + pArtifactName + " (" + iFileBytes.length + " bytes)");
        }
        else
        {
            log.warning("[BambooIntegration] Artifact upload returned HTTP " + iStatus + " for " + pArtifactName);
        }
        iConn.disconnect();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : postBuildComment
    // Description   : Posts a comment on the Bamboo build result with execution summary.
    //                 Uses the Bamboo REST API endpoint:
    //                   POST /rest/api/latest/result/{buildKey}/comment
    // Parameters    : pComment (String) - HTML-formatted comment text
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void postBuildComment(String pComment) throws Exception
    {
        String iUrl = iBaseUrl + "/result/" + iBuildKey + "/comment";

        String iBody = "{\"content\":\"" + escapeJson(pComment) + "\"}";

        HttpURLConnection iConn = openConnection(iUrl, "POST");
        iConn.setRequestProperty("Content-Type", "application/json");
        iConn.setDoOutput(true);

        try (OutputStream os = iConn.getOutputStream())
        {
            os.write(iBody.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        int iStatus = iConn.getResponseCode();
        if (iStatus >= 200 && iStatus < 300)
        {
            log.info("[BambooIntegration] Build comment posted successfully.");
        }
        else
        {
            log.warning("[BambooIntegration] Comment POST returned HTTP " + iStatus);
        }
        iConn.disconnect();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : saveExecutionHistory
    // Description   : Appends the current build's pass/fail/error counts to a JSON history file
    //                 used by HtmlReportGenerator for the build trend chart (Enhancement #3).
    //                 Keeps the last 20 entries to prevent unbounded growth.
    // Parameters    : pHistoryPath (String) - path to the JSON history file
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void saveExecutionHistory(String pHistoryPath)
    {
        try
        {
            Path iPath = Paths.get(pHistoryPath);
            StringBuilder iExisting = new StringBuilder();

            if (Files.exists(iPath))
            {
                iExisting.append(new String(Files.readAllBytes(iPath), StandardCharsets.UTF_8).trim());
            }

            // Build the new entry
            String iEntry = String.format(
                    "{\"build\":\"%s\",\"pass\":%d,\"fail\":%d,\"error\":%d,\"total\":%d,\"timestamp\":\"%s\"}",
                    escapeJson(ReportManager.getBuildNumber()),
                    ReportManager.getPassCount(),
                    ReportManager.getFailCount(),
                    ReportManager.getErrorCount(),
                    ReportManager.getTotalCount(),
                    escapeJson(ReportManager.getSuiteEndTime())
            );

            // Parse existing JSON array or start fresh
            String iArrayContent;
            if (iExisting.length() > 2 && iExisting.charAt(0) == '[')
            {
                // Remove trailing ] and append
                String inner = iExisting.substring(1, iExisting.length() - 1).trim();
                if (inner.isEmpty())
                {
                    iArrayContent = "[" + iEntry + "]";
                }
                else
                {
                    iArrayContent = "[" + inner + "," + iEntry + "]";
                }
            }
            else
            {
                iArrayContent = "[" + iEntry + "]";
            }

            // Trim to last 20 entries (simple approach: count commas at top level)
            iArrayContent = trimToLastN(iArrayContent, 20);

            // Write
            Files.createDirectories(iPath.getParent());
            Files.write(iPath, iArrayContent.getBytes(StandardCharsets.UTF_8));

            log.info("[BambooIntegration] Execution history updated: " + pHistoryPath);
        }
        catch (IOException e)
        {
            log.warning("[BambooIntegration] Failed to save execution history: " + e.getMessage());
        }
    }

    // ===============================================================================================================================
    // HELPER METHODS
    // ===============================================================================================================================

    /**
     * Builds a formatted summary comment for the Bamboo build result.
     */
    private static String buildSummaryComment()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BISS Automation Report ===\\n");
        sb.append("Environment : ").append(ReportManager.getEnvironment()).append("\\n");
        sb.append("Browser     : ").append(ReportManager.getBrowser()).append("\\n");
        sb.append("Duration    : ").append(ReportManager.getSuiteDurationFormatted()).append("\\n");
        sb.append("---\\n");
        sb.append("Total  : ").append(ReportManager.getTotalCount()).append("\\n");
        sb.append("Passed : ").append(ReportManager.getPassCount()).append(" (").append(ReportManager.getPassPercent()).append("%)\\n");
        sb.append("Failed : ").append(ReportManager.getFailCount()).append(" (").append(ReportManager.getFailPercent()).append("%)\\n");
        sb.append("Errors : ").append(ReportManager.getErrorCount()).append("\\n");
        sb.append("Steps  : ").append(ReportManager.getTotalStepCount())
                .append(" (P:").append(ReportManager.getPassedStepCount())
                .append(" F:").append(ReportManager.getFailedStepCount())
                .append(" S:").append(ReportManager.getSkippedStepCount()).append(")\\n");
        sb.append("---\\n");
        sb.append("See the attached BISS_Execution_Report.html artifact for full details.");
        return sb.toString();
    }

    /**
     * Opens an authenticated HTTP connection with Basic auth.
     */
    private static HttpURLConnection openConnection(String pUrl, String pMethod) throws Exception
    {
        HttpURLConnection iConn = (HttpURLConnection) new URL(pUrl).openConnection();
        iConn.setRequestMethod(pMethod);
        iConn.setConnectTimeout(15000);
        iConn.setReadTimeout(30000);

        // Basic authentication
        String iCredentials = iUsername + ":" + iPassword;
        String iEncoded = Base64.getEncoder().encodeToString(iCredentials.getBytes(StandardCharsets.UTF_8));
        iConn.setRequestProperty("Authorization", "Basic " + iEncoded);
        iConn.setRequestProperty("Accept", "application/json");

        return iConn;
    }

    /**
     * Resolves a configuration value from system properties, then environment variables, then default.
     */
    private static String resolveConfig(String pSysProp, String pEnvVar, String pDefault)
    {
        String iValue = System.getProperty(pSysProp);
        if (iValue != null && !iValue.isEmpty()) return iValue;

        iValue = System.getenv(pEnvVar);
        if (iValue != null && !iValue.isEmpty()) return iValue;

        return pDefault;
    }

    /**
     * Trims a JSON array string to keep only the last N entries.
     */
    private static String trimToLastN(String pJsonArray, int pN)
    {
        // Simple approach: split on "},{ " pattern at the top level
        // A robust implementation would use a JSON parser
        String inner = pJsonArray.substring(1, pJsonArray.length() - 1).trim();
        String[] entries = inner.split("\\},\\s*\\{");

        if (entries.length <= pN) return pJsonArray;

        StringBuilder sb = new StringBuilder("[");
        int start = entries.length - pN;
        for (int i = start; i < entries.length; i++)
        {
            if (i > start) sb.append(",");
            String entry = entries[i];
            if (!entry.startsWith("{")) entry = "{" + entry;
            if (!entry.endsWith("}")) entry = entry + "}";
            sb.append(entry);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Escapes a string for embedding in a JSON value.
     */
    private static String escapeJson(String pText)
    {
        if (pText == null) return "";
        return pText
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "")
                .replace("\t", "\\t");
    }
}