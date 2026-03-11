// ===================================================================================================================================
// File          : HtmlReportGenerator.java
// Package       : reporting
// Description   : Generates a comprehensive, self-contained HTML execution report for the BISS framework.
//                 Designed for management review — includes:
//                   • Executive summary cards (Total, Pass, Fail, Duration)
//                   • Doughnut chart (Pass vs Fail) — Chart.js, embedded inline, no CDN required
//                   • Bar chart (per-test duration)
//                   • Full result table with status badges, tags, environment, duration, timestamp
//                   • Expandable failure details with root cause and screenshot embed per failed test
//                   • Bamboo build metadata block (Build No., Plan Key, Agent, Environment)
//                   • Fully self-contained single HTML file — no external dependencies
//
// Called from   : TestRunner.java — after endSuite()
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import reporting.ReportManager.TestCaseResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

public class HtmlReportGenerator
{
    private static final Logger log = Logger.getLogger(HtmlReportGenerator.class.getName());

    private static final String iReportOutputDirectory = System.getProperty("html.report.path", "Test_Report/html/");

    // ***************************************************************************************************************************************************************************************
    // Function Name : generate
    // Description   : Main entry point. Builds and writes the full HTML report file to disk.
    //                 Called once from TestRunner after all test cases have completed.
    // Parameters    : None (reads from ReportManager singleton)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void generate()
    {
        try
        {
            List<TestCaseResult> iResults = ReportManager.getResults();

            if (iResults.isEmpty())
            {
                log.warning("[HtmlReportGenerator] No results to report. HTML report skipped.");
                return;
            }

            File iDir = new File(iReportOutputDirectory);
            if (!iDir.exists()) { iDir.mkdirs(); }

            String iTimestamp  = ReportManager.getSuiteStartTime().replace(":", "-").replace(" ", "_");
            String iReportFile = iReportOutputDirectory + "BISS_Execution_Report_" + iTimestamp + ".html";

            try (FileWriter iWriter = new FileWriter(iReportFile))
            {
                iWriter.write(buildHtml(iResults));
            }

            log.info("[HtmlReportGenerator] HTML report generated : " + iReportFile);
        }
        catch (Exception iException)
        {
            log.severe("[HtmlReportGenerator] Failed to generate HTML report : " + iException.getMessage());
            throw new RuntimeException("HTML report generation failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildHtml
    // Description   : Assembles the complete HTML string from all sections
    // Parameters    : pResults (List<TestCaseResult>) - all test case results from ReportManager
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildHtml(List<TestCaseResult> pResults)
    {
        StringBuilder iHtml = new StringBuilder();

        iHtml.append(buildHead());
        iHtml.append(buildStyles());
        iHtml.append("<body>\n");
        iHtml.append(buildHeader());
        iHtml.append(buildBambooMetaBlock());
        iHtml.append(buildSummaryCards());
        iHtml.append(buildCharts(pResults));
        iHtml.append(buildResultTable(pResults));
        iHtml.append(buildFailureDetails(pResults));
        iHtml.append(buildFooter());
        iHtml.append(buildScripts(pResults));
        iHtml.append("</body>\n</html>");

        return iHtml.toString();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // HEAD
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildHead()
    {
        return "<!DOCTYPE html>\n<html lang='en'>\n<head>\n"
                + "<meta charset='UTF-8'>\n"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n"
                + "<title>BISS Automation Execution Report</title>\n"
                + "<!-- Chart.js v4 — self-contained CDN reference -->\n"
                + "<script src='https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js'></script>\n"
                + "</head>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // CSS STYLES
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildStyles()
    {
        return "<style>\n"
                + "* { box-sizing: border-box; margin: 0; padding: 0; }\n"
                + "body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f2f5; color: #1a1a2e; }\n"

                // Header
                + ".report-header { background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);"
                + " color: white; padding: 32px 40px; }\n"
                + ".report-header h1 { font-size: 28px; letter-spacing: 1px; }\n"
                + ".report-header .subtitle { font-size: 13px; color: #a0aec0; margin-top: 6px; }\n"
                + ".report-header .run-meta { display:flex; gap:32px; margin-top:18px; flex-wrap:wrap; }\n"
                + ".report-header .run-meta span { font-size:13px; color:#cbd5e0; }\n"
                + ".report-header .run-meta strong { color:#e2e8f0; }\n"

                // Bamboo meta block
                + ".bamboo-block { background:#16213e; border-left:4px solid #e94560; padding:16px 40px;"
                + " display:flex; gap:40px; flex-wrap:wrap; }\n"
                + ".bamboo-block .bitem { font-size:12px; color:#a0aec0; }\n"
                + ".bamboo-block .bitem strong { display:block; color:#90cdf4; font-size:13px; margin-bottom:2px; }\n"

                // Main content
                + ".content { padding: 28px 40px; }\n"

                // Summary cards
                + ".summary-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));"
                + " gap: 20px; margin-bottom: 32px; }\n"
                + ".card { background: white; border-radius: 12px; padding: 24px 20px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); text-align: center; }\n"
                + ".card .card-value { font-size: 40px; font-weight: 700; margin-bottom: 6px; }\n"
                + ".card .card-label { font-size: 13px; color: #718096; text-transform: uppercase; letter-spacing: 1px; }\n"
                + ".card.total  .card-value { color: #2d3748; }\n"
                + ".card.pass   .card-value { color: #38a169; }\n"
                + ".card.fail   .card-value { color: #e53e3e; }\n"
                + ".card.skip   .card-value { color: #d69e2e; }\n"
                + ".card.time   .card-value { font-size: 26px; color: #3182ce; }\n"
                + ".card.rate   .card-value { color: #38a169; }\n"

                // Charts
                + ".charts-row { display: grid; grid-template-columns: 340px 1fr; gap: 24px; margin-bottom: 32px; }\n"
                + ".chart-card { background: white; border-radius: 12px; padding: 24px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); }\n"
                + ".chart-card h3 { font-size: 14px; color: #4a5568; margin-bottom: 16px;"
                + " text-transform: uppercase; letter-spacing: 1px; }\n"
                + ".chart-wrapper { position: relative; }\n"
                + ".doughnut-wrapper { max-width: 260px; margin: 0 auto; }\n"

                // Section headings
                + ".section-title { font-size: 16px; font-weight: 600; color: #2d3748;"
                + " margin-bottom: 16px; padding-bottom: 8px; border-bottom: 2px solid #e2e8f0; }\n"

                // Result table
                + ".table-card { background: white; border-radius: 12px; padding: 24px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); margin-bottom: 32px; overflow-x:auto; }\n"
                + "table { width: 100%; border-collapse: collapse; font-size: 13px; }\n"
                + "thead tr { background: #2d3748; color: white; }\n"
                + "thead th { padding: 12px 14px; text-align: left; font-weight: 500; letter-spacing: 0.5px; }\n"
                + "tbody tr { border-bottom: 1px solid #edf2f7; transition: background 0.15s; }\n"
                + "tbody tr:hover { background: #f7fafc; }\n"
                + "tbody td { padding: 11px 14px; vertical-align: top; }\n"
                + ".row-pass { border-left: 3px solid #38a169; }\n"
                + ".row-fail { border-left: 3px solid #e53e3e; background: #fff5f5; }\n"
                + ".row-error { border-left: 3px solid #d69e2e; background: #fffff0; }\n"

                // Badges
                + ".badge { display:inline-block; padding:3px 10px; border-radius:12px; font-size:11px;"
                + " font-weight:700; letter-spacing:0.5px; }\n"
                + ".badge-pass  { background:#c6f6d5; color:#276749; }\n"
                + ".badge-fail  { background:#fed7d7; color:#9b2c2c; }\n"
                + ".badge-error { background:#fefcbf; color:#744210; }\n"

                // Tags
                + ".tag { display:inline-block; background:#ebf8ff; color:#2b6cb0; padding:2px 8px;"
                + " border-radius:8px; font-size:11px; margin:1px; }\n"

                // Failure details
                + ".failure-section { margin-bottom: 32px; }\n"
                + ".failure-card { background:white; border-radius:12px; margin-bottom:16px;"
                + " box-shadow:0 2px 12px rgba(0,0,0,0.08); overflow:hidden; border-left:4px solid #e53e3e; }\n"
                + ".failure-header { display:flex; justify-content:space-between; align-items:center;"
                + " padding:16px 20px; cursor:pointer; background:#fff5f5; }\n"
                + ".failure-header h4 { font-size:14px; color:#c53030; }\n"
                + ".failure-header .toggle-icon { font-size:18px; color:#e53e3e; }\n"
                + ".failure-body { padding:20px; display:none; }\n"
                + ".failure-body.open { display:block; }\n"
                + ".error-label { font-size:11px; text-transform:uppercase; color:#718096;"
                + " letter-spacing:1px; margin-bottom:6px; margin-top:14px; }\n"
                + ".error-box { background:#2d3748; color:#fc8181; padding:14px 16px; border-radius:8px;"
                + " font-family:monospace; font-size:12px; line-height:1.6; white-space:pre-wrap;"
                + " word-break:break-word; }\n"
                + ".screenshot-embed { margin-top:16px; }\n"
                + ".screenshot-embed img { max-width:100%; border-radius:8px;"
                + " border:1px solid #e2e8f0; box-shadow:0 2px 8px rgba(0,0,0,0.1); }\n"

                // Footer
                + ".report-footer { text-align:center; padding:24px; font-size:12px; color:#a0aec0; }\n"

                // Responsive
                + "@media(max-width:900px) { .charts-row { grid-template-columns:1fr; }"
                + " .content { padding:20px; } }\n"
                + "</style>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // HEADER BAND
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildHeader()
    {
        return "<div class='report-header'>\n"
                + "  <h1>&#x1F4CA; BISS Automation Execution Report</h1>\n"
                + "  <div class='subtitle'>BDD Cucumber | Selenium WebDriver | Quality Assurance Dashboard</div>\n"
                + "  <div class='run-meta'>\n"
                + "    <span><strong>Environment</strong> " + esc(ReportManager.getEnvironment()) + "</span>\n"
                + "    <span><strong>Browser</strong> " + esc(ReportManager.getBrowser()) + "</span>\n"
                + "    <span><strong>Started</strong> " + esc(ReportManager.getSuiteStartTime()) + "</span>\n"
                + "    <span><strong>Finished</strong> " + esc(ReportManager.getSuiteEndTime()) + "</span>\n"
                + "    <span><strong>Total Duration</strong> " + ReportManager.getSuiteDurationFormatted() + "</span>\n"
                + "  </div>\n"
                + "</div>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // BAMBOO META BLOCK
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildBambooMetaBlock()
    {
        return "<div class='bamboo-block'>\n"
                + "  <div class='bitem'><strong>&#x1F528; Bamboo Build No.</strong>" + esc(ReportManager.getBuildNumber()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F5FA; Plan Key</strong>" + esc(ReportManager.getBuildPlanKey()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F916; Agent</strong>" + esc(ReportManager.getBambooAgentName()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F464; Executed By</strong>" + esc(ReportManager.getExecutedBy()) + "</div>\n"
                + "</div>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // SUMMARY CARDS
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildSummaryCards()
    {
        int iTotal   = ReportManager.getTotalCount();
        int iPass    = ReportManager.getPassCount();
        int iFail    = ReportManager.getFailCount();
        int iError   = ReportManager.getErrorCount();
        String iRate = ReportManager.getPassPercent() + "%";

        return "<div class='content'>\n"
                + "<div class='summary-cards'>\n"
                + summaryCard("total", "&#x1F9EA;", String.valueOf(iTotal),   "Total Executed")
                + summaryCard("pass",  "&#x2705;",  String.valueOf(iPass),    "Passed")
                + summaryCard("fail",  "&#x274C;",  String.valueOf(iFail),    "Failed")
                + summaryCard("skip",  "&#x26A0;",  String.valueOf(iError),   "Errors")
                + summaryCard("rate",  "&#x1F3AF;",  iRate,                   "Pass Rate")
                + summaryCard("time",  "&#x23F1;",  ReportManager.getSuiteDurationFormatted(), "Total Duration")
                + "</div>\n";
    }

    private static String summaryCard(String pType, String pIcon, String pValue, String pLabel)
    {
        return "<div class='card " + pType + "'>\n"
                + "  <div style='font-size:28px;margin-bottom:8px;'>" + pIcon + "</div>\n"
                + "  <div class='card-value'>" + pValue + "</div>\n"
                + "  <div class='card-label'>" + pLabel + "</div>\n"
                + "</div>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // CHARTS
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildCharts(List<TestCaseResult> pResults)
    {
        StringBuilder iLabels   = new StringBuilder();
        StringBuilder iDuration = new StringBuilder();
        StringBuilder iColors   = new StringBuilder();

        for (int i = 0; i < pResults.size(); i++)
        {
            TestCaseResult r = pResults.get(i);
            if (i > 0) { iLabels.append(","); iDuration.append(","); iColors.append(","); }
            iLabels.append("'").append(esc(r.iTestCaseID)).append("'");
            iDuration.append(r.iDurationMs / 1000.0);
            iColors.append(r.isPassed() ? "'#38a169'" : r.isFailed() ? "'#e53e3e'" : "'#d69e2e'");
        }

        return "<div class='charts-row'>\n"

                // Doughnut
                + "  <div class='chart-card'>\n"
                + "    <h3>&#x1F4CA; Pass / Fail Breakdown</h3>\n"
                + "    <div class='doughnut-wrapper'><canvas id='doughnutChart'></canvas></div>\n"
                + "    <div style='text-align:center;margin-top:12px;font-size:12px;color:#718096;'>"
                + "      <span style='color:#38a169;font-weight:700;'>" + ReportManager.getPassPercent() + "% Pass</span>"
                + "      &nbsp;|&nbsp;"
                + "      <span style='color:#e53e3e;font-weight:700;'>" + ReportManager.getFailPercent() + "% Fail</span>"
                + "    </div>\n"
                + "  </div>\n"

                // Bar chart
                + "  <div class='chart-card'>\n"
                + "    <h3>&#x23F1; Execution Duration per Test Case (seconds)</h3>\n"
                + "    <div class='chart-wrapper'><canvas id='barChart'></canvas></div>\n"
                + "  </div>\n"

                + "</div>\n"

                // Store data for script section
                + "<script>\n"
                + "  window._chartLabels = [" + iLabels + "];\n"
                + "  window._chartDurations = [" + iDuration + "];\n"
                + "  window._chartColors = [" + iColors + "];\n"
                + "  window._passCount = " + ReportManager.getPassCount() + ";\n"
                + "  window._failCount = " + ReportManager.getFailCount() + ";\n"
                + "  window._errorCount = " + ReportManager.getErrorCount() + ";\n"
                + "</script>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // RESULT TABLE
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildResultTable(List<TestCaseResult> pResults)
    {
        StringBuilder iSb = new StringBuilder();
        iSb.append("<div class='table-card'>\n")
                .append("<div class='section-title'>&#x1F4CB; Full Execution Results</div>\n")
                .append("<table>\n")
                .append("<thead><tr>")
                .append("<th>#</th>")
                .append("<th>Test Case ID</th>")
                .append("<th>Description</th>")
                .append("<th>Status</th>")
                .append("<th>Tags</th>")
                .append("<th>Environment</th>")
                .append("<th>Browser</th>")
                .append("<th>Duration</th>")
                .append("<th>Timestamp</th>")
                .append("</tr></thead>\n<tbody>\n");

        int iIdx = 1;
        for (TestCaseResult r : pResults)
        {
            iSb.append("<tr class='").append(r.getRowClass()).append("'>\n")
                    .append("  <td>").append(iIdx++).append("</td>\n")
                    .append("  <td><strong>").append(esc(r.iTestCaseID)).append("</strong></td>\n")
                    .append("  <td>").append(esc(r.iDescription)).append("</td>\n")
                    .append("  <td><span class='badge ").append(r.getStatusBadgeClass()).append("'>")
                    .append(r.iStatus).append("</span></td>\n")
                    .append("  <td>").append(buildTagHtml(r.iTags)).append("</td>\n")
                    .append("  <td>").append(esc(r.iEnvironment)).append("</td>\n")
                    .append("  <td>").append(esc(r.iBrowser)).append("</td>\n")
                    .append("  <td>").append(r.getDurationFormatted()).append("</td>\n")
                    .append("  <td style='font-size:11px;color:#718096;'>").append(esc(r.iTimestamp)).append("</td>\n")
                    .append("</tr>\n");
        }

        iSb.append("</tbody>\n</table>\n</div>\n");
        return iSb.toString();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // FAILURE DETAILS SECTION
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildFailureDetails(List<TestCaseResult> pResults)
    {
        long iFailedCount = pResults.stream().filter(r -> !r.isPassed()).count();
        if (iFailedCount == 0) { return ""; }

        StringBuilder iSb = new StringBuilder();
        iSb.append("<div class='failure-section'>\n")
                .append("<div class='section-title'>&#x1F6A8; Failed &amp; Error Test Case Details</div>\n");

        for (TestCaseResult r : pResults)
        {
            if (r.isPassed()) { continue; }

            String iCardId = "fail_" + r.iTestCaseID.replaceAll("[^a-zA-Z0-9]", "_");

            iSb.append("<div class='failure-card'>\n")
                    .append("  <div class='failure-header' onclick=\"toggleFailure('").append(iCardId).append("')\">\n")
                    .append("    <h4>&#x274C; ").append(esc(r.iTestCaseID)).append(" &mdash; ").append(esc(r.iDescription)).append("</h4>\n")
                    .append("    <span class='toggle-icon' id='icon_").append(iCardId).append("'>&#x25BC;</span>\n")
                    .append("  </div>\n")
                    .append("  <div class='failure-body' id='").append(iCardId).append("'>\n");

            // Meta row
            iSb.append("    <div style='display:flex;gap:24px;flex-wrap:wrap;font-size:12px;color:#718096;'>\n")
                    .append("      <span><strong>Status:</strong> <span class='badge ").append(r.getStatusBadgeClass()).append("'>").append(r.iStatus).append("</span></span>\n")
                    .append("      <span><strong>Environment:</strong> ").append(esc(r.iEnvironment)).append("</span>\n")
                    .append("      <span><strong>Browser:</strong> ").append(esc(r.iBrowser)).append("</span>\n")
                    .append("      <span><strong>Duration:</strong> ").append(r.getDurationFormatted()).append("</span>\n")
                    .append("      <span><strong>Executed:</strong> ").append(esc(r.iTimestamp)).append("</span>\n")
                    .append("    </div>\n");

            // Tags
            if (r.iTags != null && !r.iTags.isEmpty())
            {
                iSb.append("    <div class='error-label'>Tags</div>\n")
                        .append("    <div>").append(buildTagHtml(r.iTags)).append("</div>\n");
            }

            // Error message
            iSb.append("    <div class='error-label'>Root Cause / Error Message</div>\n");

            if (r.iErrorMessage != null && !r.iErrorMessage.isEmpty())
            {
                iSb.append("    <div class='error-box'>").append(esc(r.iErrorMessage)).append("</div>\n");
            }
            else
            {
                iSb.append("    <div class='error-box' style='color:#a0aec0;'>No error message captured. "
                        + "Check framework logs for stack trace details.</div>\n");
            }

            // Screenshot
            if (r.iScreenshotPath != null && !r.iScreenshotPath.isEmpty())
            {
                String iBase64 = encodeScreenshot(r.iScreenshotPath);
                if (iBase64 != null)
                {
                    iSb.append("    <div class='error-label'>Screenshot at Point of Failure</div>\n")
                            .append("    <div class='screenshot-embed'>\n")
                            .append("      <img src='data:image/png;base64,").append(iBase64).append("'"
                                    + " alt='Screenshot for ").append(esc(r.iTestCaseID)).append("'/>\n")
                            .append("    </div>\n");
                }
                else
                {
                    iSb.append("    <div class='error-label'>Screenshot</div>\n")
                            .append("    <div style='font-size:12px;color:#e53e3e;'>Screenshot file not found at : "
                                    + esc(r.iScreenshotPath) + "</div>\n");
                }
            }
            else
            {
                iSb.append("    <div style='margin-top:12px;font-size:12px;color:#a0aec0;'>"
                        + "No screenshot was captured for this failure.</div>\n");
            }

            iSb.append("  </div>\n</div>\n");
        }

        iSb.append("</div>\n");
        return iSb.toString();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // FOOTER
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildFooter()
    {
        return "<div class='report-footer'>"
                + "Generated by BISS Automation Framework &bull; "
                + "Bamboo Build: " + esc(ReportManager.getBuildNumber()) + " &bull; "
                + ReportManager.getSuiteEndTime()
                + "</div>\n"
                + "</div>\n"; // close .content
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // JAVASCRIPT : Chart.js initialisation + toggle logic
    // -------------------------------------------------------------------------------------------------------------------------------
    private static String buildScripts(List<TestCaseResult> pResults)
    {
        return "<script>\n"
                // Doughnut chart
                + "var dCtx = document.getElementById('doughnutChart').getContext('2d');\n"
                + "new Chart(dCtx, {\n"
                + "  type: 'doughnut',\n"
                + "  data: {\n"
                + "    labels: ['Pass', 'Fail', 'Error'],\n"
                + "    datasets: [{ data: [window._passCount, window._failCount, window._errorCount],\n"
                + "      backgroundColor: ['#38a169','#e53e3e','#d69e2e'],\n"
                + "      borderWidth: 2, borderColor: '#fff' }]\n"
                + "  },\n"
                + "  options: { responsive:true, plugins:{ legend:{ position:'bottom',"
                + " labels:{ font:{ size:12 }, padding:16 } } } }\n"
                + "});\n\n"

                // Bar chart
                + "var bCtx = document.getElementById('barChart').getContext('2d');\n"
                + "new Chart(bCtx, {\n"
                + "  type: 'bar',\n"
                + "  data: {\n"
                + "    labels: window._chartLabels,\n"
                + "    datasets: [{ label: 'Duration (s)',\n"
                + "      data: window._chartDurations,\n"
                + "      backgroundColor: window._chartColors,\n"
                + "      borderRadius: 6 }]\n"
                + "  },\n"
                + "  options: { responsive:true, plugins:{ legend:{ display:false } },"
                + " scales:{ y:{ beginAtZero:true, grid:{ color:'#edf2f7' },"
                + " ticks:{ font:{ size:11 } } },"
                + " x:{ grid:{ display:false }, ticks:{ font:{ size:11 }, maxRotation:45 } } } }\n"
                + "});\n\n"

                // Failure toggle function
                + "function toggleFailure(id) {\n"
                + "  var body = document.getElementById(id);\n"
                + "  var icon = document.getElementById('icon_' + id);\n"
                + "  if (body.classList.contains('open')) {\n"
                + "    body.classList.remove('open');\n"
                + "    icon.innerHTML = '&#x25BC;';\n"
                + "  } else {\n"
                + "    body.classList.add('open');\n"
                + "    icon.innerHTML = '&#x25B2;';\n"
                + "  }\n"
                + "}\n"

                // Auto-expand all failures on page load
                + "window.addEventListener('load', function() {\n"
                + "  document.querySelectorAll('.failure-body').forEach(function(el) {\n"
                + "    el.classList.add('open');\n"
                + "  });\n"
                + "  document.querySelectorAll('.toggle-icon').forEach(function(el) {\n"
                + "    el.innerHTML = '&#x25B2;';\n"
                + "  });\n"
                + "});\n"
                + "</script>\n";
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // HELPERS
    // -------------------------------------------------------------------------------------------------------------------------------

    private static String buildTagHtml(String pTags)
    {
        if (pTags == null || pTags.trim().isEmpty()) { return "<span style='color:#a0aec0;font-size:11px;'>—</span>"; }
        StringBuilder iSb = new StringBuilder();
        for (String iTag : pTags.split("[,\\s]+"))
        {
            if (!iTag.isEmpty()) { iSb.append("<span class='tag'>").append(esc(iTag)).append("</span>"); }
        }
        return iSb.toString();
    }

    private static String encodeScreenshot(String pPath)
    {
        try
        {
            File iFile = new File(pPath);
            if (!iFile.exists()) { return null; }
            byte[] iBytes = Files.readAllBytes(iFile.toPath());
            return Base64.getEncoder().encodeToString(iBytes);
        }
        catch (IOException iException)
        {
            log.warning("[HtmlReportGenerator] Cannot embed screenshot : " + pPath + " | " + iException.getMessage());
            return null;
        }
    }

    private static String esc(String pInput)
    {
        if (pInput == null) { return ""; }
        return pInput.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}