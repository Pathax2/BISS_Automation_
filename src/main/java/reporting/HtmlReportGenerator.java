// ===================================================================================================================================
// File          : HtmlReportGenerator.java
// Package       : reporting
// Description   : Generates a self-contained HTML execution dashboard for the BISS framework.
//
//                 Phase 2 additions over Phase 1:
//                   • Live search bar — filter table by TC ID or description as you type
//                   • Status filter buttons — All / Pass / Fail / Error
//                   • Sortable table columns — click any header to sort asc / desc
//                   • Screenshot lightbox — click thumbnail to open full-size modal overlay
//
//                 Retained from original:
//                   • Executive summary cards (Total, Pass, Fail, Error, Rate, Duration)
//                   • Doughnut chart + bar chart (Chart.js — requires CDN access on agent)
//                   • Bamboo build metadata block
//                   • Expandable failure detail cards with root cause + screenshot
//
//                 NOTE: Chart.js is loaded from cdn.jsdelivr.net.
//                       Bamboo agents with no outbound internet will render without charts
//                       but all other sections (cards, table, failures) still work fully.
//
// Called from   : TestRunner.java — after ReportManager.endSuite()
//                 Call site: HtmlReportGenerator.generate()  ← unchanged from Phase 1
//
// No changes needed in : TestRunner.java, ReportManager.java
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// Updated       : 26-03-2026 — Phase 2: filter bar, sort, lightbox
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

    private static final String iReportOutputDirectory =
            System.getProperty("html.report.path", "Test_Report/html/");

    // ***************************************************************************************************************************************************************************************
    // Function Name : generate
    // Description   : Main entry point. Builds and writes the full HTML report file to disk.
    //                 Called once from TestRunner after all test cases have completed.
    //                 Signature identical to Phase 1 — no changes needed in TestRunner.
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
    // Description   : Assembles the complete HTML string from all section builders
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
        iHtml.append(buildLightboxOverlay());
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


    // ===============================================================================================================================
    // HEAD
    // ===============================================================================================================================

    private static String buildHead()
    {
        return "<!DOCTYPE html>\n<html lang='en'>\n<head>\n"
                + "<meta charset='UTF-8'>\n"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>\n"
                + "<title>BISS Automation Execution Report</title>\n"
                + "<!-- Chart.js v4 — requires CDN access; all other sections work offline -->\n"
                + "<script src='https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js'></script>\n"
                + "</head>\n";
    }


    // ===============================================================================================================================
    // CSS STYLES
    // ===============================================================================================================================

    private static String buildStyles()
    {
        return "<style>\n"
                + "* { box-sizing: border-box; margin: 0; padding: 0; }\n"
                + "body { font-family: 'Segoe UI', Arial, sans-serif; background: #f0f2f5; color: #1a1a2e; }\n"

                // ── Header ───────────────────────────────────────────────────────────────────────
                + ".report-header { background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);"
                + " color: white; padding: 32px 40px; }\n"
                + ".report-header h1 { font-size: 28px; letter-spacing: 1px; }\n"
                + ".report-header .subtitle { font-size: 13px; color: #a0aec0; margin-top: 6px; }\n"
                + ".report-header .run-meta { display:flex; gap:32px; margin-top:18px; flex-wrap:wrap; }\n"
                + ".report-header .run-meta span { font-size:13px; color:#cbd5e0; }\n"
                + ".report-header .run-meta strong { color:#e2e8f0; }\n"

                // ── Bamboo meta block ─────────────────────────────────────────────────────────────
                + ".bamboo-block { background:#16213e; border-left:4px solid #e94560;"
                + " padding:16px 40px; display:flex; gap:40px; flex-wrap:wrap; }\n"
                + ".bamboo-block .bitem { font-size:12px; color:#a0aec0; }\n"
                + ".bamboo-block .bitem strong { display:block; color:#90cdf4; font-size:13px; margin-bottom:2px; }\n"

                // ── Main content ──────────────────────────────────────────────────────────────────
                + ".content { padding: 28px 40px; }\n"

                // ── Summary cards ─────────────────────────────────────────────────────────────────
                + ".summary-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));"
                + " gap: 20px; margin-bottom: 32px; }\n"
                + ".card { background: white; border-radius: 12px; padding: 24px 20px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); text-align: center; }\n"
                + ".card .card-value { font-size: 40px; font-weight: 700; margin-bottom: 6px; }\n"
                + ".card .card-label { font-size: 13px; color: #718096; text-transform: uppercase; letter-spacing: 1px; }\n"
                + ".card.total .card-value { color: #2d3748; }\n"
                + ".card.pass  .card-value { color: #38a169; }\n"
                + ".card.fail  .card-value { color: #e53e3e; }\n"
                + ".card.skip  .card-value { color: #d69e2e; }\n"
                + ".card.time  .card-value { font-size: 26px; color: #3182ce; }\n"
                + ".card.rate  .card-value { color: #38a169; }\n"

                // ── Charts ────────────────────────────────────────────────────────────────────────
                + ".charts-row { display: grid; grid-template-columns: 340px 1fr; gap: 24px; margin-bottom: 32px; }\n"
                + ".chart-card { background: white; border-radius: 12px; padding: 24px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); }\n"
                + ".chart-card h3 { font-size: 14px; color: #4a5568; margin-bottom: 16px;"
                + " text-transform: uppercase; letter-spacing: 1px; }\n"
                + ".doughnut-wrapper { max-width: 260px; margin: 0 auto; }\n"

                // ── Section headings ──────────────────────────────────────────────────────────────
                + ".section-title { font-size: 16px; font-weight: 600; color: #2d3748;"
                + " margin-bottom: 16px; padding-bottom: 8px; border-bottom: 2px solid #e2e8f0; }\n"

                // ── Table toolbar: filter bar + status buttons ─────────────────────────────────────
                + ".table-toolbar { display:flex; gap:12px; align-items:center; flex-wrap:wrap; margin-bottom:16px; }\n"
                + ".filter-input { flex:1; min-width:220px; padding:8px 14px; border:1px solid #e2e8f0;"
                + " border-radius:8px; font-size:13px; color:#2d3748; outline:none; }\n"
                + ".filter-input:focus { border-color:#3182ce; box-shadow:0 0 0 2px rgba(49,130,206,0.15); }\n"
                + ".status-btn { padding:6px 16px; border-radius:20px; border:1.5px solid #e2e8f0;"
                + " background:white; font-size:12px; font-weight:600; cursor:pointer;"
                + " transition:all 0.15s; letter-spacing:0.5px; }\n"
                + ".status-btn:hover { border-color:#3182ce; color:#3182ce; }\n"
                + ".status-btn.active-all   { background:#2d3748; color:white; border-color:#2d3748; }\n"
                + ".status-btn.active-pass  { background:#38a169; color:white; border-color:#38a169; }\n"
                + ".status-btn.active-fail  { background:#e53e3e; color:white; border-color:#e53e3e; }\n"
                + ".status-btn.active-error { background:#d69e2e; color:white; border-color:#d69e2e; }\n"
                + ".row-count { font-size:12px; color:#718096; margin-left:auto; white-space:nowrap; }\n"

                // ── Result table ──────────────────────────────────────────────────────────────────
                + ".table-card { background: white; border-radius: 12px; padding: 24px;"
                + " box-shadow: 0 2px 12px rgba(0,0,0,0.08); margin-bottom: 32px; overflow-x:auto; }\n"
                + "table { width: 100%; border-collapse: collapse; font-size: 13px; }\n"
                + "thead tr { background: #2d3748; color: white; }\n"
                + "thead th { padding: 12px 14px; text-align: left; font-weight: 500;"
                + " letter-spacing: 0.5px; cursor:pointer; user-select:none; white-space:nowrap; }\n"
                + "thead th:hover { background:#3d4f6b; }\n"
                + "thead th .sort-icon { margin-left:6px; opacity:0.5; font-size:10px; }\n"
                + "thead th.sort-asc .sort-icon::after  { content:'\\25B2'; opacity:1; }\n"
                + "thead th.sort-desc .sort-icon::after { content:'\\25BC'; opacity:1; }\n"
                + "thead th:not(.sort-asc):not(.sort-desc) .sort-icon::after { content:'\\25B2\\25BC'; }\n"
                + "tbody tr { border-bottom: 1px solid #edf2f7; transition: background 0.15s; }\n"
                + "tbody tr:hover { background: #f7fafc; }\n"
                + "tbody td { padding: 11px 14px; vertical-align: middle; }\n"
                + "tbody tr.hidden-row { display: none; }\n"
                + ".row-pass  { border-left: 3px solid #38a169; }\n"
                + ".row-fail  { border-left: 3px solid #e53e3e; background: #fff5f5; }\n"
                + ".row-error { border-left: 3px solid #d69e2e; background: #fffff0; }\n"

                // ── Badges ────────────────────────────────────────────────────────────────────────
                + ".badge { display:inline-block; padding:3px 10px; border-radius:12px; font-size:11px;"
                + " font-weight:700; letter-spacing:0.5px; }\n"
                + ".badge-pass  { background:#c6f6d5; color:#276749; }\n"
                + ".badge-fail  { background:#fed7d7; color:#9b2c2c; }\n"
                + ".badge-error { background:#fefcbf; color:#744210; }\n"

                // ── Tags ──────────────────────────────────────────────────────────────────────────
                + ".tag { display:inline-block; background:#ebf8ff; color:#2b6cb0;"
                + " padding:2px 8px; border-radius:8px; font-size:11px; margin:1px; }\n"

                // ── Screenshot thumbnail in table ─────────────────────────────────────────────────
                + ".thumb-btn { cursor:pointer; border:none; background:none; padding:0; }\n"
                + ".thumb-btn img { width:80px; height:52px; object-fit:cover; border-radius:6px;"
                + " border:1px solid #e2e8f0; transition:transform 0.15s, box-shadow 0.15s; }\n"
                + ".thumb-btn img:hover { transform:scale(1.06); box-shadow:0 4px 12px rgba(0,0,0,0.15); }\n"
                + ".no-shot { font-size:11px; color:#a0aec0; }\n"

                // ── Lightbox overlay ──────────────────────────────────────────────────────────────
                + "#lightbox { display:none; position:fixed; inset:0; background:rgba(0,0,0,0.82);"
                + " z-index:9999; align-items:center; justify-content:center; }\n"
                + "#lightbox.open { display:flex; }\n"
                + "#lightbox-inner { position:relative; max-width:92vw; max-height:90vh; }\n"
                + "#lightbox-img { max-width:100%; max-height:90vh; border-radius:8px;"
                + " box-shadow:0 8px 40px rgba(0,0,0,0.5); display:block; }\n"
                + "#lightbox-caption { text-align:center; color:#e2e8f0; font-size:13px; margin-top:10px; }\n"
                + "#lightbox-close { position:absolute; top:-14px; right:-14px; width:32px; height:32px;"
                + " border-radius:50%; background:#e53e3e; color:white; border:none; font-size:18px;"
                + " cursor:pointer; display:flex; align-items:center; justify-content:center;"
                + " box-shadow:0 2px 8px rgba(0,0,0,0.3); }\n"

                // ── Failure details ───────────────────────────────────────────────────────────────
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
                + " border:1px solid #e2e8f0; box-shadow:0 2px 8px rgba(0,0,0,0.1);"
                + " cursor:pointer; transition:opacity 0.15s; }\n"
                + ".screenshot-embed img:hover { opacity:0.88; }\n"

                // ── Footer ────────────────────────────────────────────────────────────────────────
                + ".report-footer { text-align:center; padding:24px; font-size:12px; color:#a0aec0; }\n"

                // ── Responsive ────────────────────────────────────────────────────────────────────
                + "@media(max-width:900px) { .charts-row { grid-template-columns:1fr; }"
                + " .content { padding:20px; } .table-toolbar { flex-direction:column; align-items:stretch; } }\n"
                + "</style>\n";
    }


    // ===============================================================================================================================
    // LIGHTBOX OVERLAY  (hidden div — made visible by JS when thumbnail is clicked)
    // ===============================================================================================================================

    private static String buildLightboxOverlay()
    {
        return "<div id='lightbox' onclick='closeLightbox(event)'>\n"
                + "  <div id='lightbox-inner'>\n"
                + "    <button id='lightbox-close' onclick='document.getElementById(\"lightbox\").classList.remove(\"open\")'>&times;</button>\n"
                + "    <img id='lightbox-img' src='' alt='Screenshot'/>\n"
                + "    <div id='lightbox-caption'></div>\n"
                + "  </div>\n"
                + "</div>\n";
    }


    // ===============================================================================================================================
    // HEADER BAND
    // ===============================================================================================================================

    private static String buildHeader()
    {
        return "<div class='report-header'>\n"
                + "  <h1>&#x1F4CA; BISS Automation Execution Report</h1>\n"
                + "  <div class='subtitle'>BDD Cucumber &bull; Selenium WebDriver &bull; Quality Assurance Dashboard</div>\n"
                + "  <div class='run-meta'>\n"
                + "    <span><strong>Environment&nbsp;</strong>" + esc(ReportManager.getEnvironment()) + "</span>\n"
                + "    <span><strong>Browser&nbsp;</strong>" + esc(ReportManager.getBrowser()) + "</span>\n"
                + "    <span><strong>Started&nbsp;</strong>" + esc(ReportManager.getSuiteStartTime()) + "</span>\n"
                + "    <span><strong>Finished&nbsp;</strong>" + esc(ReportManager.getSuiteEndTime()) + "</span>\n"
                + "    <span><strong>Total Duration&nbsp;</strong>" + ReportManager.getSuiteDurationFormatted() + "</span>\n"
                + "  </div>\n"
                + "</div>\n";
    }


    // ===============================================================================================================================
    // BAMBOO META BLOCK
    // ===============================================================================================================================

    private static String buildBambooMetaBlock()
    {
        return "<div class='bamboo-block'>\n"
                + "  <div class='bitem'><strong>&#x1F528; Bamboo Build No.</strong>" + esc(ReportManager.getBuildNumber()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F5FA; Plan Key</strong>" + esc(ReportManager.getBuildPlanKey()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F916; Agent</strong>" + esc(ReportManager.getBambooAgentName()) + "</div>\n"
                + "  <div class='bitem'><strong>&#x1F464; Executed By</strong>" + esc(ReportManager.getExecutedBy()) + "</div>\n"
                + "</div>\n";
    }


    // ===============================================================================================================================
    // SUMMARY CARDS
    // ===============================================================================================================================

    private static String buildSummaryCards()
    {
        int    iTotal = ReportManager.getTotalCount();
        int    iPass  = ReportManager.getPassCount();
        int    iFail  = ReportManager.getFailCount();
        int    iError = ReportManager.getErrorCount();
        String iRate  = ReportManager.getPassPercent() + "%";

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


    // ===============================================================================================================================
    // CHARTS (unchanged from Phase 1 — data injected as window globals for the script section)
    // ===============================================================================================================================

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

                + "  <div class='chart-card'>\n"
                + "    <h3>&#x1F4CA; Pass / Fail Breakdown</h3>\n"
                + "    <div class='doughnut-wrapper'><canvas id='doughnutChart'></canvas></div>\n"
                + "    <div style='text-align:center;margin-top:12px;font-size:12px;color:#718096;'>"
                + "<span style='color:#38a169;font-weight:700;'>" + ReportManager.getPassPercent() + "% Pass</span>"
                + "&nbsp;|&nbsp;"
                + "<span style='color:#e53e3e;font-weight:700;'>" + ReportManager.getFailPercent() + "% Fail</span>"
                + "    </div>\n"
                + "  </div>\n"

                + "  <div class='chart-card'>\n"
                + "    <h3>&#x23F1; Execution Duration per Test Case (seconds)</h3>\n"
                + "    <div><canvas id='barChart'></canvas></div>\n"
                + "  </div>\n"

                + "</div>\n"

                // Inject data for buildScripts()
                + "<script>\n"
                + "  window._chartLabels    = [" + iLabels   + "];\n"
                + "  window._chartDurations = [" + iDuration + "];\n"
                + "  window._chartColors    = [" + iColors   + "];\n"
                + "  window._passCount      = " + ReportManager.getPassCount()  + ";\n"
                + "  window._failCount      = " + ReportManager.getFailCount()  + ";\n"
                + "  window._errorCount     = " + ReportManager.getErrorCount() + ";\n"
                + "</script>\n";
    }


    // ===============================================================================================================================
    // RESULT TABLE — with filter toolbar (search + status buttons) and sortable column headers
    // ===============================================================================================================================

    private static String buildResultTable(List<TestCaseResult> pResults)
    {
        StringBuilder iSb = new StringBuilder();

        iSb.append("<div class='table-card'>\n")
                .append("<div class='section-title'>&#x1F4CB; Full Execution Results</div>\n")

                // ── FILTER TOOLBAR ───────────────────────────────────────────────────────────────────
                .append("<div class='table-toolbar'>\n")
                .append("  <input class='filter-input' id='searchInput' type='text'"
                        + " placeholder='&#128269; Search by Test Case ID or description...'"
                        + " oninput='applyFilters()'/>\n")
                .append("  <button class='status-btn active-all' id='btn-ALL'  onclick='setStatusFilter(\"ALL\")'  >All</button>\n")
                .append("  <button class='status-btn'            id='btn-PASS' onclick='setStatusFilter(\"PASS\")' >Pass</button>\n")
                .append("  <button class='status-btn'            id='btn-FAIL' onclick='setStatusFilter(\"FAIL\")' >Fail</button>\n")
                .append("  <button class='status-btn'            id='btn-ERROR'onclick='setStatusFilter(\"ERROR\")'>Error</button>\n")
                .append("  <span class='row-count' id='rowCount'>" + pResults.size() + " of " + pResults.size() + " rows</span>\n")
                .append("</div>\n")

                // ── TABLE ────────────────────────────────────────────────────────────────────────────
                .append("<table id='resultsTable'>\n")
                .append("<thead><tr>\n")
                .append(th("#",           "col-num",     false))
                .append(th("Test Case",   "col-tc",      true))
                .append(th("Description", "col-desc",    true))
                .append(th("Status",      "col-status",  true))
                .append(th("Tags",        "col-tags",    false))
                .append(th("Environment", "col-env",     false))
                .append(th("Browser",     "col-browser", false))
                .append(th("Duration",    "col-dur",     true))
                .append(th("Timestamp",   "col-ts",      true))
                .append(th("Screenshot",  "col-shot",    false))
                .append("</tr></thead>\n<tbody id='resultsBody'>\n");

        int iIdx = 1;
        for (TestCaseResult r : pResults)
        {
            // Thumbnail — base64 encoded inline if screenshot exists
            String iThumbnailHtml = buildThumbnail(r);

            iSb.append("<tr class='").append(r.getRowClass())
                    .append("' data-status='").append(esc(r.iStatus.toUpperCase()))
                    .append("' data-tc='").append(esc(r.iTestCaseID.toLowerCase()))
                    .append("' data-desc='").append(esc(r.iDescription.toLowerCase()))
                    .append("'>\n")
                    .append("  <td style='color:#718096;'>").append(iIdx++).append("</td>\n")
                    .append("  <td><strong>").append(esc(r.iTestCaseID)).append("</strong></td>\n")
                    .append("  <td>").append(esc(r.iDescription)).append("</td>\n")
                    .append("  <td><span class='badge ").append(r.getStatusBadgeClass()).append("'>")
                    .append(r.iStatus).append("</span></td>\n")
                    .append("  <td>").append(buildTagHtml(r.iTags)).append("</td>\n")
                    .append("  <td>").append(esc(r.iEnvironment)).append("</td>\n")
                    .append("  <td>").append(esc(r.iBrowser)).append("</td>\n")
                    .append("  <td data-ms='").append(r.iDurationMs).append("'>")
                    .append(r.getDurationFormatted()).append("</td>\n")
                    .append("  <td style='font-size:11px;color:#718096;'>").append(esc(r.iTimestamp)).append("</td>\n")
                    .append("  <td>").append(iThumbnailHtml).append("</td>\n")
                    .append("</tr>\n");
        }

        iSb.append("</tbody>\n</table>\n</div>\n");
        return iSb.toString();
    }

    // Sortable column header helper
    private static String th(String pLabel, String pId, boolean pSortable)
    {
        if (!pSortable)
        {
            return "<th id='" + pId + "'>" + pLabel + "</th>\n";
        }
        return "<th id='" + pId + "' onclick='sortTable(this)' title='Click to sort'>"
                + pLabel + "<span class='sort-icon'></span></th>\n";
    }

    // Thumbnail for the table screenshot column
    private static String buildThumbnail(TestCaseResult r)
    {
        if (r.iScreenshotPath == null || r.iScreenshotPath.isEmpty())
        {
            return "<span class='no-shot'>—</span>";
        }

        String iBase64 = encodeScreenshot(r.iScreenshotPath);
        if (iBase64 == null)
        {
            return "<span class='no-shot' title='" + esc(r.iScreenshotPath) + "'>not found</span>";
        }

        String iDataUri = "data:image/png;base64," + iBase64;
        return "<button class='thumb-btn'"
                + " onclick='openLightbox(\"" + iDataUri + "\",\"" + esc(r.iTestCaseID) + "\")'>"
                + "<img src='" + iDataUri + "' alt='Screenshot for " + esc(r.iTestCaseID) + "'/>"
                + "</button>";
    }


    // ===============================================================================================================================
    // FAILURE DETAILS SECTION — expandable cards, error message + full-size screenshot
    // ===============================================================================================================================

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
                iSb.append("    <div class='error-box' style='color:#a0aec0;'>"
                        + "No error message captured. Check framework logs for stack trace details.</div>\n");
            }

            // Full-size screenshot — click to open lightbox
            if (r.iScreenshotPath != null && !r.iScreenshotPath.isEmpty())
            {
                String iBase64 = encodeScreenshot(r.iScreenshotPath);
                if (iBase64 != null)
                {
                    String iDataUri = "data:image/png;base64," + iBase64;
                    iSb.append("    <div class='error-label'>Screenshot at Point of Failure"
                                    + " <span style='font-size:10px;color:#3182ce;'>(click to enlarge)</span></div>\n")
                            .append("    <div class='screenshot-embed'>\n")
                            .append("      <img src='").append(iDataUri).append("'"
                                    + " alt='Screenshot for ").append(esc(r.iTestCaseID)).append("'"
                                    + " onclick='openLightbox(\"").append(iDataUri).append("\",\"").append(esc(r.iTestCaseID)).append("\")'"
                                    + " title='Click to open full-size'/>\n")
                            .append("    </div>\n");
                }
                else
                {
                    iSb.append("    <div class='error-label'>Screenshot</div>\n")
                            .append("    <div style='font-size:12px;color:#e53e3e;'>"
                                    + "Screenshot file not found at: " + esc(r.iScreenshotPath) + "</div>\n");
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


    // ===============================================================================================================================
    // FOOTER
    // ===============================================================================================================================

    private static String buildFooter()
    {
        return "<div class='report-footer'>"
                + "Generated by BISS Automation Framework &bull; "
                + "Bamboo Build: " + esc(ReportManager.getBuildNumber()) + " &bull; "
                + ReportManager.getSuiteEndTime()
                + "</div>\n"
                + "</div>\n"; // close .content
    }


    // ===============================================================================================================================
    // JAVASCRIPT — Chart.js init + filter + sort + lightbox
    // ===============================================================================================================================

    private static String buildScripts(List<TestCaseResult> pResults)
    {
        return "<script>\n"

                // ── Chart.js: Doughnut ────────────────────────────────────────────────────────────
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

                // ── Chart.js: Bar ─────────────────────────────────────────────────────────────────
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

                // ── Filter state ──────────────────────────────────────────────────────────────────
                + "var _statusFilter = 'ALL';\n\n"

                + "function setStatusFilter(status) {\n"
                + "  _statusFilter = status;\n"
                + "  document.querySelectorAll('.status-btn').forEach(function(b) {\n"
                + "    b.className = 'status-btn';\n"
                + "  });\n"
                + "  var btn = document.getElementById('btn-' + status);\n"
                + "  if (btn) btn.classList.add('active-' + status.toLowerCase());\n"
                + "  applyFilters();\n"
                + "}\n\n"

                // ── Live search + status filter applied together ───────────────────────────────────
                + "function applyFilters() {\n"
                + "  var query = document.getElementById('searchInput').value.toLowerCase().trim();\n"
                + "  var rows  = document.querySelectorAll('#resultsBody tr');\n"
                + "  var visible = 0;\n"
                + "  rows.forEach(function(row) {\n"
                + "    var tc     = row.getAttribute('data-tc')   || '';\n"
                + "    var desc   = row.getAttribute('data-desc') || '';\n"
                + "    var status = row.getAttribute('data-status') || '';\n"
                + "    var matchSearch = query === '' || tc.indexOf(query) >= 0 || desc.indexOf(query) >= 0;\n"
                + "    var matchStatus = _statusFilter === 'ALL' || status === _statusFilter;\n"
                + "    if (matchSearch && matchStatus) {\n"
                + "      row.classList.remove('hidden-row');\n"
                + "      visible++;\n"
                + "    } else {\n"
                + "      row.classList.add('hidden-row');\n"
                + "    }\n"
                + "  });\n"
                + "  document.getElementById('rowCount').textContent = visible + ' of ' + rows.length + ' rows';\n"
                + "}\n\n"

                // ── Table sort ────────────────────────────────────────────────────────────────────
                + "var _sortCol = null;\n"
                + "var _sortAsc = true;\n\n"
                + "function sortTable(th) {\n"
                + "  var colIdx = th.cellIndex;\n"
                + "  if (_sortCol === th) {\n"
                + "    _sortAsc = !_sortAsc;\n"
                + "  } else {\n"
                + "    if (_sortCol) { _sortCol.classList.remove('sort-asc','sort-desc'); }\n"
                + "    _sortCol = th;\n"
                + "    _sortAsc = true;\n"
                + "  }\n"
                + "  th.classList.toggle('sort-asc',  _sortAsc);\n"
                + "  th.classList.toggle('sort-desc', !_sortAsc);\n"
                + "  var tbody = document.getElementById('resultsBody');\n"
                + "  var rows  = Array.from(tbody.querySelectorAll('tr'));\n"
                + "  rows.sort(function(a, b) {\n"
                + "    var aCell = a.cells[colIdx];\n"
                + "    var bCell = b.cells[colIdx];\n"
                + "    if (!aCell || !bCell) return 0;\n"
                + "    // Duration column: sort by numeric ms value stored in data-ms attribute\n"
                + "    var aMs = aCell.getAttribute('data-ms');\n"
                + "    if (aMs !== null) {\n"
                + "      return _sortAsc ? Number(aMs) - Number(bCell.getAttribute('data-ms'))\n"
                + "                     : Number(bCell.getAttribute('data-ms')) - Number(aMs);\n"
                + "    }\n"
                + "    var aVal = aCell.innerText.trim().toLowerCase();\n"
                + "    var bVal = bCell.innerText.trim().toLowerCase();\n"
                + "    return _sortAsc ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);\n"
                + "  });\n"
                + "  rows.forEach(function(r) { tbody.appendChild(r); });\n"
                + "}\n\n"

                // ── Lightbox ──────────────────────────────────────────────────────────────────────
                + "function openLightbox(src, caption) {\n"
                + "  document.getElementById('lightbox-img').src = src;\n"
                + "  document.getElementById('lightbox-caption').textContent = caption;\n"
                + "  document.getElementById('lightbox').classList.add('open');\n"
                + "}\n"
                + "function closeLightbox(e) {\n"
                + "  if (e.target === document.getElementById('lightbox')) {\n"
                + "    document.getElementById('lightbox').classList.remove('open');\n"
                + "  }\n"
                + "}\n"
                + "document.addEventListener('keydown', function(e) {\n"
                + "  if (e.key === 'Escape') { document.getElementById('lightbox').classList.remove('open'); }\n"
                + "});\n\n"

                // ── Failure card toggle ───────────────────────────────────────────────────────────
                + "function toggleFailure(id) {\n"
                + "  var body = document.getElementById(id);\n"
                + "  var icon = document.getElementById('icon_' + id);\n"
                + "  if (body.classList.contains('open')) {\n"
                + "    body.classList.remove('open'); icon.innerHTML = '&#x25BC;';\n"
                + "  } else {\n"
                + "    body.classList.add('open');    icon.innerHTML = '&#x25B2;';\n"
                + "  }\n"
                + "}\n\n"

                // ── Auto-expand all failure cards on load ─────────────────────────────────────────
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


    // ===============================================================================================================================
    // PRIVATE HELPERS
    // ===============================================================================================================================

    private static String buildTagHtml(String pTags)
    {
        if (pTags == null || pTags.trim().isEmpty())
        {
            return "<span style='color:#a0aec0;font-size:11px;'>—</span>";
        }
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
            log.warning("[HtmlReportGenerator] Cannot encode screenshot : " + pPath + " | " + iException.getMessage());
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