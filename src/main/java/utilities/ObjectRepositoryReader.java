package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ObjectRepositoryReader
{
    private static final Properties iProperties = new Properties();
    private static final String iObjectRepositoryPath = "src/test/resources/ObjectRepository/ObjectRepository.properties";

    static
    {
        try (InputStream iInputStream = new FileInputStream(iObjectRepositoryPath))
        {
            iProperties.load(iInputStream);
        }
        catch (Exception iException)
        {
            throw new RuntimeException("Unable to load Object Repository : " + iException.getMessage(), iException);
        }
    }

    // ***************************************************************************************************************************************************************************************
    // FunctionS Name : getLocatorValue
    // Description   : Returns locator value from Object Repository based on the supplied key
    // Parameters    : iKey (String) - locator key from ObjectRepository.properties
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : ObjectRepository.properties should be present and loaded
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getLocatorValue(String iKey)
    {
        String iLocatorValue = iProperties.getProperty(iKey);

        if (iLocatorValue == null || iLocatorValue.trim().isEmpty())
        {
            throw new RuntimeException("Locator value not found in Object Repository for key : " + iKey);
        }

        return iLocatorValue.trim();
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : getLocatorType
    // Description   : Derives locator strategy from locator key suffix
    // Parameters    : iKey (String) - locator key from ObjectRepository.properties
    // Author        : Aniket Pathare | aniket.pathare
    // Precondition  : Locator key should end with supported locator suffix like .id, .xpath, .css
    // Date Created  : 09-03-2026
    // ***************************************************************************************************************************************************************************************
    public static String getLocatorType(String iKey)
    {
        String iLowerKey = iKey.toLowerCase().trim();

        if (iLowerKey.endsWith(".id"))
        {
            return "ID";
        }
        else if (iLowerKey.endsWith(".name"))
        {
            return "NAME";
        }
        else if (iLowerKey.endsWith(".xpath"))
        {
            return "XPATH";
        }
        else if (iLowerKey.endsWith(".css"))
        {
            return "CSS";
        }
        else if (iLowerKey.endsWith(".cssselector"))
        {
            return "CSSSELECTOR";
        }
        else if (iLowerKey.endsWith(".classname"))
        {
            return "CLASSNAME";
        }
        else if (iLowerKey.endsWith(".tagname"))
        {
            return "TAGNAME";
        }
        else if (iLowerKey.endsWith(".linktext"))
        {
            return "LINKTEXT";
        }
        else if (iLowerKey.endsWith(".partiallinktext"))
        {
            return "PARTIALLINKTEXT";
        }

        throw new RuntimeException("Unsupported locator type for key : " + iKey);
    }
}