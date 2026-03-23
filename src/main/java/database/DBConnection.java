// ===================================================================================================================================
// File          : DBConnection.java
// Package       : database
// Description   : Multi-database query router for the BISS automation framework.
//                 Supports two Oracle database connections — DATA (BISS_DATA) and INET (BPS_CONNECT).
//                 Executes named SQL queries identified by a logical label string, stores results
//                 in a static result store, and exposes typed accessor methods for step definitions.
//
//                 Supported DB keys:
//                   "DATA" — BISS_DATA schema  (default for most application queries)
//                   "INET" — BPS_CONNECT schema (agent login / username lookups)
//
//                 Supported query labels:
//                   "List of herds with no errors at all"  — DATA | params: year, [limit], [mode: NORMAL|NOT_STARTED]
//                   "Herds by scheme year"                 — DATA | params: schemeYear
//                   "Get hold id for herd"                 — DATA | params: herdNumber
//                   "Get login id for herd"                — INET | params: herdNumber
//
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  HOW TO USE IN STEP DEFINITIONS
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//
//   // Run a query and read a value from the first row
//   DBRouter.runDB("DATA", "List of herds with no errors at all", "2026", "1");
//   String iHerdNo = DBRouter.getValue("APP_HERD_NO");
//
//   // Chain — use result from first query as input to second
//   DBRouter.runDB("INET", "Get login id for herd", iHerdNo);
//   String iUsername = DBRouter.getValue("USERNAME");
//
//   // Iterate all rows
//   for (Map<String, Object> iRow : DBRouter.getRows()) {
//       String iHerd = String.valueOf(iRow.get("APP_HERD_NO"));
//   }
//
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//  db.properties — required at src/test/resources/db.properties
// ═══════════════════════════════════════════════════════════════════════════════════════════════
//
//   # DATA DB (BISS_DATA)
//   db.url=jdbc:oracle:thin:@//host:port/service
//   db.username=BISS_DATA
//   db.password=yourpassword
//
//   # INET DB (BPS_CONNECT)
//   db.inet.url=jdbc:oracle:thin:@//host:port/service
//   db.inet.username=BPS_CONNECT
//   db.inet.password=yourpassword
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.*;

public class DBRouter
{

    // ===============================================================================================================================
    // SECTION 1 : Static Result Store
    // Populated after every runDB() call — accessible via getValue(), getRows(), hasRows()
    // ===============================================================================================================================

    public static List<Map<String, Object>> lastRows   = new ArrayList<>();
    public static Object                    lastScalar = null;
    public static String                    lastLabel  = "";


    // ===============================================================================================================================
    // SECTION 2 : DB Configuration
    // Properties loaded once from db.properties at class initialisation via static block
    // ===============================================================================================================================

    private static final Logger log             = Logger.getLogger(DBRouter.class.getName());
    private static final String PROPERTIES_FILE = "src/test/resources/db.properties";

    // ── DATA DB credentials (BISS_DATA schema) ───────────────────────────────────────────────
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    // ── INET DB credentials (BPS_CONNECT schema) ─────────────────────────────────────────────
    private static String DB_INET_URL;
    private static String DB_INET_USER;
    private static String DB_INET_PASSWORD;

    // ── Static initialiser — runs once when class is first loaded ─────────────────────────────
    static
    {
        setupLogger();
        loadDBProperties();
    }


    // ===============================================================================================================================
    // SECTION 3 : Public API — runDB()
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : runDB
    // Description   : Executes a named SQL query on the specified database.
    //                 The label string is normalised (trimmed, uppercased, whitespace-collapsed)
    //                 before matching — so casing and extra spaces in the label do not matter.
    //                 Results are stored in the static result store (lastRows, lastScalar).
    //
    // Parameters    : pDbKey  (String)    - "DATA" or "INET"
    //                 pLabel  (String)    - logical query label (see supported labels in file header)
    //                 pParams (String...) - zero or more positional parameters for the query
    //
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Precondition  : db.properties must be loaded; DB must be reachable from the test runner host
    // Date Created  : 10-03-2026
    //
    // Usage Examples:
    //   DBRouter.runDB("DATA", "List of herds with no errors at all", "2026", "5");
    //   DBRouter.runDB("DATA", "List of herds with no errors at all", "2026", "1", "NOT_STARTED");
    //   DBRouter.runDB("DATA", "Herds by scheme year", "2026");
    //   DBRouter.runDB("DATA", "Get hold id for herd", "K1234567");
    //   DBRouter.runDB("INET", "Get login id for herd", "K1234567");
    // ***************************************************************************************************************************************************************************************
    public static void runDB(String pDbKey, String pLabel, String... pParams)
    {
        Objects.requireNonNull(pDbKey,  "dbKey cannot be null");
        Objects.requireNonNull(pLabel, "label cannot be null");

        final String iWhichDb  = normalizeDb(pDbKey);
        final String iLabelKey = normalize(pLabel);

        lastLabel = pLabel;

        String   iSql        = null;
        Object[] iJdbcParams = new Object[0];

        switch (iLabelKey)
        {

            // ------------------------------------------------------------------
            // DATA DB : List of herds with no errors
            // Params  : [0] year (required)
            //           [1] limit — max rows to return (optional, default 5)
            //           [2] mode  — NORMAL (default) or NOT_STARTED
            //
            // Mode NORMAL      — herds where no error records exist (mde_abbrev = 'I')
            // Mode NOT_STARTED — herds where app_mde_code = 1 (application not yet started)
            // ------------------------------------------------------------------
            case "LIST OF HERDS WITH NO ERRORS AT ALL":
            {
                requireParamCountBetween(iLabelKey, pParams, 1, 3);

                int    iYear    = parseInt(pParams[0], "year");
                int    iMaxRows = (pParams.length >= 2) ? parseInt(pParams[1], "limit") : 5;
                String iMode    = (pParams.length >= 3) ? pParams[2].trim().toUpperCase() : "NORMAL";

                if ("NOT_STARTED".equals(iMode))
                {
                    // ── Mode: NOT STARTED — app_mde_code = 1 ─────────────────────────────
                    iSql =
                        "SELECT app_herd_no, app_year, aph_herd_type "        +
                        "FROM   vwbs_application_herd "                        +
                        "WHERE  app_mde_code = 1 "                             +   // 1 = NOT STARTED
                        "AND    app_year     = ? "                              +
                        "AND    ROWNUM      <= ? "                              +
                        "ORDER  BY app_herd_no ASC";

                    iJdbcParams = new Object[]{ iYear, iMaxRows };
                }
                else
                {
                    // ── Mode: NORMAL — herds with no error records ────────────────────────
                    iSql =
                        "SELECT a.app_herd_no, a.aph_herd_no, a.applicant_type "  +
                        "FROM   vwbs_error e, vwbs_application_herd a "            +
                        "WHERE  e.eor_year   (+) = a.app_year "                    +
                        "AND    e.eor_app_id (+) = a.aph_app_id "                  +
                        "AND    a.aph_id         = NVL(e.eor_aph_id (+), a.aph_id) " +
                        "AND    e.eor_app_id IS NULL "                             +
                        "AND    a.mde_abbrev     = 'I' "                           +
                        "AND    a.app_year       = ? "                             +
                        "AND    ROWNUM          <= ? "                             +
                        "ORDER  BY a.app_herd_no, a.aph_herd_no, a.applicant_type";

                    iJdbcParams = new Object[]{ iYear, iMaxRows };
                }

                break;
            }

            // ------------------------------------------------------------------
            // DATA DB : Herds by scheme year
            // Params  : [0] schemeYear
            // ------------------------------------------------------------------
            case "HERDS BY SCHEME YEAR":
            {
                requireParamCountBetween(iLabelKey, pParams, 1, 1);

                iSql =
                    "SELECT   h.hld_herd_no "                                              +
                    "FROM     tddp_application a "                                         +
                    "JOIN     tdco_holding h ON h.hld_id = a.app_applicant_holding_id "   +
                    "WHERE    a.app_syr_code = ? "                                         +
                    "ORDER BY h.hld_herd_no";

                iJdbcParams = new Object[]{ parseInt(pParams[0], "schemeYear") };
                break;
            }

            // ------------------------------------------------------------------
            // DATA DB : Resolve internal holding ID for a herd number
            // Params  : [0] herdNumber
            // ------------------------------------------------------------------
            case "GET HOLD ID FOR HERD":
            {
                requireParamCountBetween(iLabelKey, pParams, 1, 1);

                iSql        = "SELECT get_hold(?) AS holding_id FROM dual";
                iJdbcParams = new Object[]{ pParams[0] };
                break;
            }

            // ------------------------------------------------------------------
            // INET DB : Get agent username linked to a herd number (BPS_CONNECT)
            // Params  : [0] herdNumber
            // Returns : HERDNO, USERNAME
            // ------------------------------------------------------------------
            case "GET LOGIN ID FOR HERD":
            {
                requireParamCountBetween(iLabelKey, pParams, 1, 1);

                iSql =
                    "SELECT hbcus.bcus_bus_id AS HERDNO, "                                     +
                    "       (SELECT ui.uri_username "                                           +
                    "          FROM tdcr_user_info ui "                                        +
                    "         WHERE ui.uri_ccs_bus_id = abcus.bcus_bus_id "                    +
                    "           AND ROWNUM = 1) AS USERNAME "                                  +
                    "  FROM tdco_customer_asscs ca, "                                          +
                    "       tdco_business_customers abcus, "                                   +
                    "       tdco_business_customers hbcus "                                    +
                    " WHERE SYSDATE BETWEEN ca.ca_start_date AND ca.ca_end_date "             +
                    "   AND ca.ca_cac_code    = 194 "                                          +
                    "   AND ca.ca_bcus_id_from = abcus.bcus_id "                               +
                    "   AND ca.ca_bcus_id_to   = hbcus.bcus_id "                               +
                    "   AND hbcus.bcus_bus_id  = ? "                                           +
                    " ORDER BY 2, 1";

                iJdbcParams = new Object[]{ pParams[0] };
                break;
            }

            default:
                throw new RuntimeException("Unknown DB label : '" + pLabel + "'. "
                        + "Check supported labels in DBConnection.java file header.");
        }

        executeQuery(iWhichDb, iSql, iJdbcParams);
    }


    // ===============================================================================================================================
    // SECTION 4 : Query Execution
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeQuery
    // Description   : Opens a JDBC connection to the specified database, executes the prepared SQL,
    //                 and stores all result rows in lastRows. The first column of the first row is
    //                 also stored in lastScalar for single-value queries.
    //                 Connection is closed automatically via try-with-resources.
    //
    // Parameters    : pWhichDb   (String)   - "DATA" or "INET"
    //                 pSql       (String)   - parameterised SQL string
    //                 pParams    (Object[]) - positional bind parameters
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void executeQuery(String pWhichDb, String pSql, Object... pParams)
    {
        lastRows   = new ArrayList<>();
        lastScalar = null;

        try (Connection         iConn = "INET".equals(pWhichDb) ? getInetConnection() : getDataConnection();
             PreparedStatement  iStmt = iConn.prepareStatement(pSql))
        {
            for (int i = 0; i < pParams.length; i++)
            {
                iStmt.setObject(i + 1, pParams[i]);
            }

            try (ResultSet iRs = iStmt.executeQuery())
            {
                ResultSetMetaData iMeta     = iRs.getMetaData();
                int               iColCount = iMeta.getColumnCount();

                while (iRs.next())
                {
                    Map<String, Object> iRow = new LinkedHashMap<>();

                    for (int iCol = 1; iCol <= iColCount; iCol++)
                    {
                        // Prefer column label (AS alias) over column name
                        String iColName = iMeta.getColumnLabel(iCol);
                        if (iColName == null || iColName.isEmpty())
                        {
                            iColName = iMeta.getColumnName(iCol);
                        }
                        iRow.put(iColName.toUpperCase(Locale.ROOT), iRs.getObject(iCol));
                    }

                    lastRows.add(iRow);
                }

                // Convenience scalar — first column of first row
                if (!lastRows.isEmpty())
                {
                    lastScalar = lastRows.get(0).values().iterator().next();
                }
            }

            log.info("[DBRouter] Query executed on [" + pWhichDb + "] | Rows returned: " + lastRows.size());
        }
        catch (SQLException iException)
        {
            throw new RuntimeException("DB [" + pWhichDb + "] execution failed : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getDataConnection
    // Description   : Opens and returns a JDBC connection to the DATA (BISS_DATA) schema
    // ***************************************************************************************************************************************************************************************
    private static Connection getDataConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getInetConnection
    // Description   : Opens and returns a JDBC connection to the INET (BPS_CONNECT) schema
    // ***************************************************************************************************************************************************************************************
    private static Connection getInetConnection() throws SQLException
    {
        return DriverManager.getConnection(DB_INET_URL, DB_INET_USER, DB_INET_PASSWORD);
    }


    // ===============================================================================================================================
    // SECTION 5 : Public Result Accessors
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : getValue
    // Description   : Returns the value of the specified column from the first row of the last query result.
    //                 Column name matching is case-insensitive.
    //                 Returns null if no rows were returned or the column does not exist.
    //
    // Parameters    : pColumn (String) - column name to retrieve (e.g. "APP_HERD_NO", "USERNAME")
    // Returns       : String — column value as string, or null if not found
    //
    // Usage         : String iHerd = DBRouter.getValue("APP_HERD_NO");
    // ***************************************************************************************************************************************************************************************
    public static String getValue(String pColumn)
    {
        if (lastRows.isEmpty())
        {
            return null;
        }

        Map<String, Object> iFirstRow = lastRows.get(0);

        for (String iKey : iFirstRow.keySet())
        {
            if (iKey.equalsIgnoreCase(pColumn))
            {
                return Objects.toString(iFirstRow.get(iKey), null);
            }
        }

        return null;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getRows
    // Description   : Returns all rows from the last query result as a List of column-value Maps.
    //                 Column names are stored in uppercase. Use for iterating multi-row results.
    //
    // Returns       : List<Map<String, Object>> — all result rows; empty list if no rows returned
    //
    // Usage         : for (Map<String, Object> iRow : DBRouter.getRows()) { ... }
    // ***************************************************************************************************************************************************************************************
    public static List<Map<String, Object>> getRows()
    {
        return lastRows;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : hasRows
    // Description   : Returns true if the last query returned at least one row.
    //                 Use to guard getValue() calls and avoid null pointer issues.
    //
    // Returns       : boolean — true if result is non-empty
    //
    // Usage         : if (DBRouter.hasRows()) { String iVal = DBRouter.getValue("COL"); }
    // ***************************************************************************************************************************************************************************************
    public static boolean hasRows()
    {
        return !lastRows.isEmpty();
    }


    // ===============================================================================================================================
    // SECTION 6 : Properties Loader
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadDBProperties
    // Description   : Reads database connection credentials from db.properties file.
    //                 Called once during class static initialisation.
    //                 Throws immediately with a clear message if any required property is missing.
    // ***************************************************************************************************************************************************************************************
    private static void loadDBProperties()
    {
        Properties iProps = new Properties();

        try (FileInputStream iFis = new FileInputStream(PROPERTIES_FILE))
        {
            iProps.load(iFis);

            // ── DATA DB properties (BISS_DATA) ────────────────────────────────────────────────
            DB_URL      = iProps.getProperty("db.url");
            DB_USER     = iProps.getProperty("db.username");
            DB_PASSWORD = iProps.getProperty("db.password");

            if (isBlank(DB_URL) || isBlank(DB_USER) || isBlank(DB_PASSWORD))
            {
                throw new RuntimeException(
                    "Missing DATA DB properties in " + PROPERTIES_FILE
                    + " | Required: db.url, db.username, db.password");
            }

            // ── INET DB properties (BPS_CONNECT) ──────────────────────────────────────────────
            DB_INET_URL      = iProps.getProperty("db.inet.url");
            DB_INET_USER     = iProps.getProperty("db.inet.username");
            DB_INET_PASSWORD = iProps.getProperty("db.inet.password");

            if (isBlank(DB_INET_URL) || isBlank(DB_INET_USER) || isBlank(DB_INET_PASSWORD))
            {
                throw new RuntimeException(
                    "Missing INET DB properties in " + PROPERTIES_FILE
                    + " | Required: db.inet.url, db.inet.username, db.inet.password");
            }

            log.info("[DBRouter] DATA and INET connection properties loaded successfully.");
        }
        catch (IOException iException)
        {
            throw new RuntimeException(
                "Cannot load DB properties from : " + PROPERTIES_FILE
                + " | Reason: " + iException.getMessage(), iException);
        }
    }


    // ===============================================================================================================================
    // SECTION 7 : Logger Setup
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : setupLogger
    // Description   : Configures the class logger to write to IntelliJ console with a clean timestamp format.
    //                 Format: [yyyy-MM-dd HH:mm:ss] [LEVEL] message
    // ***************************************************************************************************************************************************************************************
    private static void setupLogger()
    {
        try
        {
            Logger iLogger = Logger.getLogger(DBRouter.class.getName());
            iLogger.setUseParentHandlers(false);
            iLogger.setLevel(Level.ALL);

            // Fully qualified to avoid ambiguity with java.util.Formatter
            java.util.logging.Formatter iFormatter = new java.util.logging.Formatter()
            {
                @Override
                public String format(LogRecord pRecord)
                {
                    return String.format("[%s] [%-7s] %s%n",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(pRecord.getMillis())),
                            pRecord.getLevel(),
                            pRecord.getMessage());
                }
            };

            ConsoleHandler iConsole = new ConsoleHandler();
            iConsole.setLevel(Level.ALL);
            iConsole.setFormatter(iFormatter);

            for (Handler iHandler : iLogger.getHandlers())
            {
                iLogger.removeHandler(iHandler);
            }

            iLogger.addHandler(iConsole);
        }
        catch (Exception iException)
        {
            System.err.println("[DBRouter] Logger setup failed : " + iException.getMessage());
        }
    }


    // ===============================================================================================================================
    // SECTION 8 : Normalizers and Validators
    // ===============================================================================================================================

    // ***************************************************************************************************************************************************************************************
    // Function Name : normalizeDb
    // Description   : Validates and normalises the dbKey parameter to "DATA" or "INET".
    //                 Throws with a clear message if an unsupported value is passed.
    // ***************************************************************************************************************************************************************************************
    private static String normalizeDb(String pDbKey)
    {
        String iVal = (pDbKey == null) ? "" : pDbKey.trim().toUpperCase(Locale.ROOT);

        if ("DATA".equals(iVal) || "INET".equals(iVal))
        {
            return iVal;
        }

        throw new IllegalArgumentException("dbKey must be 'DATA' or 'INET'. Got : '" + pDbKey + "'");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : normalize
    // Description   : Normalises a label string for switch matching.
    //                 Replaces colons, dashes, and multiple spaces; trims and uppercases.
    //                 Allows natural language labels to match regardless of casing or punctuation.
    // ***************************************************************************************************************************************************************************************
    private static String normalize(String pLabel)
    {
        if (pLabel == null) { return ""; }

        return pLabel
                .replace(':', ' ')
                .replace("\u2013", "-")   // en-dash
                .replace("\u2014", "-")   // em-dash
                .replaceAll("\\s+", " ")
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : requireParamCountBetween
    // Description   : Validates the parameter count is within the expected range.
    //                 Throws with a clear message if the count is outside the min-max range.
    // ***************************************************************************************************************************************************************************************
    private static void requireParamCountBetween(String pLabelKey, String[] pParams, int pMin, int pMax)
    {
        int iCount = (pParams == null) ? 0 : pParams.length;

        if (iCount < pMin || iCount > pMax)
        {
            throw new IllegalArgumentException(
                "Label '" + pLabelKey + "' expects between " + pMin + " and " + pMax
                + " parameter(s). Got : " + iCount);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : parseInt
    // Description   : Parses a string to an integer with a descriptive error message on failure.
    // ***************************************************************************************************************************************************************************************
    private static int parseInt(String pValue, String pParamName)
    {
        try
        {
            return Integer.parseInt(pValue.trim());
        }
        catch (Exception iException)
        {
            throw new IllegalArgumentException(
                "Expected integer for parameter '" + pParamName + "' but got : '" + pValue + "'");
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : isBlank
    // Description   : Returns true if the string is null or contains only whitespace
    // ***************************************************************************************************************************************************************************************
    private static boolean isBlank(String pValue)
    {
        return pValue == null || pValue.trim().isEmpty();
    }
}
