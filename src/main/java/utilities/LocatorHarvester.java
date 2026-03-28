// ===================================================================================================================================
// File          : LocatorHarvester.java
// Package       : utilities
// Description   : Automatic DOM scanner, XPath generator, locator validator, and
//                 smart-merge ObjectRepository writer.
//
//                 Key behaviours:
//
//                 1. SMART MERGE — reads the existing ExtractedLocators.properties on
//                    every run. New keys are appended. Changed XPaths are updated with a
//                    [UPDATED] comment. Exact duplicates are silently skipped.
//                    Manually curated locators already in the file are never removed.
//
//                 2. LOCATOR VALIDATION — after generating each XPath, the harvester
//                    attempts driver.findElement() on it and calls highlightElement() to
//                    visually confirm the locator resolves to a real, visible element.
//                    Only VALIDATED locators are written to the active section.
//                    Unresolvable XPaths are written to a separate UNVERIFIED block at
//                    the bottom for manual review — NOT read by ObjReader.
//
//                 3. FULL DOM COVERAGE — scans three passes:
//                      Pass A : Main document (HTML + Angular Material)
//                      Pass B : CDK Overlay Container (mat-option, autocomplete portals)
//                      Pass C : iFrames (each frame scanned, keys prefixed with frame name)
//
//                 4. CONFIDENCE SCORING — each locator rated HIGH / MEDIUM / LOW
//                    based on which XPath strategy was used:
//                      HIGH   : @id, @formcontrolname, @data-testid, @aria-label
//                      MEDIUM : @placeholder, normalize-space() text, Angular host scope
//                      LOW    : contains(@class), positional fallback
//
//                 XPath Priority Hierarchy:
//                   1. @id                    — direct stable ID
//                   2. @data-testid / @data-cy / @data-qa
//                   3. @formcontrolname       — Angular reactive form
//                   4. @aria-label            — accessibility attribute
//                   5. @placeholder           — input hint text
//                   6. normalize-space() text — visible button/label text
//                   7. contains(@class) + text
//                   8. Angular host scope     — biss-*/lib-* ancestor
//                   9. Positional fallback
//
// Output:
//   src/test/resources/Object_Repository/ExtractedLocators.properties
//   (smart-merged on every run — safe to call repeatedly)
//
// Usage:
//   LocatorHarvester.harvest(CommonFunctions.getDriver(), "LandDetails");
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 28-03-2026
// ===================================================================================================================================

package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.UUID;

public class LocatorHarvester
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // Configuration
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Logger log = Logger.getLogger(LocatorHarvester.class.getName());

    private static final String OUTPUT_FILE =
            System.getProperty("harvest.output.file",
                    "src/test/resources/Object_Repository/ExtractedLocators.properties");

    private static final int MAX_ELEMENTS   = 600;
    private static final int MAX_TEXT_LEN   = 50;
    private static final int MAX_CLASS_LEN  = 45;
    private static final int HIGHLIGHT_MS   = 300;

    // Watch mode — harvestOnNavigation() polls at this interval (ms) for up to this many minutes
    private static final long WATCH_POLL_MS      = Long.parseLong(
            System.getProperty("harvest.watch.poll.ms",      "2000"));
    private static final long WATCH_MAX_MINUTES  = Long.parseLong(
            System.getProperty("harvest.watch.max.minutes",  "30"));

    private static final String HIGH   = "HIGH";
    private static final String MEDIUM = "MEDIUM";
    private static final String LOW    = "LOW";

    private static final String T_TEXTBOX  = "TEXTBOX";
    private static final String T_BUTTON   = "BUTTON";
    private static final String T_LINK     = "LINK";
    private static final String T_DROPDOWN = "DROPDOWN";
    private static final String T_CHECKBOX = "CHECKBOX";
    private static final String T_RADIO    = "RADIO";
    private static final String T_TOGGLE   = "TOGGLE";
    private static final String T_LABEL    = "LABEL";
    private static final String T_ERROR    = "ERROR";
    private static final String T_TABLE    = "TABLE";
    private static final String T_DIALOG   = "DIALOG";
    private static final String T_UTILITY  = "UTILITY";


    // ===============================================================================================================================
    // PUBLIC ENTRY POINT
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : harvest (no-arg overload — recommended)
    // Description   : Derives the page name automatically from the current browser state
    //                 (page title first, URL path as fallback) then delegates to harvest().
    //
    //                 This is the preferred call signature — the driver already knows what
    //                 page it is on so there is no reason to pass a name manually.
    //
    //                 Page name derivation priority:
    //                   1. document.title  — cleaned of special chars and camelCased
    //                      e.g. "My Application — Land Details" → "MyApplicationLandDetails"
    //                   2. URL path        — same logic as derivePageName(url)
    //                      e.g. "/#/application/land-details"   → "ApplicationLandDetails"
    //                   3. "Page"          — fallback if both are blank
    //
    // Usage:
    //   LocatorHarvester.harvest(CommonFunctions.getDriver());
    // ***************************************************************************************************************************************************************************************
    public static void harvest(WebDriver pDriver)
    {
        String iPageName = resolvePageName(pDriver);
        log.info("[HARVESTER] Auto-detected page name: " + iPageName);
        harvest(pDriver, iPageName);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : harvest (explicit page name — kept for override when needed)
    // Description   : Same as harvest(driver) but allows the caller to supply a custom
    //                 page name instead of the auto-derived one.  Use this only when the
    //                 page title / URL does not produce a readable name.
    //
    // Usage:
    //   LocatorHarvester.harvest(CommonFunctions.getDriver(), "LandDetails");
    // ***************************************************************************************************************************************************************************************
    public static void harvest(WebDriver pDriver, String pPageName)
    {
        log.info("[HARVESTER] ═══════════════════════════════════════════════════════");
        log.info("[HARVESTER] Page  : " + pPageName);
        log.info("[HARVESTER] File  : " + OUTPUT_FILE);

        try
        {
            Map<String, String>      iExisting = readExistingFile();
            List<Map<String,String>> iRaw      = new ArrayList<>();

            // Pass A — main DOM
            List<Map<String,String>> iMain = scanPass(pDriver, false, "");
            iRaw.addAll(iMain);
            log.info("[HARVESTER] Pass A (main DOM)   : " + iMain.size());

            // Pass B — CDK overlay (mat-options, autocomplete)
            List<Map<String,String>> iOverlay = scanPass(pDriver, true, "");
            iRaw.addAll(iOverlay);
            log.info("[HARVESTER] Pass B (CDK overlay): " + iOverlay.size());

            // Pass C — iFrames
            for (String iFrameId : getFrameIds(pDriver))
            {
                try
                {
                    pDriver.switchTo().frame(iFrameId);
                    List<Map<String,String>> iFrame = scanPass(pDriver, false, iFrameId);
                    iRaw.addAll(iFrame);
                    log.info("[HARVESTER] Pass C (frame=" + iFrameId + "): " + iFrame.size());
                    pDriver.switchTo().defaultContent();
                }
                catch (Exception ex)
                {
                    pDriver.switchTo().defaultContent();
                }
            }

            // Pass D — Shadow DOM (pierce all shadow roots found in document)
            List<Map<String,String>> iShadow = scanShadowDom(pDriver);
            iRaw.addAll(iShadow);
            log.info("[HARVESTER] Pass D (shadow DOM)  : " + iShadow.size());

            log.info("[HARVESTER] Total raw: " + iRaw.size());

            // Classify + deduplicate
            Set<String> iSeenXPaths = new LinkedHashSet<>();
            Set<String> iSeenKeys   = new LinkedHashSet<>(iExisting.keySet());
            List<HarvestedElement> iHarvested = new ArrayList<>();

            for (Map<String,String> iR : iRaw)
            {
                HarvestedElement iEl = classify(iR, pPageName, iSeenKeys);
                if (iEl == null || iSeenXPaths.contains(iEl.iXPath)) { continue; }
                iSeenXPaths.add(iEl.iXPath);
                iHarvested.add(iEl);
            }

            log.info("[HARVESTER] Unique candidates: " + iHarvested.size());

            // Validate by highlighting
            List<HarvestedElement> iValidated  = new ArrayList<>();
            List<HarvestedElement> iUnverified = new ArrayList<>();

            for (HarvestedElement iEl : iHarvested)
            {
                if (validateByHighlight(pDriver, iEl.iXPath, iEl.iKey))
                    iValidated.add(iEl);
                else
                    iUnverified.add(iEl);
            }

            log.info("[HARVESTER] Validated  : " + iValidated.size());
            log.info("[HARVESTER] Unverified : " + iUnverified.size());

            // Detect duplicate XPaths — find validated locators that resolve to the same DOM node
            List<DuplicatePair> iDuplicates = detectDuplicateXPaths(pDriver, iValidated);
            if (!iDuplicates.isEmpty())
            {
                log.warning("[HARVESTER] Duplicate XPath pairs found: " + iDuplicates.size()
                        + " (both keys resolve to the same element — review recommended)");
                for (DuplicatePair dp : iDuplicates)
                    log.warning("[HARVESTER]   DUPLICATE: '" + dp.iKeyA + "' and '" + dp.iKeyB
                            + "' both resolve to the same element");
            }

            MergeResult iResult = smartMerge(
                    pDriver, pPageName, iValidated, iUnverified, iExisting, iDuplicates);

            log.info("[HARVESTER] Added=" + iResult.iAdded
                    + "  Updated=" + iResult.iUpdated
                    + "  Skipped=" + iResult.iSkipped
                    + "  Unverified=" + iResult.iUnverified);
            log.info("[HARVESTER] ═══════════════════════════════════════════════════════");
        }
        catch (Exception ex)
        {
            log.severe("[HARVESTER] Failed: " + ex.getMessage());
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : harvestOnNavigation
    // Description   : WATCH MODE — runs in a background thread and automatically calls
    //                 harvest() every time the browser navigates to a new URL.
    //
    //                 How it works:
    //                   1. Polls driver.getCurrentUrl() every WATCH_POLL_MS milliseconds.
    //                   2. When the URL changes, waits 1 second for Angular to settle,
    //                      then calls harvest() with a page name derived from the URL path.
    //                   3. Continues watching until stopWatch() is called or the timeout
    //                      (WATCH_MAX_MINUTES) is reached.
    //
    //                 The background thread is a daemon thread so it never prevents JVM exit.
    //
    //                 Usage (typically called once in @BeforeAll or a manual test run):
    //                   Thread iWatcher = LocatorHarvester.harvestOnNavigation(driver);
    //                   // ... run your test suite / manual navigation ...
    //                   LocatorHarvester.stopWatch();
    //
    // Parameters    : pDriver (WebDriver) - active driver to poll
    // Returns       : Thread — the background watcher thread (daemon, already started)
    // ***************************************************************************************************************************************************************************************
    private static volatile boolean iWatching = false;

    public static Thread harvestOnNavigation(WebDriver pDriver)
    {
        iWatching = true;

        Thread iWatcher = new Thread(() ->
        {
            String iLastUrl       = "";
            long   iStartMs       = System.currentTimeMillis();
            long   iMaxMs         = WATCH_MAX_MINUTES * 60 * 1000L;

            log.info("[HARVESTER-WATCH] Started. Polling every "
                    + WATCH_POLL_MS + "ms for up to " + WATCH_MAX_MINUTES + " minutes.");

            while (iWatching && (System.currentTimeMillis() - iStartMs) < iMaxMs)
            {
                try
                {
                    String iCurrentUrl = pDriver.getCurrentUrl();

                    if (!iCurrentUrl.equals(iLastUrl) && !iCurrentUrl.isEmpty()
                            && !iCurrentUrl.equals("about:blank")
                            && !iCurrentUrl.equals("data:,"))
                    {
                        // URL changed — give Angular 1 second to finish rendering
                        Thread.sleep(1000);

                        log.info("[HARVESTER-WATCH] Navigation detected → " + iCurrentUrl);

                        // Let harvest() auto-detect the page name from title/URL
                        harvest(pDriver);
                        iLastUrl = iCurrentUrl;
                    }

                    Thread.sleep(WATCH_POLL_MS);
                }
                catch (InterruptedException iIe)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
                catch (Exception iEx)
                {
                    // Driver may be temporarily unavailable during navigation — just wait
                    log.fine("[HARVESTER-WATCH] Poll error (transient): " + iEx.getMessage());
                    try { Thread.sleep(WATCH_POLL_MS); } catch (InterruptedException ignored) {}
                }
            }

            log.info("[HARVESTER-WATCH] Stopped.");
        });

        iWatcher.setDaemon(true);
        iWatcher.setName("LocatorHarvester-Watch");
        iWatcher.start();
        return iWatcher;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : stopWatch
    // Description   : Signals the background watch thread to stop after its current poll cycle.
    //                 Safe to call multiple times.
    // ***************************************************************************************************************************************************************************************
    public static void stopWatch()
    {
        iWatching = false;
        log.info("[HARVESTER-WATCH] Stop signal sent.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : derivePageName
    // Description   : Converts a URL into a CamelCase page name for use as a key prefix.
    //
    //                 Examples:
    //                   https://biss.gov.ie/#/application/land-details  → LandDetails
    //                   https://biss.gov.ie/#/clients/V2l0T.../details  → ClientsDetails
    //                   https://biss.gov.ie/#/                          → Home
    // ***************************************************************************************************************************************************************************************
    // ***************************************************************************************************************************************************************************************
    // Function Name : resolvePageName
    // Description   : Derives a clean CamelCase page name from the live browser state.
    //                 Tries document.title first — it is usually more human-readable than
    //                 the URL path.  Falls back to URL path if the title is blank or generic.
    //
    //                 Cleaning rules applied to the title:
    //                   - Strips everything after " — ", " | ", " - " (common title separators)
    //                     to remove site-name suffixes like "— BISS" or "| Gov.ie"
    //                   - Removes special characters
    //                   - Splits on spaces and capitalises each word (CamelCase)
    //                   - Caps total length at 40 chars
    //
    //                 Examples:
    //                   Title  "My Application — Land Details"  → "MyApplicationLandDetails"
    //                   Title  "Farmer Dashboard | BISS"        → "FarmerDashboard"
    //                   Title  "Edit Parcel"                    → "EditParcel"
    //                   URL    "/#/application/land-details"    → "ApplicationLandDetails"
    // ***************************************************************************************************************************************************************************************
    private static String resolvePageName(WebDriver pDriver)
    {
        // ── Attempt 1 : document.title ────────────────────────────────────────────────
        try
        {
            String iTitle = pDriver.getTitle();

            if (iTitle != null && !iTitle.trim().isEmpty())
            {
                // Strip everything after common title separators (site name suffix)
                iTitle = iTitle.replaceAll("\\s+(—|-|\\|)\\s+.*$", "").trim();

                if (!iTitle.isEmpty() && !iTitle.equalsIgnoreCase("about:blank"))
                {
                    // CamelCase: split on space/special chars, capitalise each word
                    String[] iWords = iTitle.replaceAll("[^a-zA-Z0-9\\s]", " ")
                            .trim().split("\\s+");
                    StringBuilder iName = new StringBuilder();
                    for (String iWord : iWords)
                    {
                        if (iWord.isEmpty()) continue;
                        iName.append(Character.toUpperCase(iWord.charAt(0)))
                                .append(iWord.substring(1).toLowerCase());
                        if (iName.length() >= 40) break;
                    }
                    if (iName.length() > 0)
                    {
                        return iName.toString();
                    }
                }
            }
        }
        catch (Exception ignored) {}

        // ── Attempt 2 : URL path ──────────────────────────────────────────────────────
        try
        {
            String iUrl = pDriver.getCurrentUrl();
            if (iUrl != null && !iUrl.isEmpty())
            {
                String iDerived = derivePageName(iUrl);
                if (!iDerived.equals("Page") && !iDerived.equals("Home"))
                    return iDerived;
            }
        }
        catch (Exception ignored) {}

        return "Page";
    }

    private static String derivePageName(String pUrl)
    {
        try
        {
            // Strip scheme + host + query, keep path only
            String iPath = pUrl
                    .replaceAll("^https?://[^/#?]+", "")  // remove host
                    .replaceAll("[?#].*$", "")              // remove query/fragment before #
                    .replace("#/", "/")                     // handle hash-routes
                    .replaceAll("^/+", "")                 // strip leading slash
                    .trim();

            if (iPath.isEmpty()) { return "Home"; }

            // Split on / and -, capitalise each token, skip base64-ish segments
            String[] iSegments = iPath.split("[/\\-]+");
            StringBuilder iName = new StringBuilder();
            for (String iSeg : iSegments)
            {
                if (iSeg.isEmpty()) continue;
                // Skip long base64 tokens (encoded IDs like V2l0T0FuU25k...)
                if (iSeg.length() > 20 && iSeg.matches("[A-Za-z0-9+/=]+")) continue;
                // Skip pure numeric segments
                if (iSeg.matches("\\d+")) continue;
                iName.append(cap(iSeg.toLowerCase()));
            }
            return iName.length() == 0 ? "Page" : iName.toString();
        }
        catch (Exception ignored)
        {
            return "Page";
        }
    }


    // ===============================================================================================================================
    // SECTION 1 : DOM SCANNER
    // ===============================================================================================================================

    @SuppressWarnings("unchecked")
    private static List<Map<String,String>> scanPass(WebDriver pDriver,
                                                     boolean pOverlayOnly,
                                                     String pFramePrefix)
    {
        String iRoot = pOverlayOnly
                ? "document.querySelector('.cdk-overlay-container') || document.body"
                : "document.body";

        String iScript =
                "(function(){" +
                        "var MAX=" + MAX_ELEMENTS + ";" +
                        "var root=" + iRoot + ";" +
                        "if(!root) return [];" +
                        "var results=[];" +
                        "var selectors=[" +
                        // ── Standard HTML ────────────────────────────────────────────────────────
                        "'input:not([type=\"hidden\"])'," +
                        "'textarea','select','button','a[href]'," +
                        // ── Angular Material interactive ─────────────────────────────────────────
                        "'mat-select','mat-checkbox','mat-radio-button','mat-slide-toggle'," +
                        "'mat-button-toggle','mat-option','mat-icon-button','mat-menu-item'," +
                        "'mat-list-item'," +
                        // ── Angular Material structural ──────────────────────────────────────────
                        "'mat-error','mat-dialog-container','mat-step-header','mat-tab-link'," +
                        "'mat-card','mat-form-field','mat-paginator','mat-sort-header'," +
                        "'mat-toolbar','mat-sidenav','mat-tab','mat-nav-list'," +
                        // ── ARIA roles ───────────────────────────────────────────────────────────
                        "'[role=\"switch\"]','[role=\"tab\"]','[role=\"listbox\"]'," +
                        "'[role=\"combobox\"]','[role=\"dialog\"]','[role=\"alertdialog\"]'," +
                        "'[role=\"option\"]','[role=\"menuitem\"]','[role=\"alert\"]'," +
                        // ── Angular form attributes ──────────────────────────────────────────────
                        "'[formcontrolname]','[formgroupname]','[formarrayname]'," +
                        // ── Test IDs ─────────────────────────────────────────────────────────────
                        "'[data-testid]','[data-cy]','[data-qa]'," +
                        // ── Accessibility ────────────────────────────────────────────────────────
                        "'[aria-label]','[aria-haspopup]'," +
                        // ── Table structure ──────────────────────────────────────────────────────
                        "'table','tbody','th'," +
                        // ── Headings ─────────────────────────────────────────────────────────────
                        "'h1','h2','h3','h4'," +
                        // ── Class patterns ───────────────────────────────────────────────────────
                        "'[class*=\"btn-add\"]','[class*=\"dialog\"]','[class*=\"modal\"]'," +
                        "'[class*=\"error\"]','[class*=\"warning\"]','[class*=\"header\"]'," +
                        "'[class*=\"spinner\"]','[class*=\"progress\"]','[class*=\"mat-column\"]'" +
                        "];" +
                        "var seen=new Set();" +
                        "selectors.forEach(function(sel){" +
                        "  var nodes;try{nodes=root.querySelectorAll(sel);}catch(e){return;}" +
                        "  for(var i=0;i<nodes.length&&results.length<MAX;i++){" +
                        "    var el=nodes[i];" +
                        "    if(seen.has(el))continue; seen.add(el);" +
                        "    try{var st=window.getComputedStyle(el);" +
                        "      if(st.display==='none'||st.visibility==='hidden'||parseFloat(st.opacity)===0)continue;" +
                        "    }catch(e){continue;}" +
                        "    var o={};" +
                        "    o.tag=el.tagName.toLowerCase();" +
                        "    o.id=el.id||'';" +
                        "    o.name=el.name||'';" +
                        "    o.type=el.type||'';" +
                        "    o.role=el.getAttribute('role')||'';" +
                        "    o.cls=typeof el.className==='string'?el.className.trim():'';" +
                        "    o.ariaLabel=el.getAttribute('aria-label')||'';" +
                        "    o.ariaExpanded=el.getAttribute('aria-expanded')||'';" +
                        "    o.ariaHaspopup=el.getAttribute('aria-haspopup')||'';" +
                        "    o.ariaDisabled=el.getAttribute('aria-disabled')||'';" +
                        "    o.placeholder=el.getAttribute('placeholder')||'';" +
                        "    o.formcontrol=el.getAttribute('formcontrolname')||'';" +
                        "    o.formgroup=el.getAttribute('formgroupname')||'';" +
                        "    o.datatestid=el.getAttribute('data-testid')||'';" +
                        "    o.datacy=el.getAttribute('data-cy')||'';" +
                        "    o.dataqa=el.getAttribute('data-qa')||'';" +
                        "    o.forAttr=el.getAttribute('for')||'';" +
                        "    o.href=el.tagName==='A'?(el.getAttribute('href')||''):'';" +
                        "    o.required=el.hasAttribute('required')?'true':'';" +
                        "    o.disabled=el.disabled||el.getAttribute('aria-disabled')==='true'?'true':'';" +
                        "    o.matDialogClose=el.hasAttribute('mat-dialog-close')?'true':'';" +
                        "    o.framePrefix='" + escJs(pFramePrefix) + "';" +
                        "    var txt=(el.innerText||el.textContent||'').trim().replace(/\\s+/g,' ');" +
                        "    o.text=txt.length>100?txt.substring(0,100):txt;" +
                        "    var p=el.parentElement;" +
                        "    o.parentTag=p?p.tagName.toLowerCase():'';" +
                        "    o.parentCls=p&&typeof p.className==='string'?p.className.trim():'';" +
                        "    o.parentId=p?(p.id||''):'';" +
                        // Angular biss-*/lib-* host scan (8 levels)
                        "    o.angularHost='';" +
                        "    var cur=el.parentElement;" +
                        "    for(var d=0;d<8&&cur;d++){" +
                        "      var t=cur.tagName.toLowerCase();" +
                        "      if(t.startsWith('biss-')||t.startsWith('lib-')){o.angularHost=t;break;}" +
                        "      cur=cur.parentElement;}" +
                        // Dialog ancestor scan (10 levels)
                        "    o.dialogAncestor='';" +
                        "    var dc=el.parentElement;" +
                        "    for(var dd=0;dd<10&&dc;dd++){" +
                        "      if(dc.tagName.toLowerCase()==='mat-dialog-container'){o.dialogAncestor='mat-dialog-container';break;}" +
                        "      dc=dc.parentElement;}" +
                        "    results.push(o);" +
                        "  }" +
                        "});" +
                        "return results;" +
                        "})();";

        Object iResult;
        try { iResult = ((JavascriptExecutor) pDriver).executeScript(iScript); }
        catch (Exception ex) { log.warning("[HARVESTER] JS scan error: " + ex.getMessage()); return Collections.emptyList(); }

        List<Map<String,String>> iList = new ArrayList<>();
        if (iResult instanceof List)
        {
            for (Object item : (List<?>) iResult)
            {
                if (item instanceof Map)
                {
                    Map<String,String> m = new LinkedHashMap<>();
                    for (Map.Entry<?,?> e : ((Map<?,?>)item).entrySet())
                        m.put(String.valueOf(e.getKey()), e.getValue()==null?"":String.valueOf(e.getValue()));
                    iList.add(m);
                }
            }
        }
        return iList;
    }

    private static List<String> getFrameIds(WebDriver pDriver)
    {
        List<String> ids = new ArrayList<>();
        try
        {
            Object r = ((JavascriptExecutor) pDriver).executeScript(
                    "var f=document.querySelectorAll('iframe,frame'),ids=[];" +
                            "for(var i=0;i<f.length;i++) ids.push(f[i].id||f[i].name||String(i));" +
                            "return ids;");
            if (r instanceof List) for (Object o : (List<?>)r) if (o!=null&&!o.toString().isEmpty()) ids.add(o.toString());
        } catch (Exception ignored) {}
        return ids;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : scanShadowDom
    // Description   : Pass D — pierces all Shadow DOM roots found anywhere in the document
    //                 and extracts interactive elements from within them.
    //
    //                 Shadow DOM roots cannot be queried with standard document.querySelectorAll()
    //                 because the browser intentionally isolates them. This method injects JS
    //                 that recursively walks all elements, enters every .shadowRoot it finds,
    //                 and runs the same selector list inside each shadow tree.
    //
    //                 Each extracted element's attribute map is identical in structure to the
    //                 maps produced by scanPass() so classify() handles them without changes.
    //
    //                 Elements found inside shadow roots get a 'shadowHost' attribute set to
    //                 the tag name of the host element, which buildXPath() uses as a scoping
    //                 comment in the generated XPath (standard XPath cannot pierce shadow DOM,
    //                 so the generated locator targets the element's own stable attributes —
    //                 @id, @aria-label, @formcontrolname — which remain unique across the page).
    //
    // Returns       : List of attribute maps, one per shadow-DOM element found
    // ***************************************************************************************************************************************************************************************
    @SuppressWarnings("unchecked")
    private static List<Map<String,String>> scanShadowDom(WebDriver pDriver)
    {
        String iScript =
                "(function(){" +
                        "  var MAX=" + MAX_ELEMENTS + ";" +
                        "  var results=[];" +
                        "  var seen=new Set();" +
                        "  var selectors=[" +
                        "    'input:not([type=\"hidden\"])','textarea','select','button','a[href]'," +
                        "    '[aria-label]','[formcontrolname]','[data-testid]','[role]'" +
                        "  ];" +

                        // Recursive shadow root walker
                        "  function walkShadow(root, hostTag) {" +
                        "    selectors.forEach(function(sel){" +
                        "      var nodes;try{nodes=root.querySelectorAll(sel);}catch(e){return;}" +
                        "      for(var i=0;i<nodes.length&&results.length<MAX;i++){" +
                        "        var el=nodes[i];" +
                        "        if(seen.has(el))continue; seen.add(el);" +
                        "        try{var st=window.getComputedStyle(el);" +
                        "          if(st.display==='none'||st.visibility==='hidden')continue;" +
                        "        }catch(e){continue;}" +
                        "        var o={};" +
                        "        o.tag=el.tagName.toLowerCase();" +
                        "        o.id=el.id||'';" +
                        "        o.type=el.type||'';" +
                        "        o.role=el.getAttribute('role')||'';" +
                        "        o.cls=typeof el.className==='string'?el.className.trim():'';" +
                        "        o.ariaLabel=el.getAttribute('aria-label')||'';" +
                        "        o.placeholder=el.getAttribute('placeholder')||'';" +
                        "        o.formcontrol=el.getAttribute('formcontrolname')||'';" +
                        "        o.datatestid=el.getAttribute('data-testid')||'';" +
                        "        o.datacy=el.getAttribute('data-cy')||'';" +
                        "        o.dataqa=el.getAttribute('data-qa')||'';" +
                        "        o.href=el.tagName==='A'?(el.getAttribute('href')||''):'';" +
                        "        o.ariaExpanded=el.getAttribute('aria-expanded')||'';" +
                        "        o.ariaHaspopup=el.getAttribute('aria-haspopup')||'';" +
                        "        o.disabled=el.disabled?'true':'';" +
                        "        o.required=el.hasAttribute('required')?'true':'';" +
                        "        o.matDialogClose=el.hasAttribute('mat-dialog-close')?'true':'';" +
                        "        o.forAttr=el.getAttribute('for')||'';" +
                        "        o.name=el.name||'';" +
                        "        o.framePrefix='';" +
                        "        o.angularHost='';" +
                        "        o.dialogAncestor='';" +
                        "        o.formgroup=el.getAttribute('formgroupname')||'';" +
                        // Mark as shadow DOM element with host context
                        "        o.shadowHost=hostTag||'';" +
                        "        var txt=(el.innerText||el.textContent||'').trim().replace(/\\s+/g,' ');" +
                        "        o.text=txt.length>100?txt.substring(0,100):txt;" +
                        "        var p=el.parentElement;" +
                        "        o.parentTag=p?p.tagName.toLowerCase():'';" +
                        "        o.parentCls=p&&typeof p.className==='string'?p.className.trim():'';" +
                        "        o.parentId=p?(p.id||''):'';" +
                        "        results.push(o);" +
                        "      }" +
                        "    });" +

                        // Recurse into nested shadow roots
                        "    var allEls=root.querySelectorAll('*');" +
                        "    for(var j=0;j<allEls.length;j++){" +
                        "      if(allEls[j].shadowRoot){" +
                        "        walkShadow(allEls[j].shadowRoot, allEls[j].tagName.toLowerCase());" +
                        "      }" +
                        "    }" +
                        "  }" +

                        // Start walk from all top-level shadow roots in the document
                        "  var topEls=document.querySelectorAll('*');" +
                        "  for(var k=0;k<topEls.length;k++){" +
                        "    if(topEls[k].shadowRoot){" +
                        "      walkShadow(topEls[k].shadowRoot, topEls[k].tagName.toLowerCase());" +
                        "    }" +
                        "  }" +

                        "  return results;" +
                        "})();";

        Object iResult;
        try { iResult = ((JavascriptExecutor) pDriver).executeScript(iScript); }
        catch (Exception ex)
        {
            log.warning("[HARVESTER] Shadow DOM scan error: " + ex.getMessage());
            return Collections.emptyList();
        }

        List<Map<String,String>> iList = new ArrayList<>();
        if (iResult instanceof List)
        {
            for (Object item : (List<?>) iResult)
            {
                if (item instanceof Map)
                {
                    Map<String,String> m = new LinkedHashMap<>();
                    for (Map.Entry<?,?> e : ((Map<?,?>)item).entrySet())
                        m.put(String.valueOf(e.getKey()),
                                e.getValue()==null ? "" : String.valueOf(e.getValue()));
                    // Tag shadow elements so the comment includes the host context
                    m.put("isShadow", "true");
                    iList.add(m);
                }
            }
        }

        if (!iList.isEmpty())
            log.info("[HARVESTER] Shadow DOM elements found: " + iList.size());

        return iList;
    }


    // ===============================================================================================================================
    // SECTION 2 : CLASSIFIER
    // ===============================================================================================================================

    private static HarvestedElement classify(Map<String,String> pRaw,
                                             String pPageName,
                                             Set<String> pSeenKeys)
    {
        String iTag         = g(pRaw,"tag");
        String iId          = g(pRaw,"id");
        String iType        = g(pRaw,"type");
        String iRole        = g(pRaw,"role");
        String iClass       = g(pRaw,"cls");
        String iAriaLabel   = g(pRaw,"ariaLabel");
        String iPlaceholder = g(pRaw,"placeholder");
        String iFormCtrl    = g(pRaw,"formcontrol");
        String iTestId      = g(pRaw,"datatestid");
        String iDataCy      = g(pRaw,"datacy");
        String iDataQa      = g(pRaw,"dataqa");
        String iText        = g(pRaw,"text");
        String iHref        = g(pRaw,"href");
        String iParentTag   = g(pRaw,"parentTag");
        String iParentCls   = g(pRaw,"parentCls");
        String iAngularHost = g(pRaw,"angularHost");
        String iDialogAnc   = g(pRaw,"dialogAncestor");
        String iFramePfx    = g(pRaw,"framePrefix");
        String iMDClose     = g(pRaw,"matDialogClose");

        if (iTag.isEmpty()) return null;
        if (iClass.contains("cdk-visually-hidden"))  return null;
        if (iClass.contains("mat-ripple") && iAriaLabel.isEmpty()
                && iFormCtrl.isEmpty() && iId.isEmpty())  return null;
        if (iTag.equals("mat-option") && iText.isEmpty()) return null;

        String iType_ = detectType(iTag, iType, iRole, iClass, iParentTag, iMDClose);
        if (iType_ == null) return null;

        XPathResult iXPR = buildXPath(iTag, iId, iType, iRole, iClass, iAriaLabel,
                iPlaceholder, iFormCtrl, iTestId, iDataCy, iDataQa, iText, iHref,
                iAngularHost, iDialogAnc, iMDClose, iType_);
        if (iXPR == null || iXPR.iXPath.isEmpty()) return null;

        String iPagePfx = iFramePfx.isEmpty() ? pPageName : pPageName + cap(iFramePfx);
        String iKey = makeKey(iPagePfx, iType_, iTag, iId, iFormCtrl,
                iAriaLabel, iPlaceholder, iText, pSeenKeys);
        if (iKey == null) return null;

        String iComment = buildComment(iTag, iAriaLabel, iText, iFormCtrl,
                iId, iPlaceholder, iType_, iXPR.iConfidence);
        return new HarvestedElement(iKey, iXPR.iXPath, iType_, iComment, iXPR.iConfidence);
    }

    private static String detectType(String tag, String type, String role,
                                     String cls, String parentTag, String mdClose)
    {
        switch (tag)
        {
            case "input":
                if ("checkbox".equals(type))                       return T_CHECKBOX;
                if ("radio".equals(type))                          return T_RADIO;
                if ("submit".equals(type)||"button".equals(type)||"reset".equals(type)) return T_BUTTON;
                if ("hidden".equals(type))                         return null;
                return T_TEXTBOX;
            case "textarea":                                       return T_TEXTBOX;
            case "select": case "mat-select":                      return T_DROPDOWN;
            case "mat-option":
                return cls.contains("mat-mdc-option") ? T_DROPDOWN : null;
            case "mat-checkbox":                                   return T_CHECKBOX;
            case "mat-radio-button":                               return T_RADIO;
            case "mat-slide-toggle":                               return T_TOGGLE;
            case "button": case "mat-button-toggle":
            case "mat-icon-button": case "mat-menu-item":          return T_BUTTON;
            case "a": case "mat-tab-link":                         return T_LINK;
            case "mat-error":                                      return T_ERROR;
            case "mat-dialog-container":                           return T_DIALOG;
            case "table": case "tbody": case "th":                 return T_TABLE;
            case "h1": case "h2": case "h3": case "h4":
            case "mat-step-header": case "mat-toolbar":            return T_LABEL;
            case "mat-card":
                return cls.contains("mat-mdc-card-content") ? null : T_LABEL;
            case "mat-paginator":                                   return T_UTILITY;
            case "mat-list-item":                                   return T_LINK;
        }
        if ("switch".equals(role))                                 return T_TOGGLE;
        if ("combobox".equals(role)||"listbox".equals(role))       return T_DROPDOWN;
        if ("dialog".equals(role)||"alertdialog".equals(role))     return T_DIALOG;
        if ("tab".equals(role))                                    return T_LABEL;
        if ("option".equals(role))                                 return T_DROPDOWN;
        if ("menuitem".equals(role))                               return T_BUTTON;
        if (cls.contains("error-panel")||cls.contains("kc-feedback")) return T_ERROR;
        if (cls.contains("mdc-data-table")||cls.contains("mat-mdc-table")) return T_TABLE;
        if (cls.contains("mdc-circular-progress")||cls.contains("spinner")) return T_UTILITY;
        if (cls.contains("btn")||cls.contains("button"))           return T_BUTTON;
        if ("true".equals(mdClose))                                return T_BUTTON;
        return null;
    }

    private static XPathResult buildXPath(String tag, String id, String type,
                                          String role, String cls, String ariaLabel,
                                          String placeholder, String formCtrl,
                                          String testId, String dataCy, String dataQa,
                                          String text, String href,
                                          String angHost, String dialogAnc,
                                          String mdClose, String elType)
    {
        String t = trunc(text.trim(), MAX_TEXT_LEN);

        // 1. @id (non-auto)
        if (!id.isEmpty() && !isAutoId(id))
            return xp("//" + tag + "[@id='" + esc(id) + "']", HIGH);

        // 2. @data-testid / @data-cy / @data-qa
        if (!testId.isEmpty()) return xp("//*[@data-testid='" + esc(testId) + "']", HIGH);
        if (!dataCy.isEmpty()) return xp("//*[@data-cy='" + esc(dataCy) + "']", HIGH);
        if (!dataQa.isEmpty()) return xp("//*[@data-qa='" + esc(dataQa) + "']", HIGH);

        // 3. @formcontrolname
        if (!formCtrl.isEmpty())
        {
            if (!angHost.isEmpty())
                return xp("//" + angHost + "//" + tag + "[@formcontrolname='" + esc(formCtrl) + "']", HIGH);
            if (!dialogAnc.isEmpty())
                return xp("//mat-dialog-container//" + tag + "[@formcontrolname='" + esc(formCtrl) + "']", HIGH);
            return xp("//" + tag + "[@formcontrolname='" + esc(formCtrl) + "']", HIGH);
        }

        // 4. @aria-label
        if (!ariaLabel.isEmpty() && ariaLabel.length() <= MAX_TEXT_LEN)
            return xp("//" + tag + "[@aria-label='" + esc(ariaLabel) + "']", HIGH);

        // 5. @placeholder
        if (!placeholder.isEmpty() && (tag.equals("input") || tag.equals("textarea")))
            return xp("//" + tag + "[@placeholder='" + esc(placeholder) + "']", HIGH);

        // 6. mat-dialog-close
        if ("true".equals(mdClose))
        {
            if (!t.isEmpty())
                return xp("//button[@mat-dialog-close][.//mat-icon[normalize-space()='" + esc(t) + "']]", MEDIUM);
            return xp("//button[@mat-dialog-close]", MEDIUM);
        }

        // 7. Text-based strategies
        if (!t.isEmpty())
        {
            switch (tag)
            {
                case "button":
                {
                    String mc = mc(cls);
                    if (!mc.isEmpty())
                        return xp("//button[contains(@class,'" + mc + "') and .//span[normalize-space()='" + esc(t) + "']]", MEDIUM);
                    if (!dialogAnc.isEmpty())
                        return xp("//mat-dialog-container//button[.//span[normalize-space()='" + esc(t) + "']]", MEDIUM);
                    return xp("//button[.//span[normalize-space()='" + esc(t) + "']]", MEDIUM);
                }
                case "a":
                    if (!href.isEmpty() && !href.startsWith("javascript") && !href.startsWith("#"))
                        return xp("//a[normalize-space()='" + esc(t) + "']", MEDIUM);
                    if (!href.isEmpty())
                        return xp("//a[@href='" + esc(href) + "']", MEDIUM);
                    return xp("//a[contains(normalize-space(),'" + esc(trunc(t,30)) + "')]", MEDIUM);
                case "h1": case "h2": case "h3": case "h4":
            {
                String mc = mc(cls);
                if (!mc.isEmpty())
                    return xp("//" + tag + "[contains(@class,'" + mc + "') and normalize-space()='" + esc(t) + "']", MEDIUM);
                return xp("//" + tag + "[normalize-space()='" + esc(t) + "']", MEDIUM);
            }
                case "mat-checkbox":
                    return xp("//mat-checkbox[.//label[normalize-space()='" + esc(t) + "']]", MEDIUM);
                case "mat-radio-button":
                    return xp("//mat-radio-button//label[normalize-space()='" + esc(t) + "']", MEDIUM);
                case "mat-error":
                    return xp("//mat-error[contains(text(),'" + esc(trunc(t,40)) + "')]", MEDIUM);
                case "mat-select":
                    if (t.toLowerCase().startsWith("select"))
                        return xp("//mat-select[.//span[contains(text(),'" + esc(t) + "')]]", MEDIUM);
                    break;
                case "mat-option":
                    if (cls.contains("optionListItem"))
                        return xp("//mat-option[contains(@class,'optionListItem')]//span[normalize-space()='" + esc(t) + "']", MEDIUM);
                    return xp("//mat-option//span[normalize-space()='" + esc(t) + "']", MEDIUM);
                case "mat-card":
                    return xp("//mat-card[.//span[normalize-space()='" + esc(trunc(t,30)) + "']]", MEDIUM);
                case "mat-step-header":
                    return xp("//mat-step-header[.//div[normalize-space()='" + esc(t) + "']]", MEDIUM);
                case "mat-button-toggle":
                    return xp("//span[@class='mat-button-toggle-label-content' and contains(.,'" + esc(t) + "')]", MEDIUM);
                case "mat-tab-link":
                    return xp("//a[contains(@class,'mat-mdc-tab-link') and normalize-space()='" + esc(t) + "']", MEDIUM);
                case "mat-list-item":
                    return xp("//mat-selection-list//span[contains(normalize-space(),'" + esc(trunc(t,30)) + "')]", MEDIUM);
                case "span":
                    if (cls.contains("left-menu-link"))
                        return xp("//span[@class='mdc-button__label']//span[contains(@class,'left-menu-link') and text()='" + esc(t) + "']", MEDIUM);
                    break;
            }
        }

        // 8. contains(@class) + text
        String mc = mc(cls);
        if (!mc.isEmpty() && mc.length() <= MAX_CLASS_LEN)
        {
            if (!t.isEmpty())
                return xp("//" + tag + "[contains(@class,'" + mc + "') and contains(normalize-space(),'" + esc(trunc(t,30)) + "')]", LOW);
            return xp("//" + tag + "[contains(@class,'" + mc + "')]", LOW);
        }

        // 9. Angular host + text
        if (!angHost.isEmpty() && !t.isEmpty())
            return xp("//" + angHost + "//" + tag + "[contains(normalize-space(),'" + esc(trunc(t,30)) + "')]", LOW);

        // Fallback: auto-generated Angular ID
        if (!id.isEmpty())
            return xp("//" + tag + "[@id='" + esc(id) + "']", LOW);

        return null;
    }


    // ===============================================================================================================================
    // SECTION 3 : VALIDATOR
    // ===============================================================================================================================

    private static boolean validateByHighlight(WebDriver pDriver, String pXPath, String pKey)
    {
        try
        {
            WebElement iEl = pDriver.findElement(By.xpath(pXPath));
            if (!iEl.isDisplayed()) return false;

            JavascriptExecutor js = (JavascriptExecutor) pDriver;
            String iOrig = iEl.getAttribute("style");
            if (iOrig == null) iOrig = "";

            // Green border = valid locator (distinguishable from red used in test steps)
            js.executeScript(
                    "arguments[0].setAttribute('style',arguments[1]);", iEl,
                    "border:3px solid #00AA00 !important;background:rgba(0,170,0,0.05) !important;"
            );

            try { Thread.sleep(HIGHLIGHT_MS); } catch (InterruptedException ignored) {}

            js.executeScript("arguments[0].setAttribute('style',arguments[1]);", iEl, iOrig);
            return true;
        }
        catch (NoSuchElementException ex)   { return false; }
        catch (Exception ex)                { return false; }
    }


    // ===============================================================================================================================
    // SECTION 4 : SMART MERGE
    // ===============================================================================================================================

    private static MergeResult smartMerge(WebDriver pDriver, String pPageName,
                                          List<HarvestedElement> pValidated,
                                          List<HarvestedElement> pUnverified,
                                          Map<String,String> pExisting,
                                          List<DuplicatePair> pDuplicates) throws Exception
    {
        MergeResult iRes = new MergeResult();

        File iFile = new File(OUTPUT_FILE);
        if (iFile.getParentFile() != null) iFile.getParentFile().mkdirs();

        String iContent = iFile.exists()
                ? new String(Files.readAllBytes(iFile.toPath()), StandardCharsets.UTF_8)
                : buildHeader(pDriver, pPageName);

        String iNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        // Categorise
        List<HarvestedElement> iToAdd    = new ArrayList<>();
        List<HarvestedElement> iToUpdate = new ArrayList<>();

        for (HarvestedElement e : pValidated)
        {
            if      (!pExisting.containsKey(e.iKey))                     { iToAdd.add(e);    iRes.iAdded++;   }
            else if (!pExisting.get(e.iKey).equals(e.iXPath))            { iToUpdate.add(e); iRes.iUpdated++; }
            else                                                          { iRes.iSkipped++;                   }
        }
        iRes.iUnverified = pUnverified.size();

        // Apply inline UPDATEs
        for (HarvestedElement e : iToUpdate)
        {
            String iOld   = pExisting.get(e.iKey);
            String iNew   = "# [UPDATED " + iNow + "] confidence=" + e.iConfidence + "\n"
                    + "# Old XPath: " + iOld + "\n"
                    + pad(e.iKey) + " = " + e.iXPath;
            iContent = iContent.replaceFirst("(?m)^" + escRe(e.iKey) + "\\s*=.*$",
                    iNew.replace("\\","\\\\").replace("$","\\$"));
        }

        // Strip old unverified block
        if (iContent.contains("# ===[ UNVERIFIED LOCATORS ]==="))
            iContent = iContent.substring(0, iContent.indexOf("# ===[ UNVERIFIED LOCATORS ]===")).stripTrailing();

        // Append new entries grouped by type
        if (!iToAdd.isEmpty())
        {
            Map<String, List<HarvestedElement>> iGrouped = new LinkedHashMap<>();
            for (String t : typeOrder()) iGrouped.put(t, new ArrayList<>());
            for (HarvestedElement e : iToAdd) iGrouped.computeIfAbsent(e.iType, k->new ArrayList<>()).add(e);

            StringBuilder sb = new StringBuilder("\n\n");
            sb.append("# ===================================================================================================================================\n");
            sb.append("# NEWLY HARVESTED — ").append(pPageName.toUpperCase())
                    .append("  |  ").append(iNow).append("\n");
            sb.append("# ===================================================================================================================================\n");

            Map<String,String> sn = sectionNames();
            for (Map.Entry<String, List<HarvestedElement>> e : iGrouped.entrySet())
            {
                if (e.getValue().isEmpty()) continue;
                sb.append("\n# -----------------------------------------------------------------------------------------------------------------------------------\n");
                sb.append("# ").append(pPageName).append(" — ").append(sn.getOrDefault(e.getKey(), e.getKey())).append("\n");
                sb.append("# -----------------------------------------------------------------------------------------------------------------------------------\n\n");
                for (HarvestedElement el : e.getValue())
                {
                    sb.append("# ").append(el.iComment).append("\n");
                    sb.append(pad(el.iKey)).append(" = ").append(el.iXPath).append("\n\n");
                }
            }
            iContent += sb.toString();
        }

        // Append UNVERIFIED block
        if (!pUnverified.isEmpty())
        {
            StringBuilder uv = new StringBuilder("\n\n");
            uv.append("# ===[ UNVERIFIED LOCATORS ]=================================================================\n");
            uv.append("# Highlight validation FAILED for these XPaths. NOT read by ObjReader.\n");
            uv.append("# Review manually, fix, then move above this block.  Harvested: ").append(iNow).append("\n");
            uv.append("# ============================================================================================\n\n");
            for (HarvestedElement e : pUnverified)
            {
                uv.append("# UNVERIFIED | ").append(e.iComment).append("\n");
                uv.append("# ").append(pad(e.iKey)).append(" = ").append(e.iXPath).append("\n\n");
            }
            iContent += uv.toString();
        }

        // ── Duplicate XPath block — advisory only, both keys still written above ──────
        if (pDuplicates != null && !pDuplicates.isEmpty())
        {
            StringBuilder dv = new StringBuilder("\n\n");
            dv.append("# ===[ DUPLICATE LOCATORS ]=================================================================\n");
            dv.append("# The following pairs of keys resolve to the SAME DOM element.\n");
            dv.append("# Both entries are stored above. Recommendation: keep the HIGH-confidence key,\n");
            dv.append("# delete the other, and update any step definitions that reference it.\n");
            dv.append("# Detected: ").append(iNow).append("\n");
            dv.append("# ============================================================================================\n\n");
            for (DuplicatePair dp : pDuplicates)
            {
                dv.append("# KEEP   [").append(dp.iConfidenceA).append("] ").append(dp.iKeyA)
                        .append(" = ").append(dp.iXPathA).append("\n");
                dv.append("# REMOVE [").append(dp.iConfidenceB).append("] ").append(dp.iKeyB)
                        .append(" = ").append(dp.iXPathB).append("\n");
                dv.append("# Recommended action: delete '").append(dp.removeKey())
                        .append("' and keep '").append(dp.keepKey()).append("'\n\n");
            }
            iContent += dv.toString();
        }

        Files.write(iFile.toPath(), iContent.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

        return iRes;
    }


    // ===============================================================================================================================
    // SECTION 5 : EXISTING FILE READER
    // ===============================================================================================================================

    private static Map<String,String> readExistingFile()
    {
        Map<String,String> m = new LinkedHashMap<>();
        File f = new File(OUTPUT_FILE);
        if (!f.exists()) return m;
        try
        {
            for (String line : Files.readAllLines(f.toPath(), StandardCharsets.UTF_8))
            {
                String t = line.trim();
                // Skip comments (including commented-out UNVERIFIED lines that start with "# i")
                if (t.isEmpty() || t.startsWith("#")) continue;
                int eq = t.indexOf('=');
                if (eq <= 0) continue;
                String k = t.substring(0, eq).trim();
                String v = t.substring(eq + 1).trim();
                if (!k.isEmpty() && !v.isEmpty()) m.put(k, v);
            }
        }
        catch (Exception ex) { log.warning("[HARVESTER] Read error: " + ex.getMessage()); }
        return m;
    }


    // ===============================================================================================================================
    // SECTION 6 : KEY GENERATOR
    // ===============================================================================================================================

    private static String makeKey(String page, String elType, String tag,
                                  String id, String fc, String al,
                                  String ph, String text, Set<String> seen)
    {
        String hint;
        if (!id.isEmpty() && !isAutoId(id)) hint = toCamel(id);
        else if (!fc.isEmpty())             hint = toCamel(fc);
        else if (!al.isEmpty() && al.length()<=30)  hint = toCamel(al);
        else if (!ph.isEmpty() && ph.length()<=30)  hint = toCamel(ph);
        else if (!text.isEmpty() && text.length()<=25) hint = toCamel(text);
        else                                hint = toCamel(tag);

        String base = ("i" + cap(page) + cap(hint) + suffix(elType, tag))
                .replaceAll("[^a-zA-Z0-9_]","");
        if (!seen.contains(base)) { seen.add(base); return base; }
        for (int n=2; n<=20; n++)
        {
            String c = base + n;
            if (!seen.contains(c)) { seen.add(c); return c; }
        }
        return null;
    }


    // ===============================================================================================================================
    // SECTION 7 : HELPERS
    // ===============================================================================================================================

    private static String buildHeader(WebDriver pDriver, String pPageName)
    {
        String url="", title="";
        try { url=pDriver.getCurrentUrl(); title=pDriver.getTitle(); } catch(Exception e_){}
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return  "# ===================================================================================================================================\n"
                + "# File          : ExtractedLocators.properties\n"
                + "# Description   : Auto-harvested XPath locators — smart-merged on every harvest run.\n"
                + "#                 VALIDATED = highlighted on screen. UNVERIFIED = at bottom, not used by ObjReader.\n"
                + "# First harvest : " + now + "  |  Page: " + pPageName + "\n"
                + "# URL           : " + url + "\n"
                + "# Page Title    : " + title + "\n"
                + "#\n"
                + "# XPath Priority: @id > @data-testid > @formcontrolname > @aria-label > @placeholder > text > class > Angular host\n"
                + "# Confidence    : HIGH (id/formcontrolname/aria) | MEDIUM (text/placeholder) | LOW (class/positional)\n"
                + "#\n"
                + "# Smart Merge:\n"
                + "#   ADD        — new key\n"
                + "#   UPDATE     — key exists but XPath changed (old kept as comment)\n"
                + "#   SKIP       — identical key + XPath\n"
                + "#   UNVERIFIED — highlight failed; written at bottom, NOT read by ObjReader\n"
                + "# ===================================================================================================================================\n";
    }

    private static String buildComment(String tag, String al, String text,
                                       String fc, String id, String ph,
                                       String elType, String conf)
    {
        String ctx;
        if (!id.isEmpty() && !isAutoId(id)) ctx = "@id='" + id + "'";
        else if (!fc.isEmpty())             ctx = "@formcontrolname='" + fc + "'";
        else if (!al.isEmpty())             ctx = "@aria-label='" + trunc(al,40) + "'";
        else if (!text.isEmpty())           ctx = "text='" + trunc(text,40) + "'";
        else if (!ph.isEmpty())             ctx = "@placeholder='" + trunc(ph,40) + "'";
        else                                ctx = "<" + tag + ">";
        return "[" + conf + "] " + elType + " | " + ctx;
    }

    private static Map<String,String> sectionNames()
    {
        Map<String,String> m = new LinkedHashMap<>();
        m.put(T_TEXTBOX,"TEXT BOXES & INPUT FIELDS"); m.put(T_BUTTON,"BUTTONS");
        m.put(T_LINK,"LINKS & NAVIGATION");           m.put(T_DROPDOWN,"DROPDOWNS & SELECTS");
        m.put(T_CHECKBOX,"CHECKBOXES");               m.put(T_RADIO,"RADIO BUTTONS");
        m.put(T_TOGGLE,"TOGGLES");                    m.put(T_LABEL,"LABELS, HEADERS & TEXT VERIFICATIONS");
        m.put(T_ERROR,"ERROR & VALIDATION MESSAGES"); m.put(T_TABLE,"TABLE & LIST ELEMENTS");
        m.put(T_DIALOG,"DIALOG & MODAL CONTAINERS");  m.put(T_UTILITY,"UTILITY & FRAMEWORK ELEMENTS");
        return m;
    }

    private static String[] typeOrder()
    {
        return new String[]{ T_TEXTBOX,T_BUTTON,T_LINK,T_DROPDOWN,T_CHECKBOX,
                T_RADIO,T_TOGGLE,T_LABEL,T_ERROR,T_TABLE,T_DIALOG,T_UTILITY };
    }

    private static String mc(String cls)
    {
        if (cls==null||cls.isEmpty()) return "";
        String[] skip={"ng-","cdk-","ng-tns","ng-trigger","ng-star","mat-mdc-",
                "mdc-","ng-untouched","ng-pristine","ng-valid","ng-touched",
                "ng-dirty","ng-invalid","mat-primary","mat-unthemed"};
        for (String tk : cls.split("\\s+"))
        {
            boolean gen=false;
            for (String p:skip) if(tk.startsWith(p)||tk.length()<4){gen=true;break;}
            if (!gen&&tk.length()<=MAX_CLASS_LEN) return tk;
        }
        return "";
    }

    private static String toCamel(String s)
    {
        if (s==null||s.isEmpty()) return "";
        String[] pts=s.trim().replaceAll("[^a-zA-Z0-9\\s_\\-]"," ").split("[\\s_\\-]+");
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<pts.length;i++)
        {
            String p=pts[i].trim(); if(p.isEmpty()) continue;
            sb.append(i==0?p.toLowerCase():cap(p.toLowerCase()));
        }
        return sb.toString();
    }

    private static String cap(String s)
    { return (s==null||s.isEmpty())?"":Character.toUpperCase(s.charAt(0))+s.substring(1); }

    private static String suffix(String et, String tag)
    {
        switch(et)
        {
            case T_TEXTBOX:  return tag.equals("textarea")?"Textarea":"Txtbox";
            case T_BUTTON:   return "Btn";
            case T_LINK:     return "Link";
            case T_DROPDOWN: return tag.equals("mat-option")?"Option":"Select";
            case T_CHECKBOX: return "Chk";
            case T_RADIO:    return "Radio";
            case T_TOGGLE:   return "Toggle";
            case T_LABEL:    return "Lbl";
            case T_ERROR:    return "Err";
            case T_TABLE:    return "Table";
            case T_DIALOG:   return "Dialog";
            default:         return "";
        }
    }

    private static String esc(String v)
    {
        if (v==null) return "";
        if (v.contains("'"))
            return "concat('"+v.replace("'","',\"'\",'")+"')";
        return v;
    }

    private static String escJs(String s)
    { return s==null?"":s.replace("\\","\\\\").replace("'","\\'"); }

    private static String trunc(String s, int max)
    { return (s==null)?"":s.length()<=max?s:s.substring(0,max); }

    private static String g(Map<String,String> m, String k)
    { String v=m.get(k); return v==null?"":v.trim(); }

    private static String pad(String k)
    { return String.format("%-40s",k); }

    private static boolean isAutoId(String id)
    { return id.matches("(mat-|cdk-|ng-|_mdc-).+"); }

    private static String escRe(String s)
    {
        return s.replace("\\","\\\\").replace(".","\\.").replace("[","\\[")
                .replace("]","\\]").replace("(","\\(").replace(")","\\)")
                .replace("{","\\{").replace("}","\\}").replace("*","\\*")
                .replace("+","\\+").replace("?","\\?").replace("^","\\^")
                .replace("$","\\$").replace("|","\\|");
    }

    private static XPathResult xp(String x, String c) { return new XPathResult(x,c); }


    // ===============================================================================================================================
    // SECTION 9 : DUPLICATE XPATH DETECTOR
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : detectDuplicateXPaths
    // Description   : After validation, checks whether any two different XPaths in the
    //                 validated list resolve to the exact same DOM element.
    //
    //                 Two locators are considered duplicates when driver.findElement() on
    //                 both returns the same element reference — checked by comparing the
    //                 element's outerHTML fingerprint (tag + id + class snapshot).
    //
    //                 Why this matters:
    //                   - Duplicate locators waste maintenance effort and create confusion
    //                     about which key to use in step definitions.
    //                   - When a duplicate is stored the reviewer can delete the lower-confidence
    //                     one and keep only the HIGH-confidence key.
    //
    //                 Returns a list of DuplicatePair records — each pair names the two keys
    //                 and their confidence tiers. This is logged as warnings and written into
    //                 a DUPLICATE LOCATORS section at the bottom of the properties file.
    //
    // Parameters    : pDriver    (WebDriver)         - active driver
    //                 pValidated (List<HarvestedElement>) - fully validated element list
    // Returns       : List<DuplicatePair> — may be empty if no duplicates found
    // ***************************************************************************************************************************************************************************************
    private static List<DuplicatePair> detectDuplicateXPaths(WebDriver pDriver,
                                                             List<HarvestedElement> pValidated)
    {
        List<DuplicatePair> iDuplicates = new ArrayList<>();

        // Build a fingerprint → list of keys map
        // Fingerprint = tag + "|" + id + "|" + first 80 chars of outerHTML
        Map<String, List<HarvestedElement>> iFingerprintMap = new LinkedHashMap<>();

        for (HarvestedElement iEl : pValidated)
        {
            try
            {
                WebElement iFound = pDriver.findElement(By.xpath(iEl.iXPath));
                String iFingerprint = buildElementFingerprint(pDriver, iFound);
                iFingerprintMap.computeIfAbsent(iFingerprint, k -> new ArrayList<>()).add(iEl);
            }
            catch (Exception ignored)
            {
                // Element may have become stale after validation pass — skip
            }
        }

        // Any fingerprint with more than one key = duplicates
        for (Map.Entry<String, List<HarvestedElement>> iEntry : iFingerprintMap.entrySet())
        {
            List<HarvestedElement> iGroup = iEntry.getValue();
            if (iGroup.size() < 2) continue;

            // Report all pairs within the group
            for (int a = 0; a < iGroup.size(); a++)
            {
                for (int b = a + 1; b < iGroup.size(); b++)
                {
                    iDuplicates.add(new DuplicatePair(
                            iGroup.get(a).iKey, iGroup.get(a).iXPath, iGroup.get(a).iConfidence,
                            iGroup.get(b).iKey, iGroup.get(b).iXPath, iGroup.get(b).iConfidence
                    ));
                }
            }
        }

        return iDuplicates;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildElementFingerprint
    // Description   : Creates a short string that uniquely identifies a DOM element for
    //                 duplicate detection. Uses tag + @id + @class snapshot + outerHTML
    //                 prefix (capped at 100 chars to avoid memory issues with large elements).
    // ***************************************************************************************************************************************************************************************
    private static String buildElementFingerprint(WebDriver pDriver, WebElement pElement)
    {
        try
        {
            String iTag   = pElement.getTagName();
            String iId    = pElement.getAttribute("id");    if (iId    == null) iId    = "";
            String iCls   = pElement.getAttribute("class"); if (iCls   == null) iCls   = "";
            String iOuter = (String) ((JavascriptExecutor) pDriver)
                    .executeScript("return arguments[0].outerHTML;", pElement);
            if (iOuter == null) iOuter = "";
            if (iOuter.length() > 100) iOuter = iOuter.substring(0, 100);
            return iTag + "|" + iId + "|" + iCls.trim() + "|" + iOuter;
        }
        catch (Exception ignored)
        {
            return UUID.randomUUID().toString(); // treat as unique on error
        }
    }


    // ===============================================================================================================================
    // SECTION 8 : DATA MODELS
    // ===============================================================================================================================

    private static class HarvestedElement
    {
        final String iKey, iXPath, iType, iComment, iConfidence;
        HarvestedElement(String k,String x,String t,String c,String cf)
        { iKey=k; iXPath=x; iType=t; iComment=c; iConfidence=cf; }
    }

    private static class XPathResult
    {
        final String iXPath, iConfidence;
        XPathResult(String x,String c) { iXPath=x; iConfidence=c; }
    }

    private static class MergeResult
    { int iAdded=0, iUpdated=0, iSkipped=0, iUnverified=0; }

    private static class DuplicatePair
    {
        final String iKeyA, iXPathA, iConfidenceA;
        final String iKeyB, iXPathB, iConfidenceB;

        DuplicatePair(String kA, String xA, String cfA,
                      String kB, String xB, String cfB)
        {
            iKeyA=kA; iXPathA=xA; iConfidenceA=cfA;
            iKeyB=kB; iXPathB=xB; iConfidenceB=cfB;
        }

        /** Returns the key to KEEP — the one with higher confidence, or A if equal */
        String keepKey()
        {
            if (HIGH.equals(iConfidenceA))  return iKeyA;
            if (HIGH.equals(iConfidenceB))  return iKeyB;
            if (MEDIUM.equals(iConfidenceA)) return iKeyA;
            return iKeyA;
        }

        /** Returns the key to REMOVE — the lower-confidence duplicate */
        String removeKey() { return keepKey().equals(iKeyA) ? iKeyB : iKeyA; }
    }
}