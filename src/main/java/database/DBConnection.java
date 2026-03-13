// ===================================================================================================================================
// File          : DBConnectionTest.java
// Package       : database
// Description   : Standalone Oracle DB verification test.
//                 Run this independently — completely separate from Selenium and Cucumber.
//                 Checks connection health, prints DB metadata, and outputs all herd numbers
//                 for a given scheme year to the IntelliJ console and logs/db_execution.log
//
// ═══════════════════════════════════════════════════════════════════
//  HOW TO RUN
// ═══════════════════════════════════════════════════════════════════
//  Right-click DBConnectionTest.java in IntelliJ
//  → Run 'verifyConnectionAndPrintHerdNumbers'
//
//  Output goes to:
//    1. IntelliJ Run console (immediately visible)
//    2. logs/db_execution.log (persistent, rolling 5MB)
//
//  ⚠  Credentials are NOT hardcoded — loaded from db.properties
//     Add db.properties to .gitignore before committing
//
// ═══════════════════════════════════════════════════════════════════
//  db.properties — create at src/test/resources/db.properties
// ═══════════════════════════════════════════════════════════════════
//  db.url      = jdbc:oracle:thin:@//dbconn.agriculture.gov.ie:1532/DEVC.agriculture.gov.ie
//  db.username = DPS_DATA
//  db.password = DPS_DATA
//
// ═══════════════════════════════════════════════════════════════════
//  pom.xml — ojdbc8 dependency required
// ═══════════════════════════════════════════════════════════════════
//  <dependency>
//      <groupId>com.oracle.database.jdbc</groupId>
//      <artifactId>ojdbc8</artifactId>
//      <version>21.9.0.0</version>
//  </dependency>
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Precondition  : ojdbc8.jar on classpath; db.properties at src/test/resources/db.properties
// Date Created  : 07-03-2026
// ===================================================================================================================================

package database;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.*;

public class DBConnection {

    // ── Logger — writes to IntelliJ console AND logs/db_execution.log ────────
    private static final Logger log = Logger.getLogger(DBConnectionTest.class.getName());

    static {
        setupLogger();
    }

    // ── Credentials loaded from file — never hardcoded in source ─────────────
    private static final String PROPERTIES_FILE = "src/test/resources/db.properties";

    // ── Scheme year to query — change this to whichever year you need ─────────
    private static final int SCHEME_YEAR = 2022;

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;


    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyConnectionAndPrintHerdNumbers
    // Description   : JUnit 5 entry point — right-click this method in IntelliJ and run it directly.
    //                 Executes in sequence:
    //                   Step 1 → Load db.properties (credentials, URL)
    //                   Step 2 → Open connection and print Oracle DB metadata
    //                   Step 3 → Call get_hold() to confirm custom function is accessible
    //                   Step 4 → Query tddp_application for all herd numbers for SCHEME_YEAR
    //                   Step 5 → Print each herd number to console and log file
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@government.ie
    // Date Created  : 07-03-2026
    // ***************************************************************************************************************************************************************************************
    @Test
    public void verifyConnectionAndPrintHerdNumbers() {

        printBanner("DB VERIFICATION TEST — STARTING");

        // ── Step 1 : Load credentials from db.properties ─────────────────
        loadDBProperties();

        // ── Step 2 : Test connection and print DB metadata ────────────────
        testConnection();

        // ── Step 3 : Verify get_hold() function is callable ───────────────
        // Replace with any valid herd number from your test data
        verifyGetHoldFunction("K1234567");

        // ── Step 4 & 5 : Query and print all herd numbers ─────────────────
        List<String> iHerdNumbers = fetchAndPrintHerdNumbers(SCHEME_YEAR);

        // ── Final summary ─────────────────────────────────────────────────
        printBanner("DB VERIFICATION TEST — COMPLETE | " + iHerdNumbers.size() + " HERD NUMBER(S) FOUND");
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : loadDBProperties
    // Description   : Reads db.url, db.username, db.password from db.properties file.
    //                 Throws immediately with a clear message if any property is missing.
    // Parameters    : None
    // ***************************************************************************************************************************************************************************************
    private void loadDBProperties() {

        log.info("Loading DB properties from: " + PROPERTIES_FILE);

        Properties iProps = new Properties();

        try (FileInputStream iFis = new FileInputStream(PROPERTIES_FILE)) {
            iProps.load(iFis);

            DB_URL      = iProps.getProperty("db.url");
            DB_USER     = iProps.getProperty("db.username");
            DB_PASSWORD = iProps.getProperty("db.password");

            if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
                throw new RuntimeException(
                        "One or more properties missing in " + PROPERTIES_FILE + "\n" +
                                "Required keys: db.url | db.username | db.password");
            }

            log.info("✔  Properties loaded | URL      : " + DB_URL);
            log.info("                     | Username : " + DB_USER);
            log.info("                     | Password : *** MASKED ***");

        } catch (IOException e) {
            log.severe("✘  Cannot read " + PROPERTIES_FILE + " — " + e.getMessage());
            log.severe("   Create the file at: src/test/resources/db.properties");
            log.severe("   With contents:");
            log.severe("     db.url      = jdbc:oracle:thin:@//dbconn.agriculture.gov.ie:1532/DEVC.agriculture.gov.ie");
            log.severe("     db.username = DPS_DATA");
            log.severe("     db.password = DPS_DATA");
            throw new RuntimeException("db.properties not found at: " + PROPERTIES_FILE, e);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : testConnection
    // Description   : Opens a JDBC connection to Oracle, checks it is valid, and prints
    //                 full DB metadata — product name, version, driver, URL, user.
    //                 This confirms you are connected to the correct database instance.
    // Parameters    : None
    // ***************************************************************************************************************************************************************************************
    private void testConnection() {

        printDivider("STEP 2 — CONNECTION TEST");

        try (Connection iConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            if (iConn.isValid(5)) {
                log.info("✔  Connection Status  : VALID and ACTIVE");
            } else {
                log.warning("⚠  Connection opened but isValid() returned FALSE — check DB health");
            }

            DatabaseMetaData iMeta = iConn.getMetaData();
            log.info("   Database Product  : " + iMeta.getDatabaseProductName());
            log.info("   Database Version  : " + iMeta.getDatabaseProductVersion());
            log.info("   JDBC Driver       : " + iMeta.getDriverName() + " v" + iMeta.getDriverVersion());
            log.info("   Connected URL     : " + iMeta.getURL());
            log.info("   Connected User    : " + iMeta.getUserName());

        } catch (SQLException e) {
            log.severe("✘  CONNECTION FAILED");
            log.severe("   Reason           : " + e.getMessage());
            log.severe("   SQL State        : " + e.getSQLState());
            log.severe("   Oracle Error Code: " + e.getErrorCode());
            log.severe("   Check: VPN connected? DB listener up? Credentials correct?");
            throw new RuntimeException("DB connection failed — cannot continue test.", e);
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyGetHoldFunction
    // Description   : Calls the Oracle get_hold() custom function via JDBC to confirm it is
    //                 accessible under DPS_DATA schema. Prints the returned holding ID.
    //                 If this fails, fetchAndPrintHerdNumbers() will also fail.
    // Parameters    : pHerdNumber (String) — a known herd number from your test data
    // ***************************************************************************************************************************************************************************************
    private void verifyGetHoldFunction(String pHerdNumber) {

        printDivider("STEP 3 — VERIFY get_hold() FUNCTION");

        // Inline call to get_hold() — same as original SQL Developer script
        String iSql = "SELECT get_hold(?) AS holding_id FROM dual";

        try (Connection iConn        = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement iStmt = iConn.prepareStatement(iSql)) {

            iStmt.setString(1, pHerdNumber);

            try (ResultSet iRs = iStmt.executeQuery()) {
                if (iRs.next()) {
                    String iHoldingId = iRs.getString("holding_id");
                    log.info("✔  get_hold('" + pHerdNumber + "') returned : " + iHoldingId);
                } else {
                    log.warning("⚠  get_hold('" + pHerdNumber + "') returned no result — herd number may not exist in DEVC");
                }
            }

        } catch (SQLException e) {
            log.warning("⚠  get_hold() call failed — function may not be accessible: " + e.getMessage());
            log.warning("   SQL State: " + e.getSQLState() + " | Error Code: " + e.getErrorCode());
        }
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : fetchAndPrintHerdNumbers
    // Description   : Queries tddp_application joined to tdco_holding to retrieve all herd numbers
    //                 for the given scheme year. Prints each one to console and log file with index.
    //
    //                 ⚠  COLUMN NAME NOTE:
    //                 Before running, verify the actual herd number column name in SQL Developer:
    //                   SELECT column_name FROM all_tab_columns
    //                   WHERE  table_name  = 'TDCO_HOLDING'
    //                   AND    column_name LIKE '%HERD%';
    //                 Then update hld_herd_no below to match your schema if different.
    //
    // Parameters    : pSchemeYear (int) — scheme year to filter by (e.g. 2022)
    // Returns       : List<String> — all herd numbers found; empty list if none
    // ***************************************************************************************************************************************************************************************
    private List<String> fetchAndPrintHerdNumbers(int pSchemeYear) {

        printDivider("STEP 4 — FETCHING HERD NUMBERS | SCHEME YEAR : " + pSchemeYear);

        List<String> iHerdNumbers = new ArrayList<>();

        // ── UPDATE hld_herd_no if your column name is different ───────────
        // ── Verify with: SELECT column_name FROM all_tab_columns          ──
        // ──              WHERE table_name = 'TDCO_HOLDING'                ──
        // ──              AND column_name LIKE '%HERD%';                   ──
        String iSql =
                "SELECT   h.hld_herd_no " +
                        "FROM     tddp_application a " +
                        "JOIN     tdco_holding     h  ON h.hld_id = a.app_applicant_holding_id " +
                        "WHERE    a.app_syr_code  = ? " +
                        "ORDER BY h.hld_herd_no";

        try (Connection iConn        = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement iStmt = iConn.prepareStatement(iSql)) {

            iStmt.setInt(1, pSchemeYear);

            try (ResultSet iRs = iStmt.executeQuery()) {

                int iCount = 0;

                while (iRs.next()) {
                    String iHerdNo = iRs.getString("hld_herd_no");
                    iHerdNumbers.add(iHerdNo);
                    iCount++;
                    log.info(String.format("   [%04d]  %s", iCount, iHerdNo));
                }

                printDivider("STEP 5 — RESULTS");

                if (iCount == 0) {
                    log.warning("⚠  No herd numbers found for scheme year: " + pSchemeYear);
                    log.warning("   Check: Does tddp_application have rows where app_syr_code = " + pSchemeYear + " ?");
                    log.warning("   Verify in SQL Developer: SELECT COUNT(*) FROM tddp_application WHERE app_syr_code = " + pSchemeYear + ";");
                } else {
                    log.info("✔  Total herd numbers retrieved : " + iCount);
                }
            }

        } catch (SQLException e) {
            log.severe("✘  fetchAndPrintHerdNumbers FAILED | SchemeYear=" + pSchemeYear);
            log.severe("   Reason     : " + e.getMessage());
            log.severe("   SQL State  : " + e.getSQLState());
            log.severe("   Error Code : " + e.getErrorCode());
            log.severe("   Tip: Run this in SQL Developer to debug:");
            log.severe("   SELECT h.hld_herd_no FROM tddp_application a");
            log.severe("   JOIN tdco_holding h ON h.hld_id = a.app_applicant_holding_id");
            log.severe("   WHERE a.app_syr_code = " + pSchemeYear + ";");
        }

        return iHerdNumbers;
    }


    // ***************************************************************************************************************************************************************************************
    // Function Name : setupLogger
    // Description   : Configures Logger to write simultaneously to:
    //                   1. IntelliJ Run console  — visible immediately during execution
    //                   2. logs/db_execution.log — persistent rolling file (5MB, 3 files kept)
    //                 Log format: [yyyy-MM-dd HH:mm:ss] [LEVEL  ] message
    // Parameters    : None
    // ***************************************************************************************************************************************************************************************
    private static void setupLogger() {

        try {
            new java.io.File("logs").mkdirs();

            Logger iLogger = Logger.getLogger(DBConnectionTest.class.getName());
            iLogger.setLevel(Level.ALL);
            iLogger.setUseParentHandlers(false);

            Formatter iFormatter = new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%s] [%-7s] %s%n",
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.getMillis())),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            };

            // ── Console — IntelliJ Run window ─────────────────────────────
            ConsoleHandler iConsoleHandler = new ConsoleHandler();
            iConsoleHandler.setLevel(Level.ALL);
            iConsoleHandler.setFormatter(iFormatter);

            // ── File — logs/db_execution.log (5MB rolling, 3 files) ───────
            FileHandler iFileHandler = new FileHandler(
                    "logs/db_execution.log", 5 * 1024 * 1024, 3, true);
            iFileHandler.setLevel(Level.ALL);
            iFileHandler.setFormatter(iFormatter);

            iLogger.addHandler(iConsoleHandler);
            iLogger.addHandler(iFileHandler);

        } catch (IOException e) {
            System.err.println("Logger setup failed: " + e.getMessage());
        }
    }

    // ── Console formatting helpers ────────────────────────────────────────────
    private void printBanner(String pMessage) {
        log.info("╔══════════════════════════════════════════════════════════════════╗");
        log.info("║  " + pMessage);
        log.info("╚══════════════════════════════════════════════════════════════════╝");
    }

    private void printDivider(String pLabel) {
        log.info("──────────────────────────────────────────────────────────────────");
        log.info("  " + pLabel);
        log.info("──────────────────────────────────────────────────────────────────");
    }
}
