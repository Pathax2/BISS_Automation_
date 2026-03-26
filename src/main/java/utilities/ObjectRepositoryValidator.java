// ===================================================================================================================================
// File          : ObjectRepositoryValidator.java
// Package       : utilities
// Description   : Object Repository health check for the BISS automation framework.
//                 Runs ONCE during @BeforeAll (before any browser is launched) and validates:
//                   1. All OR keys referenced in a supplied whitelist actually exist in the properties file
//                   2. No OR values are empty or blank
//                   3. XPath values are syntactically parseable (heuristic check)
//                   4. CSS selectors do not contain common typos
//                   5. Prints a complete validation report to console — fail-fast with clear message
//
//                 Prevents cryptic mid-run failures caused by missing or malformed locators.
//
// Folder        : src/main/java/utilities/ObjectRepositoryValidator.java
//
// Integration   :
//   Called from Hooks.java @BeforeAll — before launchBrowser().
//   Pass the set of keys your step definitions use.
//   ObjReader.getAllKeys() provides the full loaded key set.
//
// Usage:
//   ObjectRepositoryValidator.validate(Set.of(
//       "iLoginEmailFld", "iLoginPasswordFld", "iLoginbtn", "iDashboardTitle"
//   ));
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class ObjectRepositoryValidator
{
    private static final Logger log = Logger.getLogger(ObjectRepositoryValidator.class.getName());

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor
    // -------------------------------------------------------------------------------------------------------------------------------
    private ObjectRepositoryValidator() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : validate
    // Description   : Validates that all keys in pRequiredKeys exist in the OR and have non-blank values.
    //                 Also runs a basic XPath/CSS heuristic check on values.
    //                 Throws RuntimeException if ANY issue is found — fail-fast before browser launch.
    // Parameters    : pRequiredKeys (Set<String>) - OR key names that must exist (from step definitions)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void validate(Set<String> pRequiredKeys)
    {
        List<String> iErrors   = new ArrayList<>();
        List<String> iWarnings = new ArrayList<>();
        int          iChecked  = 0;

        log.info("[ORValidator] Starting Object Repository validation...");
        log.info("[ORValidator] Required keys to check : " + (pRequiredKeys == null ? 0 : pRequiredKeys.size()));

        if (pRequiredKeys == null || pRequiredKeys.isEmpty())
        {
            log.warning("[ORValidator] No required keys supplied — validation skipped.");
            return;
        }

        for (String iKey : pRequiredKeys)
        {
            iChecked++;

            if (iKey == null || iKey.trim().isEmpty())
            {
                iErrors.add("Blank key found in required keys list at position " + iChecked);
                continue;
            }

            // 1. Check key exists in OR
            if (!ObjReader.containsKey(iKey))
            {
                iErrors.add("MISSING KEY : '" + iKey + "' not found in ObjectRepository.properties");
                continue;
            }

            // 2. Check value is non-blank
            String iValue = ObjReader.getLocator(iKey);
            if (iValue == null || iValue.trim().isEmpty())
            {
                iErrors.add("EMPTY VALUE : Key '" + iKey + "' has a blank locator value in ObjectRepository.properties");
                continue;
            }

            // 3. Determine locator type and run heuristic validation
            String iLocatorType = ObjReader.getLocatorType(iKey);
            validateLocatorValue(iKey, iLocatorType, iValue.trim(), iErrors, iWarnings);
        }

        printReport(iChecked, iErrors, iWarnings);

        if (!iErrors.isEmpty())
        {
            throw new RuntimeException("[ORValidator] Object Repository validation FAILED with "
                    + iErrors.size() + " error(s). Fix the above issues before running tests.");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : validateAll
    // Description   : Validates ALL keys currently loaded in the Object Repository.
    //                 Use this when you want to validate the entire OR file, not just a subset.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void validateAll()
    {
        validate(ObjReader.getAllKeys());
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------------------------------------------------------------

    private static void validateLocatorValue(
            String pKey,
            String pType,
            String pValue,
            List<String> pErrors,
            List<String> pWarnings)
    {
        switch (pType.toUpperCase())
        {
            case "XPATH":
                validateXpath(pKey, pValue, pErrors, pWarnings);
                break;

            case "CSS":
                validateCss(pKey, pValue, pErrors, pWarnings);
                break;

            case "ID":
                if (pValue.contains(" "))
                {
                    pWarnings.add("ID WARNING : Key '" + pKey + "' — ID value contains a space : '" + pValue
                            + "'. IDs with spaces are invalid HTML.");
                }
                break;

            case "NAME":
            case "CLASSNAME":
            case "TAGNAME":
            case "LINKTEXT":
            case "PARTIALLINKTEXT":
                // Basic non-empty check already done above — no further heuristic needed
                break;

            default:
                pWarnings.add("UNKNOWN TYPE WARNING : Key '" + pKey + "' has unrecognised locator type '" + pType + "'");
                break;
        }
    }

    private static void validateXpath(String pKey, String pValue, List<String> pErrors, List<String> pWarnings)
    {
        // Heuristic checks — not a full parser but catches the most common authoring mistakes
        if (!pValue.startsWith("/") && !pValue.startsWith("(") && !pValue.startsWith(".")
                && !pValue.startsWith("@"))
        {
            pWarnings.add("XPATH WARNING : Key '" + pKey + "' — value does not start with '/', '(', '.', or '@'. "
                    + "This may not be a valid XPath. Value : '" + truncate(pValue) + "'");
        }

        // Check for unbalanced brackets
        int iOpenSquare  = countOccurrences(pValue, '[');
        int iCloseSquare = countOccurrences(pValue, ']');
        if (iOpenSquare != iCloseSquare)
        {
            pErrors.add("XPATH ERROR : Key '" + pKey + "' — unbalanced square brackets "
                    + "[open=" + iOpenSquare + ", close=" + iCloseSquare + "]. "
                    + "Value : '" + truncate(pValue) + "'");
        }

        int iOpenParen  = countOccurrences(pValue, '(');
        int iCloseParen = countOccurrences(pValue, ')');
        if (iOpenParen != iCloseParen)
        {
            pErrors.add("XPATH ERROR : Key '" + pKey + "' — unbalanced parentheses "
                    + "[open=" + iOpenParen + ", close=" + iCloseParen + "]. "
                    + "Value : '" + truncate(pValue) + "'");
        }

        // Check for common quote mistakes — mismatched single/double quotes inside predicates
        long iSingleQuotes = pValue.chars().filter(c -> c == '\'').count();
        if (iSingleQuotes % 2 != 0)
        {
            pWarnings.add("XPATH WARNING : Key '" + pKey + "' — odd number of single quotes in XPath. "
                    + "Possible unclosed string literal. Value : '" + truncate(pValue) + "'");
        }
    }

    private static void validateCss(String pKey, String pValue, List<String> pErrors, List<String> pWarnings)
    {
        // CSS selectors must not contain XPath-specific syntax
        if (pValue.contains("//") || pValue.contains("[@"))
        {
            pErrors.add("CSS ERROR : Key '" + pKey + "' — value looks like an XPath, not a CSS selector. "
                    + "Check the locator type suffix. Value : '" + truncate(pValue) + "'");
        }

        int iOpenBrace  = countOccurrences(pValue, '{');
        int iCloseBrace = countOccurrences(pValue, '}');
        if (iOpenBrace != iCloseBrace)
        {
            pWarnings.add("CSS WARNING : Key '" + pKey + "' — unbalanced curly braces in CSS selector.");
        }
    }

    private static void printReport(int pChecked, List<String> pErrors, List<String> pWarnings)
    {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║         OBJECT REPOSITORY VALIDATION REPORT                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════════════╣");
        System.out.println("║  Keys checked  : " + pad(String.valueOf(pChecked), 46) + "║");
        System.out.println("║  Errors        : " + pad(String.valueOf(pErrors.size()), 46) + "║");
        System.out.println("║  Warnings      : " + pad(String.valueOf(pWarnings.size()), 46) + "║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");

        if (!pWarnings.isEmpty())
        {
            System.out.println("\n  ⚠  WARNINGS:");
            pWarnings.forEach(w -> System.out.println("     • " + w));
        }

        if (!pErrors.isEmpty())
        {
            System.out.println("\n  ✗  ERRORS (must fix before execution):");
            pErrors.forEach(e -> System.out.println("     ✗ " + e));
        }
        else
        {
            System.out.println("\n  ✓  All Object Repository keys validated successfully.");
        }

        System.out.println();
    }

    private static int countOccurrences(String pStr, char pChar)
    {
        int iCount = 0;
        for (char c : pStr.toCharArray()) { if (c == pChar) iCount++; }
        return iCount;
    }

    private static String truncate(String pValue)
    {
        return pValue.length() > 80 ? pValue.substring(0, 80) + "..." : pValue;
    }

    private static String pad(String pValue, int pWidth)
    {
        if (pValue.length() >= pWidth) { return pValue; }
        return pValue + " ".repeat(pWidth - pValue.length());
    }
}
