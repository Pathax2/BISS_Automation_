// ===================================================================================================================================
// File          : HtmlReportGenerator.java
// Package       : reporting
// Description   : Generates a self-contained, industry-grade HTML test execution report
//                 with an executive dashboard, donut charts, step-level drill-down,
//                 per-step logs/screenshots, and full 4K responsive design.
//
// Integration Points:
//   - ReportManager.java : consumes getResults() and suite-level accessors
//   - TestRunner.java    : calls HtmlReportGenerator.generate() after suite completion
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package reporting;

import reporting.ReportManager.TestCaseResult;
import reporting.ReportManager.StepResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class HtmlReportGenerator
{
    private static final Logger log = Logger.getLogger(HtmlReportGenerator.class.getName());

    // ***************************************************************************************************************************************************************************************
    // Function Name : generate
    // Description   : Builds and writes the full HTML report to the specified file path.
    // Parameters    : pOutputPath (String) - absolute or relative path for the output .html file
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void generate(String pOutputPath)
    {
        List<TestCaseResult> iResults = ReportManager.getResults();

        int iTotal   = ReportManager.getTotalCount();
        int iPass    = ReportManager.getPassCount();
        int iFail    = ReportManager.getFailCount();
        int iError   = ReportManager.getErrorCount();
        int iTotalSteps   = ReportManager.getTotalStepCount();
        int iPassSteps    = ReportManager.getPassedStepCount();
        int iFailSteps    = ReportManager.getFailedStepCount();
        int iSkipSteps    = ReportManager.getSkippedStepCount();

        String iPassPct = ReportManager.getPassPercent();
        String iFailPct = ReportManager.getFailPercent();
        String iDuration = ReportManager.getSuiteDurationFormatted();

        StringBuilder iHtml = new StringBuilder(32768);

        // ===========================================================================================================================
        // HTML HEAD
        // ===========================================================================================================================
        iHtml.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
        iHtml.append("<meta charset=\"UTF-8\">\n");
        iHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        iHtml.append("<title>BISS Test Execution Report</title>\n");

        // Google Fonts
        iHtml.append("<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n");
        iHtml.append("<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n");
        iHtml.append("<link href=\"https://fonts.googleapis.com/css2?family=DM+Sans:ital,wght@0,400;0,500;0,600;0,700&family=JetBrains+Mono:wght@400;500&family=Plus+Jakarta+Sans:wght@600;700;800&display=swap\" rel=\"stylesheet\">\n");

        // ===========================================================================================================================
        // EMBEDDED CSS
        // ===========================================================================================================================
        iHtml.append("<style>\n");
        iHtml.append(buildCSS());
        iHtml.append("</style>\n");

        iHtml.append("</head>\n<body>\n");

        // ===========================================================================================================================
        // MAIN CONTAINER
        // ===========================================================================================================================
        iHtml.append("<div class=\"report-shell\">\n");

        // --- TOP BAR ---
        iHtml.append("<header class=\"top-bar\">\n");
        iHtml.append("  <div class=\"top-bar-left\">\n");
        iHtml.append("    <div class=\"logo\">BISS</div>\n");
        iHtml.append("    <span class=\"top-bar-divider\"></span>\n");
        iHtml.append("    <span class=\"top-bar-label\">Automation Execution Report</span>\n");
        iHtml.append("  </div>\n");
        iHtml.append("  <div class=\"top-bar-right\">\n");
        iHtml.append("    <span class=\"meta-pill\">").append(esc(ReportManager.getEnvironment())).append("</span>\n");
        iHtml.append("    <span class=\"meta-pill\">").append(esc(ReportManager.getBrowser())).append("</span>\n");
        iHtml.append("    <span class=\"meta-pill\">Build ").append(esc(ReportManager.getBuildNumber())).append("</span>\n");
        iHtml.append("  </div>\n");
        iHtml.append("</header>\n");

        // --- EXECUTION METADATA RIBBON ---
        iHtml.append("<section class=\"meta-ribbon\">\n");
        appendMetaItem(iHtml, "Plan", ReportManager.getBuildPlanKey());
        appendMetaItem(iHtml, "Agent", ReportManager.getBambooAgentName());
        appendMetaItem(iHtml, "Executed By", ReportManager.getExecutedBy());
        appendMetaItem(iHtml, "Started", ReportManager.getSuiteStartTime());
        appendMetaItem(iHtml, "Finished", ReportManager.getSuiteEndTime());
        appendMetaItem(iHtml, "Duration", iDuration);
        iHtml.append("</section>\n");

        // --- KPI CARDS ---
        iHtml.append("<section class=\"kpi-row\">\n");
        appendKpiCard(iHtml, "Total Tests",   String.valueOf(iTotal),  "kpi-total",  "");
        appendKpiCard(iHtml, "Passed",         String.valueOf(iPass),   "kpi-pass",   iPassPct + "%");
        appendKpiCard(iHtml, "Failed",         String.valueOf(iFail),   "kpi-fail",   iFailPct + "%");
        appendKpiCard(iHtml, "Errors",         String.valueOf(iError),  "kpi-error",  "");
        appendKpiCard(iHtml, "Total Steps",    String.valueOf(iTotalSteps), "kpi-total", "");
        appendKpiCard(iHtml, "Steps Passed",   String.valueOf(iPassSteps),  "kpi-pass",  "");
        appendKpiCard(iHtml, "Steps Failed",   String.valueOf(iFailSteps),  "kpi-fail",  "");
        appendKpiCard(iHtml, "Steps Skipped",  String.valueOf(iSkipSteps),  "kpi-skip",  "");
        iHtml.append("</section>\n");

        // --- CHART ROW ---
        iHtml.append("<section class=\"chart-row\">\n");
        iHtml.append("  <div class=\"chart-card\">\n");
        iHtml.append("    <h3 class=\"chart-title\">Test Case Distribution</h3>\n");
        iHtml.append("    <canvas id=\"tcChart\" width=\"280\" height=\"280\"></canvas>\n");
        iHtml.append("  </div>\n");
        iHtml.append("  <div class=\"chart-card\">\n");
        iHtml.append("    <h3 class=\"chart-title\">Step Distribution</h3>\n");
        iHtml.append("    <canvas id=\"stepChart\" width=\"280\" height=\"280\"></canvas>\n");
        iHtml.append("  </div>\n");
        iHtml.append("  <div class=\"chart-card chart-card-wide\">\n");
        iHtml.append("    <h3 class=\"chart-title\">Execution Timeline (ms)</h3>\n");
        iHtml.append("    <canvas id=\"timelineChart\" width=\"700\" height=\"280\"></canvas>\n");
        iHtml.append("  </div>\n");
        iHtml.append("</section>\n");

        // --- FILTER BAR ---
        iHtml.append("<section class=\"filter-bar\">\n");
        iHtml.append("  <input type=\"text\" id=\"searchBox\" class=\"search-input\" placeholder=\"Search by ID, description, tags\u2026\" />\n");
        iHtml.append("  <div class=\"filter-pills\">\n");
        iHtml.append("    <button class=\"filter-pill active\" data-filter=\"ALL\">All (").append(iTotal).append(")</button>\n");
        iHtml.append("    <button class=\"filter-pill fp-pass\" data-filter=\"PASS\">Passed (").append(iPass).append(")</button>\n");
        iHtml.append("    <button class=\"filter-pill fp-fail\" data-filter=\"FAIL\">Failed (").append(iFail).append(")</button>\n");
        iHtml.append("    <button class=\"filter-pill fp-error\" data-filter=\"ERROR\">Error (").append(iError).append(")</button>\n");
        iHtml.append("  </div>\n");
        iHtml.append("</section>\n");

        // --- RESULTS TABLE ---
        iHtml.append("<section class=\"results-section\">\n");
        iHtml.append("<table class=\"results-table\" id=\"resultsTable\">\n");
        iHtml.append("<thead><tr>\n");
        iHtml.append("  <th class=\"th-expand\"></th>\n");
        iHtml.append("  <th class=\"th-idx\">#</th>\n");
        iHtml.append("  <th class=\"th-id\">Test Case ID</th>\n");
        iHtml.append("  <th class=\"th-desc\">Description</th>\n");
        iHtml.append("  <th class=\"th-tags\">Tags</th>\n");
        iHtml.append("  <th class=\"th-steps\">Steps</th>\n");
        iHtml.append("  <th class=\"th-dur\">Duration</th>\n");
        iHtml.append("  <th class=\"th-status\">Status</th>\n");
        iHtml.append("</tr></thead>\n<tbody>\n");

        for (int i = 0; i < iResults.size(); i++)
        {
            TestCaseResult r = iResults.get(i);
            String iRowId = "row-" + i;

            // Main row
            iHtml.append("<tr class=\"tc-row ").append(r.getRowClass()).append("\" data-status=\"").append(esc(r.iStatus)).append("\" data-search=\"")
                    .append(esc((r.iTestCaseID + " " + r.iDescription + " " + r.iTags).toLowerCase())).append("\">\n");
            iHtml.append("  <td class=\"td-expand\"><button class=\"expand-btn\" onclick=\"toggleDetail('").append(iRowId).append("')\" aria-label=\"Expand\">&#9654;</button></td>\n");
            iHtml.append("  <td class=\"td-idx\">").append(i + 1).append("</td>\n");
            iHtml.append("  <td class=\"td-id\"><code>").append(esc(r.iTestCaseID)).append("</code></td>\n");
            iHtml.append("  <td class=\"td-desc\">").append(esc(r.iDescription)).append("</td>\n");
            iHtml.append("  <td class=\"td-tags\">").append(renderTags(r.iTags)).append("</td>\n");
            iHtml.append("  <td class=\"td-steps\">").append(r.iSteps.size()).append(" <span class=\"step-mini\">(");
            iHtml.append("<span class=\"sm-pass\">").append(r.getStepPassCount()).append("P</span>");
            if (r.getStepFailCount() > 0)
                iHtml.append(" <span class=\"sm-fail\">").append(r.getStepFailCount()).append("F</span>");
            if (r.getStepSkipCount() > 0)
                iHtml.append(" <span class=\"sm-skip\">").append(r.getStepSkipCount()).append("S</span>");
            iHtml.append(")</span></td>\n");
            iHtml.append("  <td class=\"td-dur\"><code>").append(esc(r.getDurationFormatted())).append("</code></td>\n");
            iHtml.append("  <td class=\"td-status\"><span class=\"badge ").append(r.getStatusBadgeClass()).append("\">").append(esc(r.iStatus)).append("</span></td>\n");
            iHtml.append("</tr>\n");

            // Detail row (hidden by default)
            iHtml.append("<tr class=\"detail-row\" id=\"").append(iRowId).append("\" style=\"display:none;\">\n");
            iHtml.append("  <td colspan=\"8\">\n");
            iHtml.append("    <div class=\"detail-panel\">\n");

            // Error message (if any)
            if (!r.iErrorMessage.isEmpty())
            {
                iHtml.append("      <div class=\"detail-error\">\n");
                iHtml.append("        <div class=\"detail-error-title\">&#9888; Failure Reason</div>\n");
                iHtml.append("        <pre class=\"error-pre\">").append(esc(r.iErrorMessage)).append("</pre>\n");
                iHtml.append("      </div>\n");
            }

            // Screenshot (if any)
            if (!r.iScreenshotPath.isEmpty())
            {
                iHtml.append("      <div class=\"detail-screenshot\">\n");
                iHtml.append("        <span class=\"screenshot-label\">&#128247; Screenshot</span>\n");
                iHtml.append("        <a href=\"").append(esc(r.iScreenshotPath)).append("\" target=\"_blank\" class=\"screenshot-link\">").append(esc(r.iScreenshotPath)).append("</a>\n");
                iHtml.append("      </div>\n");
            }

            // Steps table
            if (!r.iSteps.isEmpty())
            {
                iHtml.append("      <div class=\"steps-container\">\n");
                iHtml.append("        <div class=\"steps-header\">Step-by-Step Execution</div>\n");
                iHtml.append("        <table class=\"steps-table\">\n");
                iHtml.append("          <thead><tr>\n");
                iHtml.append("            <th class=\"sth-idx\">#</th>\n");
                iHtml.append("            <th class=\"sth-kw\">Keyword</th>\n");
                iHtml.append("            <th class=\"sth-text\">Step</th>\n");
                iHtml.append("            <th class=\"sth-dur\">Duration</th>\n");
                iHtml.append("            <th class=\"sth-status\">Status</th>\n");
                iHtml.append("          </tr></thead>\n<tbody>\n");

                for (int s = 0; s < r.iSteps.size(); s++)
                {
                    StepResult step = r.iSteps.get(s);
                    String iStepRowId = iRowId + "-step-" + s;
                    String iStepRowClass = step.isPassed() ? "step-row-pass" : step.isFailed() ? "step-row-fail" : "step-row-skip";

                    iHtml.append("          <tr class=\"step-row ").append(iStepRowClass).append("\">\n");
                    iHtml.append("            <td class=\"std-idx\">").append(s + 1).append("</td>\n");
                    iHtml.append("            <td class=\"std-kw\"><span class=\"kw-badge kw-").append(esc(step.iStepKeyword.toLowerCase())).append("\">").append(esc(step.iStepKeyword)).append("</span></td>\n");
                    iHtml.append("            <td class=\"std-text\">\n");
                    iHtml.append("              <span class=\"step-text\">").append(esc(step.iStepText)).append("</span>\n");

                    // Step log toggle
                    boolean iHasLog   = !step.iLogMessage.isEmpty();
                    boolean iHasError = !step.iErrorMessage.isEmpty();
                    boolean iHasShot  = !step.iScreenshotPath.isEmpty();

                    if (iHasLog || iHasError || iHasShot)
                    {
                        iHtml.append("              <button class=\"step-detail-toggle\" onclick=\"toggleStepLog('").append(iStepRowId).append("')\">View Logs &#9660;</button>\n");
                        iHtml.append("              <div class=\"step-log-panel\" id=\"").append(iStepRowId).append("\" style=\"display:none;\">\n");

                        if (iHasLog)
                        {
                            iHtml.append("                <div class=\"step-log-section\">\n");
                            iHtml.append("                  <div class=\"step-log-label\">&#128196; Execution Log</div>\n");
                            iHtml.append("                  <pre class=\"step-log-pre\">").append(esc(step.iLogMessage)).append("</pre>\n");
                            iHtml.append("                </div>\n");
                        }
                        if (iHasError)
                        {
                            iHtml.append("                <div class=\"step-log-section step-log-error\">\n");
                            iHtml.append("                  <div class=\"step-log-label\">&#9888; Error</div>\n");
                            iHtml.append("                  <pre class=\"step-log-pre step-error-pre\">").append(esc(step.iErrorMessage)).append("</pre>\n");
                            iHtml.append("                </div>\n");
                        }
                        if (iHasShot)
                        {
                            iHtml.append("                <div class=\"step-log-section\">\n");
                            iHtml.append("                  <div class=\"step-log-label\">&#128247; Screenshot</div>\n");
                            iHtml.append("                  <a href=\"").append(esc(step.iScreenshotPath)).append("\" target=\"_blank\" class=\"screenshot-link\">").append(esc(step.iScreenshotPath)).append("</a>\n");
                            iHtml.append("                </div>\n");
                        }

                        iHtml.append("              </div>\n");
                    }

                    iHtml.append("            </td>\n");
                    iHtml.append("            <td class=\"std-dur\"><code>").append(esc(step.getDurationFormatted())).append("</code></td>\n");
                    iHtml.append("            <td class=\"std-status\"><span class=\"badge ").append(step.getStatusBadgeClass()).append("\">").append(esc(step.iStatus)).append("</span></td>\n");
                    iHtml.append("          </tr>\n");
                }

                iHtml.append("        </tbody></table>\n");
                iHtml.append("      </div>\n");
            }

            iHtml.append("    </div>\n");
            iHtml.append("  </td>\n</tr>\n");
        }

        iHtml.append("</tbody>\n</table>\n");
        iHtml.append("</section>\n");

        // --- FOOTER ---
        iHtml.append("<footer class=\"report-footer\">\n");
        iHtml.append("  <span>BISS Automation Framework</span>\n");
        iHtml.append("  <span class=\"footer-dot\">&middot;</span>\n");
        iHtml.append("  <span>Generated ").append(esc(ReportManager.getSuiteEndTime())).append("</span>\n");
        iHtml.append("  <span class=\"footer-dot\">&middot;</span>\n");
        iHtml.append("  <span>").append(esc(ReportManager.getExecutedBy())).append("</span>\n");
        iHtml.append("</footer>\n");

        iHtml.append("</div>\n");   // close report-shell

        // ===========================================================================================================================
        // EMBEDDED JAVASCRIPT
        // ===========================================================================================================================
        iHtml.append("<script>\n");
        iHtml.append(buildJS(iPass, iFail, iError, iPassSteps, iFailSteps, iSkipSteps, iResults));
        iHtml.append("</script>\n");

        iHtml.append("</body>\n</html>\n");

        // --- WRITE FILE ---
        writeFile(pOutputPath, iHtml.toString());
    }

    // ===============================================================================================================================
    // CSS BUILDER
    // ===============================================================================================================================
    private static String buildCSS()
    {
        StringBuilder c = new StringBuilder(16384);

        // --- CSS VARIABLES / ROOT ---
        c.append(":root {\n");
        c.append("  --bg-primary: #0B0E11;\n");
        c.append("  --bg-secondary: #131720;\n");
        c.append("  --bg-tertiary: #1A1F2E;\n");
        c.append("  --bg-card: #161B26;\n");
        c.append("  --bg-card-hover: #1C2233;\n");
        c.append("  --bg-input: #0F1318;\n");
        c.append("  --border-primary: #232A3B;\n");
        c.append("  --border-subtle: #1C2233;\n");
        c.append("  --text-primary: #E8ECF4;\n");
        c.append("  --text-secondary: #8B95A8;\n");
        c.append("  --text-tertiary: #5A6478;\n");
        c.append("  --accent-pass: #10B981;\n");
        c.append("  --accent-pass-bg: rgba(16,185,129,0.08);\n");
        c.append("  --accent-pass-border: rgba(16,185,129,0.20);\n");
        c.append("  --accent-fail: #EF4444;\n");
        c.append("  --accent-fail-bg: rgba(239,68,68,0.08);\n");
        c.append("  --accent-fail-border: rgba(239,68,68,0.20);\n");
        c.append("  --accent-error: #F59E0B;\n");
        c.append("  --accent-error-bg: rgba(245,158,11,0.08);\n");
        c.append("  --accent-error-border: rgba(245,158,11,0.20);\n");
        c.append("  --accent-skip: #6366F1;\n");
        c.append("  --accent-skip-bg: rgba(99,102,241,0.08);\n");
        c.append("  --accent-skip-border: rgba(99,102,241,0.20);\n");
        c.append("  --accent-blue: #3B82F6;\n");
        c.append("  --radius-sm: 6px;\n");
        c.append("  --radius-md: 10px;\n");
        c.append("  --radius-lg: 14px;\n");
        c.append("  --shadow-card: 0 1px 3px rgba(0,0,0,0.4), 0 0 0 1px var(--border-primary);\n");
        c.append("  --shadow-elevated: 0 8px 32px rgba(0,0,0,0.5);\n");
        c.append("  --font-body: 'DM Sans', -apple-system, BlinkMacSystemFont, sans-serif;\n");
        c.append("  --font-heading: 'Plus Jakarta Sans', 'DM Sans', sans-serif;\n");
        c.append("  --font-mono: 'JetBrains Mono', 'Fira Code', monospace;\n");
        c.append("}\n\n");

        // --- RESET & BASE ---
        c.append("*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }\n");
        c.append("html { font-size: 15px; -webkit-font-smoothing: antialiased; -moz-osx-font-smoothing: grayscale; }\n");
        c.append("body { font-family: var(--font-body); color: var(--text-primary); background: var(--bg-primary); line-height: 1.6; }\n\n");

        // --- SHELL ---
        c.append(".report-shell { max-width: 1920px; margin: 0 auto; padding: 0 32px 48px; }\n");
        c.append("@media (min-width: 2560px) { .report-shell { max-width: 2400px; } html { font-size: 17px; } }\n");
        c.append("@media (min-width: 3840px) { .report-shell { max-width: 3600px; } html { font-size: 20px; } }\n\n");

        // --- TOP BAR ---
        c.append(".top-bar { display: flex; align-items: center; justify-content: space-between; padding: 20px 0; border-bottom: 1px solid var(--border-primary); margin-bottom: 24px; }\n");
        c.append(".top-bar-left { display: flex; align-items: center; gap: 16px; }\n");
        c.append(".logo { font-family: var(--font-heading); font-weight: 800; font-size: 1.6rem; letter-spacing: -0.04em; background: linear-gradient(135deg, #3B82F6, #8B5CF6); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text; }\n");
        c.append(".top-bar-divider { width: 1px; height: 24px; background: var(--border-primary); }\n");
        c.append(".top-bar-label { font-size: 0.93rem; color: var(--text-secondary); font-weight: 500; letter-spacing: 0.02em; }\n");
        c.append(".top-bar-right { display: flex; gap: 8px; }\n");
        c.append(".meta-pill { font-size: 0.78rem; font-weight: 600; padding: 5px 14px; border-radius: 100px; background: var(--bg-tertiary); color: var(--text-secondary); border: 1px solid var(--border-primary); letter-spacing: 0.03em; text-transform: uppercase; }\n\n");

        // --- META RIBBON ---
        c.append(".meta-ribbon { display: flex; flex-wrap: wrap; gap: 24px; padding: 16px 24px; background: var(--bg-card); border: 1px solid var(--border-primary); border-radius: var(--radius-lg); margin-bottom: 24px; }\n");
        c.append(".meta-item { display: flex; flex-direction: column; gap: 2px; }\n");
        c.append(".meta-label { font-size: 0.7rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.08em; color: var(--text-tertiary); }\n");
        c.append(".meta-value { font-size: 0.88rem; font-weight: 500; color: var(--text-primary); font-family: var(--font-mono); }\n\n");

        // --- KPI CARDS ---
        c.append(".kpi-row { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 14px; margin-bottom: 24px; }\n");
        c.append(".kpi-card { background: var(--bg-card); border: 1px solid var(--border-primary); border-radius: var(--radius-md); padding: 20px 22px; position: relative; overflow: hidden; transition: transform 0.15s, box-shadow 0.15s; }\n");
        c.append(".kpi-card:hover { transform: translateY(-2px); box-shadow: var(--shadow-elevated); }\n");
        c.append(".kpi-card::before { content: ''; position: absolute; top: 0; left: 0; right: 0; height: 3px; }\n");
        c.append(".kpi-total::before  { background: linear-gradient(90deg, var(--accent-blue), #6366F1); }\n");
        c.append(".kpi-pass::before   { background: var(--accent-pass); }\n");
        c.append(".kpi-fail::before   { background: var(--accent-fail); }\n");
        c.append(".kpi-error::before  { background: var(--accent-error); }\n");
        c.append(".kpi-skip::before   { background: var(--accent-skip); }\n");
        c.append(".kpi-label { font-size: 0.72rem; font-weight: 600; text-transform: uppercase; letter-spacing: 0.08em; color: var(--text-tertiary); margin-bottom: 8px; }\n");
        c.append(".kpi-value { font-family: var(--font-heading); font-size: 2rem; font-weight: 800; letter-spacing: -0.03em; color: var(--text-primary); }\n");
        c.append(".kpi-sub { font-size: 0.82rem; color: var(--text-tertiary); margin-left: 6px; font-weight: 500; }\n\n");

        // --- CHART ROW ---
        c.append(".chart-row { display: grid; grid-template-columns: 1fr 1fr 2fr; gap: 14px; margin-bottom: 24px; }\n");
        c.append("@media (max-width: 1200px) { .chart-row { grid-template-columns: 1fr; } }\n");
        c.append(".chart-card { background: var(--bg-card); border: 1px solid var(--border-primary); border-radius: var(--radius-md); padding: 24px; display: flex; flex-direction: column; align-items: center; }\n");
        c.append(".chart-card-wide { align-items: stretch; }\n");
        c.append(".chart-title { font-family: var(--font-heading); font-size: 0.85rem; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 16px; text-align: center; }\n\n");

        // --- FILTER BAR ---
        c.append(".filter-bar { display: flex; align-items: center; gap: 16px; margin-bottom: 16px; flex-wrap: wrap; }\n");
        c.append(".search-input { flex: 1; min-width: 220px; padding: 10px 16px; border-radius: var(--radius-md); border: 1px solid var(--border-primary); background: var(--bg-input); color: var(--text-primary); font-family: var(--font-body); font-size: 0.88rem; outline: none; transition: border-color 0.2s; }\n");
        c.append(".search-input:focus { border-color: var(--accent-blue); }\n");
        c.append(".search-input::placeholder { color: var(--text-tertiary); }\n");
        c.append(".filter-pills { display: flex; gap: 6px; }\n");
        c.append(".filter-pill { padding: 7px 16px; border-radius: 100px; border: 1px solid var(--border-primary); background: var(--bg-tertiary); color: var(--text-secondary); font-size: 0.78rem; font-weight: 600; cursor: pointer; transition: all 0.15s; }\n");
        c.append(".filter-pill:hover { border-color: var(--accent-blue); color: var(--text-primary); }\n");
        c.append(".filter-pill.active { background: var(--accent-blue); color: #fff; border-color: var(--accent-blue); }\n");
        c.append(".fp-pass.active { background: var(--accent-pass); border-color: var(--accent-pass); }\n");
        c.append(".fp-fail.active { background: var(--accent-fail); border-color: var(--accent-fail); }\n");
        c.append(".fp-error.active { background: var(--accent-error); border-color: var(--accent-error); }\n\n");

        // --- RESULTS TABLE ---
        c.append(".results-section { background: var(--bg-card); border: 1px solid var(--border-primary); border-radius: var(--radius-lg); overflow: hidden; }\n");
        c.append(".results-table { width: 100%; border-collapse: collapse; }\n");
        c.append(".results-table thead { background: var(--bg-tertiary); }\n");
        c.append(".results-table th { padding: 14px 16px; font-size: 0.72rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.08em; color: var(--text-tertiary); text-align: left; border-bottom: 1px solid var(--border-primary); }\n");
        c.append(".results-table td { padding: 12px 16px; font-size: 0.88rem; border-bottom: 1px solid var(--border-subtle); vertical-align: middle; }\n");
        c.append(".th-expand, .td-expand { width: 40px; text-align: center; }\n");
        c.append(".th-idx, .td-idx { width: 48px; text-align: center; color: var(--text-tertiary); }\n");
        c.append(".th-id { width: 140px; }\n");
        c.append(".td-id code { font-family: var(--font-mono); font-size: 0.82rem; color: var(--accent-blue); font-weight: 500; }\n");
        c.append(".th-desc { min-width: 200px; }\n");
        c.append(".th-tags, .td-tags { width: 160px; }\n");
        c.append(".th-steps, .td-steps { width: 120px; }\n");
        c.append(".th-dur, .td-dur { width: 100px; }\n");
        c.append(".td-dur code { font-family: var(--font-mono); font-size: 0.82rem; color: var(--text-secondary); }\n");
        c.append(".th-status, .td-status { width: 90px; text-align: center; }\n\n");

        // Row hover
        c.append(".tc-row { transition: background 0.12s; cursor: pointer; }\n");
        c.append(".tc-row:hover { background: var(--bg-card-hover); }\n");
        c.append(".row-pass { border-left: 3px solid var(--accent-pass); }\n");
        c.append(".row-fail { border-left: 3px solid var(--accent-fail); }\n");
        c.append(".row-error { border-left: 3px solid var(--accent-error); }\n\n");

        // Expand button
        c.append(".expand-btn { background: none; border: none; color: var(--text-tertiary); cursor: pointer; font-size: 0.75rem; padding: 4px 6px; border-radius: 4px; transition: all 0.15s; }\n");
        c.append(".expand-btn:hover { color: var(--accent-blue); background: rgba(59,130,246,0.1); }\n");
        c.append(".expand-btn.open { transform: rotate(90deg); color: var(--accent-blue); }\n\n");

        // Badge
        c.append(".badge { display: inline-block; padding: 4px 12px; border-radius: 100px; font-size: 0.72rem; font-weight: 700; letter-spacing: 0.06em; text-transform: uppercase; }\n");
        c.append(".badge-pass  { background: var(--accent-pass-bg); color: var(--accent-pass); border: 1px solid var(--accent-pass-border); }\n");
        c.append(".badge-fail  { background: var(--accent-fail-bg); color: var(--accent-fail); border: 1px solid var(--accent-fail-border); }\n");
        c.append(".badge-error { background: var(--accent-error-bg); color: var(--accent-error); border: 1px solid var(--accent-error-border); }\n");
        c.append(".badge-skip  { background: var(--accent-skip-bg); color: var(--accent-skip); border: 1px solid var(--accent-skip-border); }\n\n");

        // Tags
        c.append(".tag-chip { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 0.7rem; font-family: var(--font-mono); background: var(--bg-tertiary); color: var(--text-secondary); margin: 1px 2px; border: 1px solid var(--border-subtle); }\n\n");

        // Step-mini indicators
        c.append(".step-mini { font-size: 0.72rem; color: var(--text-tertiary); }\n");
        c.append(".sm-pass { color: var(--accent-pass); font-weight: 600; }\n");
        c.append(".sm-fail { color: var(--accent-fail); font-weight: 600; }\n");
        c.append(".sm-skip { color: var(--accent-skip); font-weight: 600; }\n\n");

        // --- DETAIL PANEL ---
        c.append(".detail-row td { padding: 0 !important; background: var(--bg-secondary); }\n");
        c.append(".detail-panel { padding: 20px 24px 24px; }\n");
        c.append(".detail-error { background: var(--accent-fail-bg); border: 1px solid var(--accent-fail-border); border-radius: var(--radius-sm); padding: 14px 18px; margin-bottom: 16px; }\n");
        c.append(".detail-error-title { font-size: 0.82rem; font-weight: 700; color: var(--accent-fail); margin-bottom: 8px; }\n");
        c.append(".error-pre { font-family: var(--font-mono); font-size: 0.78rem; color: var(--accent-fail); white-space: pre-wrap; word-break: break-all; line-height: 1.7; margin: 0; }\n");
        c.append(".detail-screenshot { margin-bottom: 16px; display: flex; align-items: center; gap: 10px; }\n");
        c.append(".screenshot-label { font-size: 0.82rem; font-weight: 600; color: var(--text-secondary); }\n");
        c.append(".screenshot-link { font-family: var(--font-mono); font-size: 0.78rem; color: var(--accent-blue); text-decoration: none; }\n");
        c.append(".screenshot-link:hover { text-decoration: underline; }\n\n");

        // --- STEPS TABLE ---
        c.append(".steps-container { background: var(--bg-primary); border: 1px solid var(--border-primary); border-radius: var(--radius-md); overflow: hidden; }\n");
        c.append(".steps-header { padding: 12px 18px; font-family: var(--font-heading); font-size: 0.82rem; font-weight: 700; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.06em; background: var(--bg-tertiary); border-bottom: 1px solid var(--border-primary); }\n");
        c.append(".steps-table { width: 100%; border-collapse: collapse; }\n");
        c.append(".steps-table th { padding: 10px 14px; font-size: 0.68rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.08em; color: var(--text-tertiary); text-align: left; background: var(--bg-secondary); border-bottom: 1px solid var(--border-subtle); }\n");
        c.append(".steps-table td { padding: 10px 14px; font-size: 0.84rem; border-bottom: 1px solid var(--border-subtle); vertical-align: top; }\n");
        c.append(".sth-idx, .std-idx { width: 40px; text-align: center; }\n");
        c.append(".sth-kw, .std-kw { width: 80px; }\n");
        c.append(".sth-dur, .std-dur { width: 90px; }\n");
        c.append(".std-dur code { font-family: var(--font-mono); font-size: 0.78rem; color: var(--text-secondary); }\n");
        c.append(".sth-status, .std-status { width: 80px; text-align: center; }\n\n");

        // Step row colouring
        c.append(".step-row-pass { border-left: 2px solid var(--accent-pass); }\n");
        c.append(".step-row-fail { border-left: 2px solid var(--accent-fail); background: var(--accent-fail-bg); }\n");
        c.append(".step-row-skip { border-left: 2px solid var(--accent-skip); opacity: 0.7; }\n\n");

        // Keyword badges
        c.append(".kw-badge { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 0.7rem; font-weight: 700; font-family: var(--font-mono); text-transform: uppercase; }\n");
        c.append(".kw-given { background: #1E3A5F; color: #60A5FA; }\n");
        c.append(".kw-when  { background: #3B2F1A; color: #FBBF24; }\n");
        c.append(".kw-then  { background: #1A3B2F; color: #34D399; }\n");
        c.append(".kw-and   { background: var(--bg-tertiary); color: var(--text-secondary); }\n");
        c.append(".kw-but   { background: #3B1A2F; color: #F472B6; }\n\n");

        // Step text + log panel
        c.append(".step-text { display: block; margin-bottom: 4px; }\n");
        c.append(".step-detail-toggle { background: none; border: none; color: var(--accent-blue); cursor: pointer; font-size: 0.72rem; font-weight: 600; padding: 2px 0; }\n");
        c.append(".step-detail-toggle:hover { text-decoration: underline; }\n");
        c.append(".step-log-panel { margin-top: 8px; padding: 12px 14px; background: var(--bg-secondary); border: 1px solid var(--border-subtle); border-radius: var(--radius-sm); }\n");
        c.append(".step-log-section { margin-bottom: 10px; }\n");
        c.append(".step-log-section:last-child { margin-bottom: 0; }\n");
        c.append(".step-log-label { font-size: 0.72rem; font-weight: 700; color: var(--text-tertiary); text-transform: uppercase; letter-spacing: 0.06em; margin-bottom: 4px; }\n");
        c.append(".step-log-pre { font-family: var(--font-mono); font-size: 0.76rem; color: var(--text-secondary); white-space: pre-wrap; word-break: break-word; margin: 0; line-height: 1.65; }\n");
        c.append(".step-error-pre { color: var(--accent-fail); }\n");
        c.append(".step-log-error { background: var(--accent-fail-bg); border-radius: var(--radius-sm); padding: 10px 12px; }\n\n");

        // --- FOOTER ---
        c.append(".report-footer { margin-top: 32px; padding: 16px 0; border-top: 1px solid var(--border-primary); display: flex; align-items: center; justify-content: center; gap: 8px; font-size: 0.78rem; color: var(--text-tertiary); }\n");
        c.append(".footer-dot { color: var(--border-primary); }\n\n");

        // --- ANIMATIONS ---
        c.append("@keyframes fadeIn { from { opacity: 0; transform: translateY(8px); } to { opacity: 1; transform: translateY(0); } }\n");
        c.append(".kpi-card, .chart-card, .meta-ribbon, .results-section { animation: fadeIn 0.4s ease-out both; }\n");
        c.append(".kpi-card:nth-child(2) { animation-delay: 0.05s; }\n");
        c.append(".kpi-card:nth-child(3) { animation-delay: 0.10s; }\n");
        c.append(".kpi-card:nth-child(4) { animation-delay: 0.15s; }\n");
        c.append(".kpi-card:nth-child(5) { animation-delay: 0.20s; }\n");
        c.append(".kpi-card:nth-child(6) { animation-delay: 0.25s; }\n");
        c.append(".kpi-card:nth-child(7) { animation-delay: 0.30s; }\n");
        c.append(".kpi-card:nth-child(8) { animation-delay: 0.35s; }\n\n");

        // --- SCROLLBAR ---
        c.append("::-webkit-scrollbar { width: 8px; height: 8px; }\n");
        c.append("::-webkit-scrollbar-track { background: var(--bg-primary); }\n");
        c.append("::-webkit-scrollbar-thumb { background: var(--border-primary); border-radius: 4px; }\n");
        c.append("::-webkit-scrollbar-thumb:hover { background: var(--text-tertiary); }\n\n");

        // --- PRINT ---
        c.append("@media print { body { background: #fff; color: #1a1a1a; } .filter-bar, .expand-btn, .step-detail-toggle { display: none; } .report-shell { max-width: 100%; } }\n");

        return c.toString();
    }

    // ===============================================================================================================================
    // JS BUILDER
    // ===============================================================================================================================
    private static String buildJS(int pPass, int pFail, int pError, int pPassSteps, int pFailSteps, int pSkipSteps, List<TestCaseResult> pResults)
    {
        StringBuilder j = new StringBuilder(8192);

        // --- Donut chart renderer (pure Canvas, zero dependencies) ---
        j.append("function drawDonut(canvasId, data, colors) {\n");
        j.append("  var c = document.getElementById(canvasId);\n");
        j.append("  if (!c) return;\n");
        j.append("  var dpr = window.devicePixelRatio || 1;\n");
        j.append("  var w = c.width, h = c.height;\n");
        j.append("  c.width = w * dpr; c.height = h * dpr;\n");
        j.append("  c.style.width = w + 'px'; c.style.height = h + 'px';\n");
        j.append("  var ctx = c.getContext('2d');\n");
        j.append("  ctx.scale(dpr, dpr);\n");
        j.append("  var total = data.reduce(function(a,b){return a+b;},0);\n");
        j.append("  if (total === 0) { ctx.fillStyle='#5A6478'; ctx.textAlign='center'; ctx.font='600 14px DM Sans'; ctx.fillText('No Data', w/2, h/2); return; }\n");
        j.append("  var cx=w/2, cy=h/2, outerR=Math.min(w,h)/2-10, innerR=outerR*0.62;\n");
        j.append("  var startAngle = -Math.PI/2;\n");
        j.append("  for (var i=0; i<data.length; i++) {\n");
        j.append("    if (data[i]===0) continue;\n");
        j.append("    var slice = (data[i]/total)*2*Math.PI;\n");
        j.append("    ctx.beginPath(); ctx.moveTo(cx,cy); ctx.arc(cx,cy,outerR,startAngle,startAngle+slice); ctx.closePath();\n");
        j.append("    ctx.fillStyle=colors[i]; ctx.fill();\n");
        j.append("    startAngle += slice;\n");
        j.append("  }\n");
        j.append("  ctx.beginPath(); ctx.arc(cx,cy,innerR,0,2*Math.PI); ctx.fillStyle='#161B26'; ctx.fill();\n");
        // Center text
        j.append("  ctx.fillStyle='#E8ECF4'; ctx.textAlign='center'; ctx.font='800 28px Plus Jakarta Sans'; ctx.fillText(total, cx, cy+4);\n");
        j.append("  ctx.fillStyle='#5A6478'; ctx.font='600 11px DM Sans'; ctx.fillText('TOTAL', cx, cy+20);\n");
        // Legend
        j.append("  var labels=['Pass','Fail','Error','Skip'];\n");
        j.append("  var lx = 10, ly = h - 20;\n");
        j.append("  ctx.font='600 10px DM Sans';\n");
        j.append("  for (var i=0; i<data.length; i++) {\n");
        j.append("    if (data[i]===0) continue;\n");
        j.append("    ctx.fillStyle=colors[i]; ctx.fillRect(lx, ly-7, 10, 10);\n");
        j.append("    ctx.fillStyle='#8B95A8'; ctx.textAlign='left'; ctx.fillText(labels[i]+' '+data[i], lx+14, ly+2);\n");
        j.append("    lx += ctx.measureText(labels[i]+' '+data[i]).width + 26;\n");
        j.append("  }\n");
        j.append("}\n\n");

        // --- Bar chart for timeline ---
        j.append("function drawTimeline(canvasId, ids, durations, statuses) {\n");
        j.append("  var c = document.getElementById(canvasId); if (!c) return;\n");
        j.append("  var dpr = window.devicePixelRatio || 1;\n");
        j.append("  var w = c.width, h = c.height;\n");
        j.append("  c.width = w * dpr; c.height = h * dpr;\n");
        j.append("  c.style.width = w + 'px'; c.style.height = h + 'px';\n");
        j.append("  var ctx = c.getContext('2d'); ctx.scale(dpr, dpr);\n");
        j.append("  if (ids.length === 0) return;\n");
        j.append("  var maxD = Math.max.apply(null, durations);\n");
        j.append("  if (maxD === 0) maxD = 1;\n");
        j.append("  var pad = {t:20,r:20,b:40,l:60};\n");
        j.append("  var pw = w-pad.l-pad.r, ph = h-pad.t-pad.b;\n");
        j.append("  var barW = Math.max(4, Math.min(32, (pw / ids.length) - 4));\n");
        j.append("  var gap = (pw - barW*ids.length) / (ids.length+1);\n");
        j.append("  var colorMap = {PASS:'#10B981', FAIL:'#EF4444', ERROR:'#F59E0B'};\n");
        // Grid lines
        j.append("  ctx.strokeStyle='#232A3B'; ctx.lineWidth=1;\n");
        j.append("  for (var g=0; g<=4; g++) { var gy=pad.t+ph-(ph*g/4); ctx.beginPath(); ctx.moveTo(pad.l,gy); ctx.lineTo(w-pad.r,gy); ctx.stroke(); }\n");
        // Y-axis labels
        j.append("  ctx.fillStyle='#5A6478'; ctx.font='500 10px JetBrains Mono'; ctx.textAlign='right';\n");
        j.append("  for (var g=0; g<=4; g++) { var gy=pad.t+ph-(ph*g/4); ctx.fillText(Math.round(maxD*g/4)+'ms', pad.l-8, gy+4); }\n");
        // Bars
        j.append("  for (var i=0; i<ids.length; i++) {\n");
        j.append("    var bx = pad.l + gap + i*(barW+gap);\n");
        j.append("    var bh = (durations[i]/maxD)*ph;\n");
        j.append("    var by = pad.t + ph - bh;\n");
        j.append("    ctx.fillStyle = colorMap[statuses[i]] || '#6366F1';\n");
        j.append("    ctx.beginPath();\n");
        j.append("    var r = Math.min(3, barW/2);\n");
        j.append("    ctx.moveTo(bx, by+bh); ctx.lineTo(bx, by+r); ctx.quadraticCurveTo(bx, by, bx+r, by); ctx.lineTo(bx+barW-r, by); ctx.quadraticCurveTo(bx+barW, by, bx+barW, by+r); ctx.lineTo(bx+barW, by+bh); ctx.closePath(); ctx.fill();\n");
        // X-axis label
        j.append("    ctx.save(); ctx.translate(bx+barW/2, pad.t+ph+8); ctx.rotate(Math.PI/4);\n");
        j.append("    ctx.fillStyle='#5A6478'; ctx.font='500 8px JetBrains Mono'; ctx.textAlign='left'; ctx.fillText(ids[i], 0, 0); ctx.restore();\n");
        j.append("  }\n");
        j.append("}\n\n");

        // --- Init charts ---
        j.append("drawDonut('tcChart',   [").append(pPass).append(",").append(pFail).append(",").append(pError).append("],   ['#10B981','#EF4444','#F59E0B']);\n");
        j.append("drawDonut('stepChart', [").append(pPassSteps).append(",").append(pFailSteps).append(",0,").append(pSkipSteps).append("], ['#10B981','#EF4444','#F59E0B','#6366F1']);\n");

        // Timeline data
        j.append("drawTimeline('timelineChart',\n  [");
        for (int i = 0; i < pResults.size(); i++)
        {
            if (i > 0) j.append(",");
            j.append("'").append(escJS(pResults.get(i).iTestCaseID)).append("'");
        }
        j.append("],\n  [");
        for (int i = 0; i < pResults.size(); i++)
        {
            if (i > 0) j.append(",");
            j.append(pResults.get(i).iDurationMs);
        }
        j.append("],\n  [");
        for (int i = 0; i < pResults.size(); i++)
        {
            if (i > 0) j.append(",");
            j.append("'").append(escJS(pResults.get(i).iStatus)).append("'");
        }
        j.append("]\n);\n\n");

        // --- Toggle detail row ---
        j.append("function toggleDetail(rowId) {\n");
        j.append("  var row = document.getElementById(rowId); if (!row) return;\n");
        j.append("  var btn = row.previousElementSibling.querySelector('.expand-btn');\n");
        j.append("  if (row.style.display === 'none') { row.style.display='table-row'; if(btn)btn.classList.add('open'); }\n");
        j.append("  else { row.style.display='none'; if(btn)btn.classList.remove('open'); }\n");
        j.append("}\n\n");

        // Also allow clicking the whole row
        j.append("document.querySelectorAll('.tc-row').forEach(function(r){\n");
        j.append("  r.addEventListener('click', function(e){\n");
        j.append("    if (e.target.tagName==='BUTTON') return;\n");
        j.append("    var btn = r.querySelector('.expand-btn'); if(btn) btn.click();\n");
        j.append("  });\n");
        j.append("});\n\n");

        // --- Toggle step log ---
        j.append("function toggleStepLog(id) {\n");
        j.append("  var el = document.getElementById(id); if (!el) return;\n");
        j.append("  el.style.display = el.style.display==='none' ? 'block' : 'none';\n");
        j.append("}\n\n");

        // --- Filter pills ---
        j.append("document.querySelectorAll('.filter-pill').forEach(function(btn){\n");
        j.append("  btn.addEventListener('click', function(){\n");
        j.append("    document.querySelectorAll('.filter-pill').forEach(function(b){b.classList.remove('active');});\n");
        j.append("    btn.classList.add('active');\n");
        j.append("    applyFilters();\n");
        j.append("  });\n");
        j.append("});\n\n");

        // --- Search box ---
        j.append("document.getElementById('searchBox').addEventListener('input', function(){ applyFilters(); });\n\n");

        // --- Combined filter logic ---
        j.append("function applyFilters() {\n");
        j.append("  var status = document.querySelector('.filter-pill.active').dataset.filter;\n");
        j.append("  var query = document.getElementById('searchBox').value.toLowerCase().trim();\n");
        j.append("  document.querySelectorAll('.tc-row').forEach(function(row){\n");
        j.append("    var rs = row.dataset.status;\n");
        j.append("    var search = row.dataset.search || '';\n");
        j.append("    var matchStatus = (status==='ALL' || rs===status);\n");
        j.append("    var matchSearch = (query==='' || search.indexOf(query)!==-1);\n");
        j.append("    row.style.display = (matchStatus && matchSearch) ? '' : 'none';\n");
        j.append("    var detail = row.nextElementSibling;\n");
        j.append("    if (detail && detail.classList.contains('detail-row')) { detail.style.display='none'; var b=row.querySelector('.expand-btn'); if(b)b.classList.remove('open'); }\n");
        j.append("  });\n");
        j.append("}\n");

        return j.toString();
    }

    // ===============================================================================================================================
    // HELPER METHODS
    // ===============================================================================================================================

    private static void appendMetaItem(StringBuilder pHtml, String pLabel, String pValue)
    {
        pHtml.append("  <div class=\"meta-item\">\n");
        pHtml.append("    <span class=\"meta-label\">").append(esc(pLabel)).append("</span>\n");
        pHtml.append("    <span class=\"meta-value\">").append(esc(pValue)).append("</span>\n");
        pHtml.append("  </div>\n");
    }

    private static void appendKpiCard(StringBuilder pHtml, String pLabel, String pValue, String pClass, String pSub)
    {
        pHtml.append("  <div class=\"kpi-card ").append(pClass).append("\">\n");
        pHtml.append("    <div class=\"kpi-label\">").append(esc(pLabel)).append("</div>\n");
        pHtml.append("    <div class=\"kpi-value\">").append(esc(pValue));
        if (pSub != null && !pSub.isEmpty())
        {
            pHtml.append("<span class=\"kpi-sub\">").append(esc(pSub)).append("</span>");
        }
        pHtml.append("</div>\n");
        pHtml.append("  </div>\n");
    }

    private static String renderTags(String pTags)
    {
        if (pTags == null || pTags.trim().isEmpty()) return "<span class=\"tag-chip\">—</span>";
        StringBuilder sb = new StringBuilder();
        for (String t : pTags.split("[,\\s]+"))
        {
            String tag = t.trim();
            if (!tag.isEmpty())
            {
                sb.append("<span class=\"tag-chip\">").append(esc(tag)).append("</span>");
            }
        }
        return sb.length() == 0 ? "<span class=\"tag-chip\">—</span>" : sb.toString();
    }

    /** HTML-escape */
    private static String esc(String pText)
    {
        if (pText == null) return "";
        return pText.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /** JS string escape */
    private static String escJS(String pText)
    {
        if (pText == null) return "";
        return pText.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private static void writeFile(String pPath, String pContent)
    {
        try
        {
            File iFile = new File(pPath);
            File iParent = iFile.getParentFile();
            if (iParent != null && !iParent.exists())
            {
                iParent.mkdirs();
            }
            try (BufferedWriter iWriter = new BufferedWriter(new FileWriter(iFile)))
            {
                iWriter.write(pContent);
            }
            log.info("[HtmlReportGenerator] Report written to : " + iFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            log.severe("[HtmlReportGenerator] Failed to write report : " + e.getMessage());
            throw new RuntimeException("Failed to write HTML report", e);
        }
    }
}