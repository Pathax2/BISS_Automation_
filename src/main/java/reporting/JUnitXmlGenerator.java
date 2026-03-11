// ===================================================================================================================================
// File          : JUnitXmlGenerator.java
// Package       : reporting
// Description   : Generates a JUnit-compatible XML test results file consumable by Bamboo CI.
//                 Bamboo reads this file from target/surefire-reports/ via its "JUnit Parser" task,
//                 populating the Bamboo build dashboard with pass/fail counts, test names, and durations.
//
//                 Output format follows the JUnit XML schema:
//                   <testsuites>
//                     <testsuite name="..." tests="..." failures="..." errors="..." time="...">
//                       <testcase name="..." classname="..." time="...">
//                         <failure message="...">...</failure>   <!-- only on failure -->
//                       </testcase>
//                     </testsuite>
//                   </testsuites>
//
// Called from   : TestRunner.java — after HtmlReportGenerator.generate()
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import reporting.ReportManager.TestCaseResult;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Logger;

public class JUnitXmlGenerator
{
    private static final Logger log = Logger.getLogger(JUnitXmlGenerator.class.getName());

    // Bamboo JUnit parser looks here by default — matches Maven Surefire output path
    private static final String iXmlOutputDirectory = System.getProperty(
            "junit.xml.path", "target/surefire-reports/");

    private static final String iXmlFileName = "BISS_Execution_Results.xml";

    // ***************************************************************************************************************************************************************************************
    // Function Name : generate
    // Description   : Builds and writes the JUnit XML file. Called once from TestRunner after all test cases complete.
    //                 Bamboo's "JUnit Parser" task must point to target/surefire-reports/ to pick this up automatically.
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
                log.warning("[JUnitXmlGenerator] No results to write. XML generation skipped.");
                return;
            }

            File iDir = new File(iXmlOutputDirectory);
            if (!iDir.exists()) { iDir.mkdirs(); }

            String iXmlPath = iXmlOutputDirectory + iXmlFileName;

            try (FileWriter iWriter = new FileWriter(iXmlPath))
            {
                iWriter.write(buildXml(iResults));
            }

            log.info("[JUnitXmlGenerator] JUnit XML written : " + iXmlPath);
        }
        catch (Exception iException)
        {
            log.severe("[JUnitXmlGenerator] Failed to generate JUnit XML : " + iException.getMessage());
            throw new RuntimeException("JUnit XML generation failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildXml
    // Description   : Assembles the full JUnit XML string
    // Parameters    : pResults (List<TestCaseResult>) - all collected results
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildXml(List<TestCaseResult> pResults)
    {
        int    iTotal    = ReportManager.getTotalCount();
        int    iFail     = ReportManager.getFailCount();
        int    iError    = ReportManager.getErrorCount();
        double iTotalSec = ReportManager.getTotalDurationMs() / 1000.0;

        StringBuilder iXml = new StringBuilder();
        iXml.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        iXml.append("<testsuites>\n");
        iXml.append("  <testsuite\n");
        iXml.append("    name='BISS Automation Suite'\n");
        iXml.append("    tests='").append(iTotal).append("'\n");
        iXml.append("    failures='").append(iFail).append("'\n");
        iXml.append("    errors='").append(iError).append("'\n");
        iXml.append("    skipped='0'\n");
        iXml.append("    time='").append(String.format("%.3f", iTotalSec)).append("'\n");
        iXml.append("    timestamp='").append(xmlEsc(ReportManager.getSuiteStartTime())).append("'\n");
        iXml.append("    hostname='").append(xmlEsc(ReportManager.getBambooAgentName())).append("'>\n\n");

        // Properties block — Bamboo build metadata embedded in XML
        iXml.append("    <properties>\n");
        iXml.append("      <property name='environment' value='").append(xmlEsc(ReportManager.getEnvironment())).append("'/>\n");
        iXml.append("      <property name='browser' value='").append(xmlEsc(ReportManager.getBrowser())).append("'/>\n");
        iXml.append("      <property name='bamboo.buildNumber' value='").append(xmlEsc(ReportManager.getBuildNumber())).append("'/>\n");
        iXml.append("      <property name='bamboo.buildPlanKey' value='").append(xmlEsc(ReportManager.getBuildPlanKey())).append("'/>\n");
        iXml.append("      <property name='executedBy' value='").append(xmlEsc(ReportManager.getExecutedBy())).append("'/>\n");
        iXml.append("    </properties>\n\n");

        for (TestCaseResult r : pResults)
        {
            double iSec = r.iDurationMs / 1000.0;

            iXml.append("    <testcase\n");
            iXml.append("      name='").append(xmlEsc(r.iTestCaseID)).append(" - ").append(xmlEsc(r.iDescription)).append("'\n");
            iXml.append("      classname='").append(xmlEsc("BISS." + r.iEnvironment)).append("'\n");
            iXml.append("      time='").append(String.format("%.3f", iSec)).append("'>\n");

            if (r.isFailed())
            {
                iXml.append("      <failure type='AssertionError' message='")
                        .append(xmlEsc(firstLine(r.iErrorMessage))).append("'>\n");
                iXml.append("        ").append(xmlEsc(r.iErrorMessage)).append("\n");
                iXml.append("      </failure>\n");
            }
            else if (r.isError())
            {
                iXml.append("      <error type='RuntimeException' message='")
                        .append(xmlEsc(firstLine(r.iErrorMessage))).append("'>\n");
                iXml.append("        ").append(xmlEsc(r.iErrorMessage)).append("\n");
                iXml.append("      </error>\n");
            }

            iXml.append("    </testcase>\n\n");
        }

        iXml.append("  </testsuite>\n");
        iXml.append("</testsuites>\n");

        return iXml.toString();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // HELPERS
    // -------------------------------------------------------------------------------------------------------------------------------

    private static String xmlEsc(String pInput)
    {
        if (pInput == null) { return ""; }
        return pInput.replace("&",  "&amp;")
                .replace("<",  "&lt;")
                .replace(">",  "&gt;")
                .replace("\"", "&quot;")
                .replace("'",  "&apos;");
    }

    private static String firstLine(String pInput)
    {
        if (pInput == null || pInput.isEmpty()) { return "No error message captured."; }
        int iNewline = pInput.indexOf('\n');
        return iNewline == -1 ? pInput : pInput.substring(0, iNewline).trim();
    }
}