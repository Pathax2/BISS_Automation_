// ===================================================================================================================================
// File          : ObjReader.java
// Package       : utilities
// Description   : Loads and reads the Object Repository properties file for the BISS automation framework.
//                 Provides locator value and locator type resolution by key suffix convention.
//                 File path is overridable via JVM system property for CI/CD flexibility.
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class ObjReader
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // Logger
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Logger log = Logger.getLogger(ObjReader.class.getName());
    public static Set<String> getAllKeys() {return iProperties.stringPropertyNames();}
    // -------------------------------------------------------------------------------------------------------------------------------
    // Object Repository path — overridable via -Dobject.repository.path=... for CI environments
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String iObjectRepositoryPath = System.getProperty(
            "object.repository.path",
            "src/test/resources/Object_Repository/ObjectRepository.properties"
    );

    // -------------------------------------------------------------------------------------------------------------------------------
    // Properties store — loaded once at class initialisation via static block
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final Properties iProperties = new Properties();

    static
    {
        try (InputStream iInputStream = new FileInputStream(iObjectRepositoryPath))
        {
            iProperties.load(iInputStream);
            log.info("Object Repository loaded successfully from : " + iObjectRepositoryPath
                    + " | Total keys : " + iProperties.size());
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load Object Repository from : '" + iObjectRepositoryPath
                    + "' | Reason : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getLocatorValue
    // Description   : Returns the locator value (e.g. XPath expression, CSS selector) for the supplied key.
    //                 Key must exist in ObjectRepository.properties and must not be blank.
    // Parameters    : pKey (String) - property key from ObjectRepository.properties
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : ObjectRepository.properties must be loaded and key must exist
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getLocator(String pKey)
    {
        if (pKey == null || pKey.trim().isEmpty())
        {
            throw new RuntimeException("ObjReader.getLocatorValue: key cannot be null or blank.");
        }

        String iLocatorValue = iProperties.getProperty(pKey.trim());

        if (iLocatorValue == null || iLocatorValue.trim().isEmpty())
        {
            throw new RuntimeException("Locator not found in Object Repository for key : '" + pKey
                    + "'. Verify the key exists in : " + iObjectRepositoryPath);
        }

        return iLocatorValue.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getLocatorType
    // Description   : Derives the Selenium locator strategy from the key suffix convention.
    //                 Keys in the Object Repository must follow the naming pattern: iElementName.locatortype
    //                 Supported suffixes: .id | .name | .xpath | .css | .cssselector | .classname | .tagname |
    //                                     .linktext | .partiallinktext
    //                 Keys WITHOUT a suffix are treated as XPATH by default (backward compatible with current OR).
    // Parameters    : pKey (String) - property key from ObjectRepository.properties
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Precondition  : Key must not be null or blank
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getLocatorType(String pKey)
    {
        if (pKey == null || pKey.trim().isEmpty())
        {
            throw new RuntimeException("ObjReader.getLocatorType: key cannot be null or blank.");
        }

        String iLowerKey = pKey.toLowerCase().trim();

        if (iLowerKey.endsWith(".id"))             { return "ID"; }
        if (iLowerKey.endsWith(".name"))           { return "NAME"; }
        if (iLowerKey.endsWith(".xpath"))          { return "XPATH"; }
        if (iLowerKey.endsWith(".css"))            { return "CSS"; }
        if (iLowerKey.endsWith(".cssselector"))    { return "CSSSELECTOR"; }
        if (iLowerKey.endsWith(".classname"))      { return "CLASSNAME"; }
        if (iLowerKey.endsWith(".tagname"))        { return "TAGNAME"; }
        if (iLowerKey.endsWith(".linktext"))       { return "LINKTEXT"; }
        if (iLowerKey.endsWith(".partiallinktext") ){ return "PARTIALLINKTEXT"; }

        // -------------------------------------------------------------------------------------------------------------------------------
        // Default fallback: current ObjectRepository.properties uses bare keys (no suffix).
        // Treat these as XPATH since all existing locators are XPath expressions.
        // Update keys to use suffix convention (e.g. iLoginbtn.xpath) to make locator type explicit.
        // -------------------------------------------------------------------------------------------------------------------------------
        log.warning("ObjReader: No locator suffix found for key '" + pKey + "'. Defaulting to XPATH.");
        return "XPATH";
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : containsKey
    // Description   : Returns true if the supplied key exists in the Object Repository
    // Parameters    : pKey (String) - property key to check
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static boolean containsKey(String pKey)
    {
        if (pKey == null || pKey.trim().isEmpty())
        {
            return false;
        }

        return iProperties.containsKey(pKey.trim());
    }

}
