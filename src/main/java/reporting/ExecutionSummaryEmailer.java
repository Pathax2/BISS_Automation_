// ===================================================================================================================================
// File          : ExecutionSummaryEmailer.java
// Package       : reporting
// Description   : Post-execution email notifier for the BISS automation framework.
//                 Sends a styled HTML summary email after every test run, containing:
//                   • Pass/Fail/Error counts and pass rate
//                   • Environment, Browser, Build number (from Bamboo)
//                   • Table of all test case results with colour-coded status
//                   • List of failed test case IDs with error summaries
//                   • Link text pointing to the HTML report artifact
//
//                 Uses javax.mail (Jakarta Mail). Add to pom.xml:
//                   <dependency>
//                     <groupId>com.sun.mail</groupId>
//                     <artifactId>javax.mail</artifactId>
//                     <version>1.6.2</version>
//                   </dependency>
//
// Folder        : src/main/java/reporting/ExecutionSummaryEmailer.java
//
// Config keys (application.properties / profile overrides):
//   notify.email.enabled=true
//   notify.email.smtp.host=smtp.office365.com
//   notify.email.smtp.port=587
//   notify.email.smtp.starttls=true
//   notify.email.from=automation@biss.ie
//   notify.email.to=qa-team@biss.ie,management@biss.ie
//   notify.email.password  → set ONLY via Bamboo plan variable (-Dnotify.email.password=xxx)
//                            NEVER store in properties files
//
// Called from   : TestRunner.java after HtmlReportGenerator.generate()
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import reporting.ReportManager.TestCaseResult;
import utilities.ConfigManager;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class ExecutionSummaryEmailer
{
    private static final Logger log = Logger.getLogger(ExecutionSummaryEmailer.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Function Name : send
    // Description   : Sends the HTML execution summary email if notify.email.enabled=true.
    //                 Silently skips (with a log) if disabled or if SMTP config is incomplete.
    //                 Never throws — email failure must not fail the build.
    // Parameters    : None (reads from ReportManager singleton)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void send()
    {
        boolean iEnabled = ConfigManager.getBool("notify.email.enabled", false);

        if (!iEnabled)
        {
            log.info("[Emailer] Email notifications disabled. Skipping.");
            return;
        }

        try
        {
            String iHost      = ConfigManager.get("notify.email.smtp.host");
            int    iPort      = ConfigManager.getInt("notify.email.smtp.port", 587);
            boolean iStartTls = ConfigManager.getBool("notify.email.smtp.starttls", true);
            String iFrom      = ConfigManager.get("notify.email.from");
            String iTo        = ConfigManager.get("notify.email.to");
            // Password MUST come from JVM system property set by Bamboo — never from a file
            String iPassword  = System.getProperty("notify.email.password", "");

            if (iPassword.isEmpty())
            {
                log.warning("[Emailer] notify.email.password not set. Email skipped. "
                        + "Set via Bamboo plan variable: -Dnotify.email.password=<value>");
                return;
            }

            String iSubject = buildSubject();
            String iBody    = buildHtmlBody();

            Properties iProps = new Properties();
            iProps.put("mail.smtp.host",            iHost);
            iProps.put("mail.smtp.port",            String.valueOf(iPort));
            iProps.put("mail.smtp.auth",            "true");
            iProps.put("mail.smtp.starttls.enable", String.valueOf(iStartTls));

            final String iFinalPassword = iPassword;
            Session iSession = Session.getInstance(iProps, new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(iFrom, iFinalPassword);
                }
            });

            MimeMessage iMessage = new MimeMessage(iSession);
            iMessage.setFrom(new InternetAddress(iFrom));

            // Support comma-separated recipient list
            for (String iRecipient : iTo.split(","))
            {
                String iTrimmed = iRecipient.trim();
                if (!iTrimmed.isEmpty())
                {
                    iMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(iTrimmed));
                }
            }

            iMessage.setSubject(iSubject);
            iMessage.setContent(iBody, "text/html; charset=UTF-8");

            Transport.send(iMessage);
            log.info("[Emailer] Summary email sent to : " + iTo);
        }
        catch (MessagingException iException)
        {
            // Email failure must NEVER fail the build — log and continue
            log.severe("[Emailer] Failed to send email : " + iException.getMessage());
        }
        catch (Exception iException)
        {
            log.severe("[Emailer] Unexpected emailer error : " + iException.getMessage());
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildSubject
    // Description   : Builds the email subject line with run outcome indicator
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildSubject()
    {
        int iTotal = ReportManager.getTotalCount();
        int iFail  = ReportManager.getFailCount();
        String iOutcome = iFail == 0 ? "✅ ALL PASSED" : "❌ " + iFail + " FAILED";

        return "[BISS Automation] " + iOutcome
                + " | " + iTotal + " Tests"
                + " | " + ReportManager.getEnvironment().toUpperCase()
                + " | Build " + ReportManager.getBuildNumber();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildHtmlBody
    // Description   : Builds the full HTML email body
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static String buildHtmlBody()
    {
        List<TestCaseResult> iResults = ReportManager.getResults();
        int iPass  = ReportManager.getPassCount();
        int iFail  = ReportManager.getFailCount();
        int iError = ReportManager.getErrorCount();
        int iTotal = ReportManager.getTotalCount();

        StringBuilder iHtml = new StringBuilder();

        iHtml.append("<!DOCTYPE html><html><head>")
                .append("<meta charset='UTF-8'>")
                .append("<style>")
                .append("body{font-family:'Segoe UI',Arial,sans-serif;background:#f0f2f5;margin:0;padding:0;}")
                .append(".wrapper{max-width:750px;margin:30px auto;background:white;border-radius:12px;")
                .append("  box-shadow:0 4px 20px rgba(0,0,0,0.1);overflow:hidden;}")
                .append(".header{background:linear-gradient(135deg,#1a1a2e,#0f3460);color:white;padding:28px 32px;}")
                .append(".header h1{margin:0;font-size:22px;letter-spacing:0.5px;}")
                .append(".header p{margin:6px 0 0;font-size:13px;color:#a0aec0;}")
                .append(".body{padding:28px 32px;}")
                .append(".cards{display:flex;gap:16px;margin-bottom:24px;flex-wrap:wrap;}")
                .append(".card{flex:1;min-width:100px;padding:16px;border-radius:10px;text-align:center;}")
                .append(".card .val{font-size:32px;font-weight:700;}")
                .append(".card .lbl{font-size:11px;color:#718096;text-transform:uppercase;letter-spacing:1px;margin-top:4px;}")
                .append(".card.total{background:#edf2f7;} .card.total .val{color:#2d3748;}")
                .append(".card.pass{background:#c6f6d5;}  .card.pass  .val{color:#276749;}")
                .append(".card.fail{background:#fed7d7;}  .card.fail  .val{color:#9b2c2c;}")
                .append(".card.rate{background:#ebf8ff;}  .card.rate  .val{color:#2b6cb0;font-size:22px;}")
                .append(".meta{background:#f7fafc;border-radius:8px;padding:14px 18px;margin-bottom:24px;")
                .append("  font-size:13px;color:#4a5568;display:flex;gap:24px;flex-wrap:wrap;}")
                .append(".meta strong{color:#2d3748;}")
                .append("table{width:100%;border-collapse:collapse;font-size:13px;margin-bottom:24px;}")
                .append("thead tr{background:#2d3748;color:white;}")
                .append("thead th{padding:10px 12px;text-align:left;font-weight:500;}")
                .append("tbody tr{border-bottom:1px solid #edf2f7;}")
                .append("tbody td{padding:9px 12px;}")
                .append(".badge{display:inline-block;padding:2px 9px;border-radius:10px;font-size:11px;font-weight:700;}")
                .append(".badge-pass{background:#c6f6d5;color:#276749;}")
                .append(".badge-fail{background:#fed7d7;color:#9b2c2c;}")
                .append(".badge-error{background:#fefcbf;color:#744210;}")
                .append(".row-fail{background:#fff5f5;border-left:3px solid #e53e3e;}")
                .append(".failure-box{background:#2d3748;color:#fc8181;padding:12px;border-radius:8px;")
                .append("  font-family:monospace;font-size:12px;margin-top:6px;white-space:pre-wrap;word-break:break-word;}")
                .append(".footer{text-align:center;padding:18px;font-size:12px;color:#a0aec0;background:#f7fafc;}")
                .append("</style></head><body>")
                .append("<div class='wrapper'>")

                // Header
                .append("<div class='header'>")
                .append("<h1>&#x1F4CA; BISS Automation — Execution Summary</h1>")
                .append("<p>Automated test run results for management review</p>")
                .append("</div>")

                .append("<div class='body'>")

                // Summary cards
                .append("<div class='cards'>")
                .append(card("total", String.valueOf(iTotal), "Total"))
                .append(card("pass",  String.valueOf(iPass),  "Passed"))
                .append(card("fail",  String.valueOf(iFail + iError), "Failed"))
                .append(card("rate",  ReportManager.getPassPercent() + "%", "Pass Rate"))
                .append("</div>")

                // Metadata
                .append("<div class='meta'>")
                .append("<span><strong>Environment</strong> ").append(esc(ReportManager.getEnvironment())).append("</span>")
                .append("<span><strong>Browser</strong> ").append(esc(ReportManager.getBrowser())).append("</span>")
                .append("<span><strong>Build</strong> ").append(esc(ReportManager.getBuildNumber())).append("</span>")
                .append("<span><strong>Plan</strong> ").append(esc(ReportManager.getBuildPlanKey())).append("</span>")
                .append("<span><strong>Started</strong> ").append(esc(ReportManager.getSuiteStartTime())).append("</span>")
                .append("<span><strong>Duration</strong> ").append(ReportManager.getSuiteDurationFormatted()).append("</span>")
                .append("</div>")

                // Results table
                .append("<table>")
                .append("<thead><tr><th>#</th><th>Test Case ID</th><th>Description</th>")
                .append("<th>Status</th><th>Duration</th></tr></thead><tbody>");

        int iIdx = 1;
        for (TestCaseResult r : iResults)
        {
            String iRowClass = r.isPassed() ? "" : "row-fail";
            iHtml.append("<tr class='").append(iRowClass).append("'>")
                    .append("<td>").append(iIdx++).append("</td>")
                    .append("<td><strong>").append(esc(r.iTestCaseID)).append("</strong></td>")
                    .append("<td>").append(esc(r.iDescription)).append("</td>")
                    .append("<td><span class='badge ").append(r.getStatusBadgeClass()).append("'>")
                    .append(r.iStatus).append("</span></td>")
                    .append("<td>").append(r.getDurationFormatted()).append("</td>")
                    .append("</tr>");
        }

        iHtml.append("</tbody></table>");

        // Failure details section
        long iFailCount = iResults.stream().filter(r -> !r.isPassed()).count();
        if (iFailCount > 0)
        {
            iHtml.append("<h3 style='color:#c53030;font-size:15px;margin-bottom:12px;'>")
                    .append("&#x274C; Failed Test Details</h3>");

            for (TestCaseResult r : iResults)
            {
                if (r.isPassed()) { continue; }

                iHtml.append("<div style='margin-bottom:20px;'>")
                        .append("<strong style='color:#c53030;'>").append(esc(r.iTestCaseID)).append("</strong>")
                        .append(" — ").append(esc(r.iDescription))
                        .append("<div class='failure-box'>")
                        .append(esc(r.iErrorMessage.isEmpty() ? "No error message captured. Check Bamboo build logs." : r.iErrorMessage))
                        .append("</div></div>");
            }
        }

        // Report link note
        iHtml.append("<div style='background:#ebf8ff;border-radius:8px;padding:14px 18px;font-size:13px;color:#2b6cb0;'>")
                .append("&#x1F4CE; Full execution report with screenshots and charts is available as a ")
                .append("<strong>Bamboo build artifact</strong> under the build for plan ")
                .append("<strong>").append(esc(ReportManager.getBuildPlanKey())).append("</strong>")
                .append(", Build #").append(esc(ReportManager.getBuildNumber())).append(".")
                .append("</div>")

                .append("</div>") // end body
                .append("<div class='footer'>BISS Automation Framework &bull; ")
                .append(ReportManager.getSuiteEndTime())
                .append("</div>")
                .append("</div></body></html>");

        return iHtml.toString();
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------------------------------------------------------------

    private static String card(String pType, String pVal, String pLabel)
    {
        return "<div class='card " + pType + "'>"
                + "<div class='val'>" + pVal + "</div>"
                + "<div class='lbl'>" + pLabel + "</div>"
                + "</div>";
    }

    private static String esc(String pInput)
    {
        if (pInput == null) { return ""; }
        return pInput.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}