// ===================================================================================================================================
// File          : TestDataFactory.java
// Package       : utilities
// Description   : Dynamic test data generator for the BISS automation framework.
//                 Generates unique, timestamped, and random values at runtime so tests are
//                 never dependent on static Excel data alone.
//
//                 Zero external dependencies — pure Java (LocalDateTime, UUID, Random, String).
//
// Folder        : src/main/java/utilities/TestDataFactory.java
//
// Usage in Step Definitions or CommonFunctions resolveValue():
//   TestDataFactory.getUniqueEmail()           → "autouser_10032026_143022_847@biss.ie"
//   TestDataFactory.getTimestampedName("User") → "User_10032026_143022"
//   TestDataFactory.getRandomNumeric(6)        → "739284"
//   TestDataFactory.getRandomAlpha(8)          → "KxRpTmWq"
//   TestDataFactory.getDateOffset(5)           → "15-03-2026"
//   TestDataFactory.getDateOffset(-2)          → "08-03-2026"
//   TestDataFactory.getCurrentDate()           → "10-03-2026"
//   TestDataFactory.getCurrentTimestamp()      → "10-03-2026 14:30:22"
//   TestDataFactory.getUUID()                  → "f47ac10b-58cc-4372-a567-0e02b2c3d479"
//   TestDataFactory.getIrishPPSN()             → "1234567T"  (formatted test PPSN)
//
// Integration with iAction via TD: prefix in ExcelUtilities:
//   If ExcelUtilities.getCurrentTestDataValue() sees "FACTORY:getUniqueEmail" it delegates here.
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class TestDataFactory
{
    private static final Logger log = Logger.getLogger(TestDataFactory.class.getName());

    private static final DateTimeFormatter iDateFormatter      = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter iTimestampFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final DateTimeFormatter iFileTimestamp      = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");

    private static final String iDefaultEmailDomain = ConfigManager.getOrDefault("testdata.email.domain", "biss.ie");
    private static final Random iRandom             = new Random();

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor — static utility only
    // -------------------------------------------------------------------------------------------------------------------------------
    private TestDataFactory() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : getUniqueEmail
    // Description   : Generates a unique email address using a timestamp and random suffix.
    //                 Format: autouser_<ddMMyyyy_HHmmss>_<3digitRandom>@<domain>
    //                 Guaranteed unique within the same second to the nearest millisecond.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getUniqueEmail()
    {
        String iTimestamp = LocalDateTime.now().format(iFileTimestamp);
        String iRand      = String.valueOf(100 + iRandom.nextInt(900));
        String iEmail     = "autouser_" + iTimestamp + "_" + iRand + "@" + iDefaultEmailDomain;
        log.info("[TestDataFactory] Unique email : " + iEmail);
        return iEmail;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getUniqueEmail (with prefix)
    // Description   : Generates a unique email with a custom prefix.
    //                 Format: <prefix>_<ddMMyyyy_HHmmss>_<3digitRandom>@<domain>
    // Parameters    : pPrefix (String) - custom prefix for the email local part
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getUniqueEmail(String pPrefix)
    {
        String iSafePrefix = pPrefix == null ? "auto" : pPrefix.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String iTimestamp  = LocalDateTime.now().format(iFileTimestamp);
        String iRand       = String.valueOf(100 + iRandom.nextInt(900));
        String iEmail      = iSafePrefix + "_" + iTimestamp + "_" + iRand + "@" + iDefaultEmailDomain;
        log.info("[TestDataFactory] Unique email : " + iEmail);
        return iEmail;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getTimestampedName
    // Description   : Appends a timestamp to a base name. Useful for creating unique usernames,
    //                 company names, or file names that must not clash between test runs.
    //                 Format: <baseName>_<ddMMyyyy_HHmmss>
    // Parameters    : pBaseName (String) - the base name to append the timestamp to
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getTimestampedName(String pBaseName)
    {
        String iSafe  = pBaseName == null ? "Auto" : pBaseName.trim();
        String iValue = iSafe + "_" + LocalDateTime.now().format(iFileTimestamp);
        log.info("[TestDataFactory] Timestamped name : " + iValue);
        return iValue;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRandomNumeric
    // Description   : Generates a random numeric string of exactly the specified length.
    //                 First digit is never 0 (unless length=1).
    // Parameters    : pLength (int) - desired length of the numeric string (1–18)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getRandomNumeric(int pLength)
    {
        if (pLength <= 0 || pLength > 18)
        {
            throw new RuntimeException("[TestDataFactory] getRandomNumeric: length must be 1–18, got : " + pLength);
        }

        StringBuilder iSb = new StringBuilder();
        iSb.append((char) ('1' + iRandom.nextInt(9))); // non-zero first digit

        for (int i = 1; i < pLength; i++)
        {
            iSb.append((char) ('0' + iRandom.nextInt(10)));
        }

        return iSb.toString();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRandomAlpha
    // Description   : Generates a random mixed-case alphabetic string of the specified length.
    //                 Useful for generating reference codes, names, or identifiers.
    // Parameters    : pLength (int) - desired length (1–50)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getRandomAlpha(int pLength)
    {
        if (pLength <= 0 || pLength > 50)
        {
            throw new RuntimeException("[TestDataFactory] getRandomAlpha: length must be 1–50, got : " + pLength);
        }

        String  iChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder iSb = new StringBuilder();

        for (int i = 0; i < pLength; i++)
        {
            iSb.append(iChars.charAt(iRandom.nextInt(iChars.length())));
        }

        return iSb.toString();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRandomAlphaNumeric
    // Description   : Generates a random alphanumeric string of the specified length.
    //                 Useful for reference numbers, application IDs, and form fields.
    // Parameters    : pLength (int) - desired length (1–50)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getRandomAlphaNumeric(int pLength)
    {
        if (pLength <= 0 || pLength > 50)
        {
            throw new RuntimeException("[TestDataFactory] getRandomAlphaNumeric: length must be 1–50, got : " + pLength);
        }

        String        iChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder iSb    = new StringBuilder();

        for (int i = 0; i < pLength; i++)
        {
            iSb.append(iChars.charAt(iRandom.nextInt(iChars.length())));
        }

        return iSb.toString();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentDate
    // Description   : Returns today's date formatted as dd-MM-yyyy
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentDate()
    {
        return LocalDate.now().format(iDateFormatter);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDateOffset
    // Description   : Returns a date offset from today by the given number of days.
    //                 Positive = future date. Negative = past date.
    //                 Format: dd-MM-yyyy
    // Parameters    : pDays (int) - number of days to offset from today (can be negative)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getDateOffset(int pDays)
    {
        return LocalDate.now().plusDays(pDays).format(iDateFormatter);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDateFormatted
    // Description   : Returns today's date in a custom format string
    // Parameters    : pFormat (String) - Java DateTimeFormatter pattern, e.g. "yyyy/MM/dd", "dd MMM yyyy"
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getDateFormatted(String pFormat)
    {
        try
        {
            return LocalDate.now().format(DateTimeFormatter.ofPattern(pFormat));
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[TestDataFactory] Invalid date format pattern : '" + pFormat + "'");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getCurrentTimestamp
    // Description   : Returns the current date and time formatted as dd-MM-yyyy HH:mm:ss
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getCurrentTimestamp()
    {
        return LocalDateTime.now().format(iTimestampFormatter);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getUUID
    // Description   : Returns a randomly generated UUID (version 4).
    //                 Suitable for unique reference IDs, correlation IDs, and test identifiers.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getUUID()
    {
        return UUID.randomUUID().toString();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getIrishPhoneNumber
    // Description   : Generates a plausible Irish mobile phone number for test data.
    //                 Format: 08X-XXXXXXX (X = random digit). NOT a real number.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getIrishPhoneNumber()
    {
        // Irish mobile prefixes: 083, 085, 086, 087, 089
        String[] iPrefixes = {"083", "085", "086", "087", "089"};
        String   iPrefix   = iPrefixes[iRandom.nextInt(iPrefixes.length)];
        return iPrefix + getRandomNumeric(7);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getIrishEircode
    // Description   : Generates a plausible Irish Eircode for test data.
    //                 Format: D01 XXXX (Dublin) with random routing key and unique identifier.
    //                 NOT a real address — for form field testing only.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getIrishEircode()
    {
        String[] iRoutingKeys = {"D01", "D02", "D04", "D06", "D08", "K67", "A94", "T23"};
        String   iKey         = iRoutingKeys[iRandom.nextInt(iRoutingKeys.length)];
        String   iUnique      = getRandomAlphaNumeric(4).toUpperCase();
        return iKey + " " + iUnique;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : fromFactoryKey
    // Description   : Resolves a FACTORY: prefixed key to a generated value.
    //                 Used by ExcelUtilities or CommonFunctions when test data contains a FACTORY: marker.
    //                 Example: cell value "FACTORY:getUniqueEmail" → calls getUniqueEmail()
    //                          cell value "FACTORY:getRandomNumeric:6" → calls getRandomNumeric(6)
    //                          cell value "FACTORY:getDateOffset:-3"   → calls getDateOffset(-3)
    // Parameters    : pFactoryKey (String) - the key after the FACTORY: prefix
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String fromFactoryKey(String pFactoryKey)
    {
        if (pFactoryKey == null || pFactoryKey.trim().isEmpty())
        {
            throw new RuntimeException("[TestDataFactory] fromFactoryKey: key cannot be blank.");
        }

        String[] iParts    = pFactoryKey.trim().split(":", 2);
        String   iMethod   = iParts[0].trim();
        String   iArgument = iParts.length > 1 ? iParts[1].trim() : "";

        switch (iMethod)
        {
            case "getUniqueEmail":
                return iArgument.isEmpty() ? getUniqueEmail() : getUniqueEmail(iArgument);

            case "getTimestampedName":
                return getTimestampedName(iArgument.isEmpty() ? "Auto" : iArgument);

            case "getRandomNumeric":
                return getRandomNumeric(iArgument.isEmpty() ? 6 : Integer.parseInt(iArgument));

            case "getRandomAlpha":
                return getRandomAlpha(iArgument.isEmpty() ? 8 : Integer.parseInt(iArgument));

            case "getRandomAlphaNumeric":
                return getRandomAlphaNumeric(iArgument.isEmpty() ? 8 : Integer.parseInt(iArgument));

            case "getCurrentDate":
                return getCurrentDate();

            case "getCurrentTimestamp":
                return getCurrentTimestamp();

            case "getDateOffset":
                return getDateOffset(iArgument.isEmpty() ? 0 : Integer.parseInt(iArgument));

            case "getDateFormatted":
                return getDateFormatted(iArgument.isEmpty() ? "dd-MM-yyyy" : iArgument);

            case "getUUID":
                return getUUID();

            case "getIrishPhoneNumber":
                return getIrishPhoneNumber();

            case "getIrishEircode":
                return getIrishEircode();

            default:
                throw new RuntimeException("[TestDataFactory] Unknown factory method : '" + iMethod
                        + "'. Check your Excel FACTORY: value.");
        }
    }
}