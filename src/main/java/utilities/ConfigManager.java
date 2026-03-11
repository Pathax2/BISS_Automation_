// ===================================================================================================================================
// File          : ConfigManager.java
// Package       : utilities
// Description   : Centralised configuration manager for the BISS automation framework.
//                 Loads a base application.properties file and overlays an environment-specific
//                 profile file (application-dev.properties, application-staging.properties, etc.)
//                 on top of it. Environment is resolved via -Denv=<profile> JVM argument.
//
// Folder        : src/main/java/utilities/ConfigManager.java
//
// Usage         :
//   ConfigManager.get("app.url")
//   ConfigManager.getInt("explicit.wait.seconds", 20)
//   ConfigManager.getBool("headless", false)
//
// Profile resolution order (highest wins):
//   1. JVM -D system properties  (-Dapp.url=https://override.ie)
//   2. application-<env>.properties
//   3. application.properties (base defaults)
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigManager
{
    private static final Logger log = Logger.getLogger(ConfigManager.class.getName());

    // Base config directory inside test resources
    private static final String iConfigBasePath   = "src/test/resources/config/";
    private static final String iBaseFileName     = "application.properties";

    // Resolved properties store — merged base + profile + JVM overrides
    private static final Properties iProperties = new Properties();

    // Active environment profile — resolved from -Denv=staging (defaults to "dev")
    private static final String iActiveProfile = System.getProperty("env", "dev").trim().toLowerCase();

    // Load on class initialisation — runs once for the entire JVM lifetime
    static
    {
        loadBaseProperties();
        loadProfileProperties();
        log.info("[ConfigManager] Loaded | Profile='" + iActiveProfile
                + "' | Total keys=" + iProperties.size());
    }

    // -------------------------------------------------------------------------------------------------------------------------------
    // Private constructor — static utility class, not instantiable
    // -------------------------------------------------------------------------------------------------------------------------------
    private ConfigManager() {}

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadBaseProperties
    // Description   : Loads the base application.properties file containing framework-level defaults
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void loadBaseProperties()
    {
        String iPath = iConfigBasePath + iBaseFileName;

        try (InputStream iStream = new FileInputStream(iPath))
        {
            iProperties.load(iStream);
            log.info("[ConfigManager] Base config loaded from : " + iPath);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("[ConfigManager] Failed to load base config from : '"
                    + iPath + "' | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : loadProfileProperties
    // Description   : Loads the environment-specific profile properties and overlays them onto the base.
    //                 Missing profile file is treated as a warning, not a fatal error.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private static void loadProfileProperties()
    {
        String iProfileFile = "application-" + iActiveProfile + ".properties";
        String iPath        = iConfigBasePath + iProfileFile;

        try (InputStream iStream = new FileInputStream(iPath))
        {
            Properties iProfileProps = new Properties();
            iProfileProps.load(iStream);

            // Overlay profile values on top of base — profile values win
            iProperties.putAll(iProfileProps);
            log.info("[ConfigManager] Profile config loaded from : " + iPath
                    + " | Keys overridden=" + iProfileProps.size());
        }
        catch (Exception iException)
        {
            log.warning("[ConfigManager] Profile config not found for env='" + iActiveProfile
                    + "'. Using base defaults only. Path attempted: " + iPath);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : get
    // Description   : Returns a String config value. JVM -D system properties take highest priority.
    // Parameters    : pKey (String) - property key
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Key must exist in loaded properties or as a JVM system property
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String get(String pKey)
    {
        if (pKey == null || pKey.trim().isEmpty())
        {
            throw new RuntimeException("[ConfigManager] Key cannot be null or blank.");
        }

        // JVM system property overrides everything (for Bamboo plan variables)
        String iJvmValue = System.getProperty(pKey.trim());
        if (iJvmValue != null && !iJvmValue.trim().isEmpty())
        {
            return iJvmValue.trim();
        }

        String iValue = iProperties.getProperty(pKey.trim());

        if (iValue == null || iValue.trim().isEmpty())
        {
            throw new RuntimeException("[ConfigManager] Key not found in config : '" + pKey
                    + "' | Active profile : '" + iActiveProfile + "'");
        }

        return iValue.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getOrDefault
    // Description   : Returns a String config value, returning the supplied default if key is absent
    // Parameters    : pKey     (String) - property key
    //                 pDefault (String) - fallback value if key not found
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getOrDefault(String pKey, String pDefault)
    {
        try
        {
            return get(pKey);
        }
        catch (Exception iException)
        {
            return pDefault;
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getInt
    // Description   : Returns an integer config value
    // Parameters    : pKey     (String) - property key
    //                 pDefault (int)    - fallback if key absent or unparseable
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static int getInt(String pKey, int pDefault)
    {
        try
        {
            return Integer.parseInt(get(pKey));
        }
        catch (Exception iException)
        {
            log.warning("[ConfigManager] Could not parse int for key '" + pKey + "'. Using default=" + pDefault);
            return pDefault;
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getBool
    // Description   : Returns a boolean config value
    // Parameters    : pKey     (String)  - property key
    //                 pDefault (boolean) - fallback if key absent
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static boolean getBool(String pKey, boolean pDefault)
    {
        try
        {
            return Boolean.parseBoolean(get(pKey));
        }
        catch (Exception iException)
        {
            return pDefault;
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getLong
    // Description   : Returns a long config value
    // Parameters    : pKey     (String) - property key
    //                 pDefault (long)   - fallback if key absent or unparseable
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static long getLong(String pKey, long pDefault)
    {
        try
        {
            return Long.parseLong(get(pKey));
        }
        catch (Exception iException)
        {
            log.warning("[ConfigManager] Could not parse long for key '" + pKey + "'. Using default=" + pDefault);
            return pDefault;
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getActiveProfile
    // Description   : Returns the currently active environment profile name (e.g. "staging")
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getActiveProfile()
    {
        return iActiveProfile;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : printAll
    // Description   : Prints all loaded config keys and values to console. Masks sensitive keys.
    //                 Useful for debugging Bamboo agent config issues.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void printAll()
    {
        log.info("[ConfigManager] ===== Active Configuration (Profile: " + iActiveProfile + ") =====");
        iProperties.stringPropertyNames().stream().sorted().forEach(iKey ->
        {
            String iValue = iKey.toLowerCase().contains("password")
                    || iKey.toLowerCase().contains("secret")
                    || iKey.toLowerCase().contains("token")
                    ? "****MASKED****"
                    : iProperties.getProperty(iKey);

            log.info("[ConfigManager]   " + iKey + " = " + iValue);
        });
    }
}