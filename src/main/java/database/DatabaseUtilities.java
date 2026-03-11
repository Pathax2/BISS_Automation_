// ===================================================================================================================================
// File          : DatabaseUtilities.java
// Package       : database
// Description   : Database verification layer for the BISS automation framework.
//                 Provides JDBC-based query execution and assertion methods so UI test results
//                 can be cross-validated against the database to confirm data was saved correctly.
//
//                 Supports Oracle, PostgreSQL, and MSSQL via standard JDBC.
//                 Connection pooling via HikariCP (add dependency to pom.xml).
//
//                 pom.xml dependency:
//                   <dependency>
//                     <groupId>com.zaxxer</groupId>
//                     <artifactId>HikariCP</artifactId>
//                     <version>5.1.0</version>
//                   </dependency>
//
// Folder        : src/main/java/database/DatabaseUtilities.java
//
// Config keys (application.properties / profile overrides):
//   db.enabled=true
//   db.driver=oracle.jdbc.OracleDriver
//   db.url=jdbc:oracle:thin:@host:1521:SID
//   db.username=biss_user
//   db.password → set ONLY via Bamboo plan variable (-Ddb.password=xxx), NEVER in properties files
//   db.connection.pool.size=5
//   db.query.timeout.seconds=30
//
// Usage in Step Definitions:
//   DatabaseUtilities.verifyCellValue("APPLICATIONS", "APP_ID", appId, "STATUS", "SUBMITTED");
//   String iStatus = DatabaseUtilities.querySingleValue("SELECT STATUS FROM APPS WHERE ID=?", appId);
//   int    iCount  = DatabaseUtilities.queryCount("SELECT COUNT(*) FROM APPS WHERE USER_ID=?", userId);
//   DatabaseUtilities.verifyRecordExists("SELECT * FROM APPS WHERE REF_NO=?", refNo);
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import utilities.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseUtilities
{
    private static final Logger log = Logger.getLogger(DatabaseUtilities.class.getName());

    private static HikariDataSource iDataSource = null;

    private static final boolean  iDbEnabled     = ConfigManager.getBool("db.enabled", false);
    private static final int      iQueryTimeout  = ConfigManager.getInt("db.query.timeout.seconds", 30);

    // -------------------------------------------------------------------------------------------------------------------------------
    // Initialise connection pool on first use (lazy — only if db.enabled=true)
    // -------------------------------------------------------------------------------------------------------------------------------
    private static synchronized void ensureInitialised()
    {
        if (iDataSource != null && !iDataSource.isClosed()) { return; }

        if (!iDbEnabled)
        {
            throw new RuntimeException("[DatabaseUtilities] db.enabled=false. "
                    + "Enable database support in application.properties before calling DB methods.");
        }

        String iDriver   = ConfigManager.get("db.driver");
        String iUrl      = ConfigManager.get("db.url");
        String iUsername = ConfigManager.get("db.username");
        String iPassword = System.getProperty("db.password", "");

        if (iPassword.isEmpty())
        {
            throw new RuntimeException("[DatabaseUtilities] db.password not set. "
                    + "Pass via Bamboo plan variable: -Ddb.password=<value>. Never store in properties files.");
        }

        int iPoolSize = ConfigManager.getInt("db.connection.pool.size", 5);

        try
        {
            Class.forName(iDriver);

            HikariConfig iConfig = new HikariConfig();
            iConfig.setJdbcUrl(iUrl);
            iConfig.setUsername(iUsername);
            iConfig.setPassword(iPassword);
            iConfig.setDriverClassName(iDriver);
            iConfig.setMaximumPoolSize(iPoolSize);
            iConfig.setConnectionTimeout(30_000);
            iConfig.setIdleTimeout(600_000);
            iConfig.setMaxLifetime(1_800_000);
            iConfig.setPoolName("BISS-DB-Pool");

            iDataSource = new HikariDataSource(iConfig);
            log.info("[DatabaseUtilities] Connection pool initialised | URL=" + iUrl + " | PoolSize=" + iPoolSize);
        }
        catch (ClassNotFoundException iException)
        {
            throw new RuntimeException("[DatabaseUtilities] JDBC driver not found : '" + iDriver
                    + "'. Add the driver JAR to pom.xml. " + iException.getMessage(), iException);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[DatabaseUtilities] Pool initialisation failed : "
                    + iException.getMessage(), iException);
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor
    // -------------------------------------------------------------------------------------------------------------------------------
    private DatabaseUtilities() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : querySingleValue
    // Description   : Executes a SELECT query and returns the value of the first column of the first row.
    //                 Throws if query returns no rows.
    // Parameters    : pSql    (String)    - parameterised SQL query (use ? for parameters)
    //                 pParams (Object...) - parameter values to bind in order
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : db.enabled=true and db.password set via JVM property
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String querySingleValue(String pSql, Object... pParams)
    {
        ensureInitialised();

        try (Connection iConn = iDataSource.getConnection();
             PreparedStatement iStmt = prepareStatement(iConn, pSql, pParams);
             ResultSet iRs = iStmt.executeQuery())
        {
            if (!iRs.next())
            {
                throw new RuntimeException("[DatabaseUtilities] Query returned no rows. SQL : " + pSql);
            }

            String iResult = iRs.getString(1);
            log.info("[DatabaseUtilities] querySingleValue result : '" + iResult + "' | SQL : " + pSql);
            return iResult == null ? "" : iResult.trim();
        }
        catch (RuntimeException iRe)
        {
            throw iRe;
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[DatabaseUtilities] querySingleValue failed : "
                    + iException.getMessage() + " | SQL : " + pSql, iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : queryCount
    // Description   : Executes a COUNT query and returns the integer result.
    //                 Expects the first column of the first row to be a numeric count.
    // Parameters    : pSql    (String)    - parameterised SQL (e.g. "SELECT COUNT(*) FROM TABLE WHERE...")
    //                 pParams (Object...) - parameter values
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int queryCount(String pSql, Object... pParams)
    {
        try
        {
            String iResult = querySingleValue(pSql, pParams);
            int    iCount  = Integer.parseInt(iResult);
            log.info("[DatabaseUtilities] queryCount result : " + iCount);
            return iCount;
        }
        catch (NumberFormatException iException)
        {
            throw new RuntimeException("[DatabaseUtilities] queryCount returned non-numeric value. SQL : " + pSql);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : queryRows
    // Description   : Executes a SELECT and returns all rows as a list of column-name → value maps.
    //                 Column names are lowercased for consistent map key access.
    // Parameters    : pSql    (String)    - parameterised SQL
    //                 pParams (Object...) - parameter values
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static List<Map<String, String>> queryRows(String pSql, Object... pParams)
    {
        ensureInitialised();
        List<Map<String, String>> iRows = new ArrayList<>();

        try (Connection iConn = iDataSource.getConnection();
             PreparedStatement iStmt = prepareStatement(iConn, pSql, pParams);
             ResultSet iRs = iStmt.executeQuery())
        {
            ResultSetMetaData iMeta     = iRs.getMetaData();
            int               iColCount = iMeta.getColumnCount();

            while (iRs.next())
            {
                Map<String, String> iRow = new LinkedHashMap<>();
                for (int i = 1; i <= iColCount; i++)
                {
                    String iColName = iMeta.getColumnLabel(i).toLowerCase();
                    String iValue   = iRs.getString(i);
                    iRow.put(iColName, iValue == null ? "" : iValue.trim());
                }
                iRows.add(iRow);
            }

            log.info("[DatabaseUtilities] queryRows returned " + iRows.size() + " row(s) | SQL : " + pSql);
            return iRows;
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[DatabaseUtilities] queryRows failed : "
                    + iException.getMessage() + " | SQL : " + pSql, iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyCellValue
    // Description   : Verifies a specific column value in a table row identified by a key column/value pair.
    //                 The most common DB verification pattern in BDD step definitions.
    // Parameters    : pTable          (String) - table name
    //                 pKeyColumn      (String) - column to identify the row (e.g. "APP_ID")
    //                 pKeyValue       (String) - value of the key column
    //                 pTargetColumn   (String) - column whose value you want to verify
    //                 pExpectedValue  (String) - expected value (case-insensitive comparison)
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void verifyCellValue(
            String pTable,
            String pKeyColumn,
            String pKeyValue,
            String pTargetColumn,
            String pExpectedValue)
    {
        String iSql    = "SELECT " + pTargetColumn + " FROM " + pTable
                + " WHERE " + pKeyColumn + " = ?";
        String iActual = querySingleValue(iSql, pKeyValue);

        if (!iActual.equalsIgnoreCase(pExpectedValue.trim()))
        {
            throw new AssertionError("[DatabaseUtilities] DB Verification FAILED"
                    + "\n  Table    : " + pTable
                    + "\n  Row key  : " + pKeyColumn + " = '" + pKeyValue + "'"
                    + "\n  Column   : " + pTargetColumn
                    + "\n  Expected : '" + pExpectedValue + "'"
                    + "\n  Actual   : '" + iActual + "'");
        }

        log.info("[DatabaseUtilities] DB Verification PASSED | " + pTable + "." + pTargetColumn
                + " = '" + iActual + "' for " + pKeyColumn + "='" + pKeyValue + "'");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyRecordExists
    // Description   : Verifies that at least one record is returned by the given query.
    //                 Throws AssertionError if no records found.
    // Parameters    : pSql    (String)    - parameterised SELECT SQL
    //                 pParams (Object...) - parameter values
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void verifyRecordExists(String pSql, Object... pParams)
    {
        int iCount = queryCount("SELECT COUNT(*) FROM (" + pSql + ")", pParams);

        if (iCount == 0)
        {
            throw new AssertionError("[DatabaseUtilities] verifyRecordExists FAILED — no records returned."
                    + " SQL : " + pSql);
        }

        log.info("[DatabaseUtilities] verifyRecordExists PASSED — " + iCount + " record(s) found.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : verifyRecordNotExists
    // Description   : Verifies that the given query returns zero records.
    //                 Throws AssertionError if any records found (used to confirm deletion).
    // Parameters    : pSql    (String)    - parameterised SELECT SQL
    //                 pParams (Object...) - parameter values
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void verifyRecordNotExists(String pSql, Object... pParams)
    {
        int iCount = queryCount("SELECT COUNT(*) FROM (" + pSql + ")", pParams);

        if (iCount > 0)
        {
            throw new AssertionError("[DatabaseUtilities] verifyRecordNotExists FAILED — "
                    + iCount + " unexpected record(s) found. SQL : " + pSql);
        }

        log.info("[DatabaseUtilities] verifyRecordNotExists PASSED — zero records confirmed.");
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : executeUpdate
    // Description   : Executes a non-query SQL statement (INSERT, UPDATE, DELETE).
    //                 Returns number of rows affected.
    //                 USE WITH CAUTION — only for test data setup/teardown, never for application logic.
    // Parameters    : pSql    (String)    - parameterised DML SQL
    //                 pParams (Object...) - parameter values
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int executeUpdate(String pSql, Object... pParams)
    {
        ensureInitialised();

        try (Connection iConn = iDataSource.getConnection();
             PreparedStatement iStmt = prepareStatement(iConn, pSql, pParams))
        {
            int iRowsAffected = iStmt.executeUpdate();
            log.info("[DatabaseUtilities] executeUpdate affected " + iRowsAffected + " row(s) | SQL : " + pSql);
            return iRowsAffected;
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[DatabaseUtilities] executeUpdate failed : "
                    + iException.getMessage() + " | SQL : " + pSql, iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : closePool
    // Description   : Shuts down the connection pool. Call from Hooks @AfterAll if db.enabled=true.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void closePool()
    {
        if (iDataSource != null && !iDataSource.isClosed())
        {
            iDataSource.close();
            log.info("[DatabaseUtilities] Connection pool closed.");
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------------------------------------------------------------

    private static PreparedStatement prepareStatement(Connection pConn, String pSql, Object... pParams)
            throws Exception
    {
        PreparedStatement iStmt = pConn.prepareStatement(pSql);
        iStmt.setQueryTimeout(iQueryTimeout);

        if (pParams != null)
        {
            for (int i = 0; i < pParams.length; i++)
            {
                iStmt.setObject(i + 1, pParams[i]);
            }
        }

        return iStmt;
    }
}