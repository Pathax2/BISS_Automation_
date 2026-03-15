package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

/**
 * ***************************************************************************************************************************************************************************************
 * File Name    : ObjectRepositoryExtractor.java
 * Description  : Extracts useful automation elements from the current runtime page and writes them into a single properties file
 *                grouped by Page and then by Category.
 * Author       : Aniket Pathare
 * Date Modified: 14-03-2026
 * ***************************************************************************************************************************************************************************************
 */
public class ObjectRepositoryExtractor
{
    private final WebDriver iDriver;

    public ObjectRepositoryExtractor(WebDriver pDriver)
    {
        this.iDriver = pDriver;
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : extractPageObjects
    // Description   : Extracts all useful objects from the current page and appends them into a single properties file grouped by page and category
    // Parameters    : pPageName (String) - Logical page name like Home, MyClients, FarmerDashboard
    //                 pOutputFilePath (String) - Properties file path
    // Author        : Aniket Pathare
    // Precondition  : Target page must already be opened
    // Date Created  : 14-03-2026
    // ***************************************************************************************************************************************************************************************
    public void extractPageObjects(String pPageName, String pOutputFilePath)
    {
        Map<String, List<LocatorEntry>> iCategorizedEntries = new LinkedHashMap<>();
        Map<String, Integer> iDuplicateCounterMap = new HashMap<>();

        iCategorizedEntries.put("Buttons", new ArrayList<>());
        iCategorizedEntries.put("Links", new ArrayList<>());
        iCategorizedEntries.put("Textboxes", new ArrayList<>());
        iCategorizedEntries.put("TextAreas", new ArrayList<>());
        iCategorizedEntries.put("Dropdowns", new ArrayList<>());
        iCategorizedEntries.put("Checkboxes", new ArrayList<>());
        iCategorizedEntries.put("RadioButtons", new ArrayList<>());
        iCategorizedEntries.put("Images", new ArrayList<>());
        iCategorizedEntries.put("Labels", new ArrayList<>());
        iCategorizedEntries.put("Tables", new ArrayList<>());
        iCategorizedEntries.put("Icons", new ArrayList<>());
        iCategorizedEntries.put("OtherElements", new ArrayList<>());

        List<WebElement> iElements = getAllUsefulElements();

        for (WebElement iElement : iElements)
        {
            try
            {
                if (!iElement.isDisplayed())
                {
                    continue;
                }

                String iCategory = getCategory(iElement);
                String iBaseKey = buildRepositoryKey(pPageName, iElement);

                if (iBaseKey == null || iBaseKey.trim().isEmpty())
                {
                    continue;
                }

                String iFinalKey = makeUnique(iBaseKey, iDuplicateCounterMap);
                String iXPath = buildXPath(iElement);
                String iCss = buildCss(iElement);

                iCategorizedEntries.get(iCategory).add(new LocatorEntry(iFinalKey, iXPath, iCss));
            }
            catch (Exception iException)
            {
                System.out.println("Skipping element due to error: " + iException.getMessage());
            }
        }

        writeToPropertiesFile(pPageName, iCategorizedEntries, pOutputFilePath);
    }

    private List<WebElement> getAllUsefulElements()
    {
        return iDriver.findElements(By.xpath(
                "//button | //a | //input | //textarea | //select | //img | //label | //table | //mat-select | //mat-checkbox | //mat-radio-button | //mat-icon | //*[@role='button'] | //*[@role='link'] | //*[@role='textbox'] | //*[@role='checkbox'] | //*[@role='radio'] | //*[@role='combobox']"
        ));
    }

    private String getCategory(WebElement pElement)
    {
        String iTag = lower(pElement.getTagName());
        String iType = lower(pElement.getAttribute("type"));

        if ("button".equals(iTag) || "submit".equals(iType) || "button".equals(iType) || hasRole(pElement, "button"))
        {
            return "Buttons";
        }
        else if ("a".equals(iTag) || hasRole(pElement, "link"))
        {
            return "Links";
        }
        else if ("input".equals(iTag) && Arrays.asList("text", "password", "email", "number", "search", "tel", "url", "").contains(iType))
        {
            return "Textboxes";
        }
        else if ("textarea".equals(iTag) || hasRole(pElement, "textbox"))
        {
            return "TextAreas";
        }
        else if ("select".equals(iTag) || "mat-select".equals(iTag) || hasRole(pElement, "combobox"))
        {
            return "Dropdowns";
        }
        else if (("input".equals(iTag) && "checkbox".equals(iType)) || "mat-checkbox".equals(iTag) || hasRole(pElement, "checkbox"))
        {
            return "Checkboxes";
        }
        else if (("input".equals(iTag) && "radio".equals(iType)) || "mat-radio-button".equals(iTag) || hasRole(pElement, "radio"))
        {
            return "RadioButtons";
        }
        else if ("img".equals(iTag))
        {
            return "Images";
        }
        else if ("label".equals(iTag))
        {
            return "Labels";
        }
        else if ("table".equals(iTag))
        {
            return "Tables";
        }
        else if ("mat-icon".equals(iTag))
        {
            return "Icons";
        }

        return "OtherElements";
    }

    private String buildRepositoryKey(String pPageName, WebElement pElement)
    {
        String iTag = lower(pElement.getTagName());
        String iType = lower(pElement.getAttribute("type"));
        String iNamePart = bestName(pElement);

        if (iNamePart == null || iNamePart.isBlank())
        {
            iNamePart = "Element";
        }

        iNamePart = toPascalCase(iNamePart);
        String iPagePrefix = toPascalCase(pPageName);

        if ("button".equals(iTag) || "submit".equals(iType) || "button".equals(iType) || hasRole(pElement, "button"))
        {
            return "iBtn_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("a".equals(iTag) || hasRole(pElement, "link"))
        {
            return "iLnk_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("input".equals(iTag) && Arrays.asList("text", "password", "email", "number", "search", "tel", "url", "").contains(iType))
        {
            return "iTxt_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("textarea".equals(iTag) || hasRole(pElement, "textbox"))
        {
            return "iTxtArea_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("select".equals(iTag) || "mat-select".equals(iTag) || hasRole(pElement, "combobox"))
        {
            return "iDrp_" + iPagePrefix + "_" + iNamePart;
        }
        else if (("input".equals(iTag) && "checkbox".equals(iType)) || "mat-checkbox".equals(iTag) || hasRole(pElement, "checkbox"))
        {
            return "iChk_" + iPagePrefix + "_" + iNamePart;
        }
        else if (("input".equals(iTag) && "radio".equals(iType)) || "mat-radio-button".equals(iTag) || hasRole(pElement, "radio"))
        {
            return "iRdo_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("img".equals(iTag))
        {
            return "iImg_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("label".equals(iTag))
        {
            return "iLbl_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("table".equals(iTag))
        {
            return "iTbl_" + iPagePrefix + "_" + iNamePart;
        }
        else if ("mat-icon".equals(iTag))
        {
            return "iIcn_" + iPagePrefix + "_" + iNamePart;
        }

        return "iElm_" + iPagePrefix + "_" + iNamePart;
    }

    private String bestName(WebElement pElement)
    {
        List<String> iCandidates = Arrays.asList(
                trimToNull(pElement.getText()),
                trimToNull(pElement.getAttribute("id")),
                trimToNull(pElement.getAttribute("name")),
                trimToNull(pElement.getAttribute("placeholder")),
                trimToNull(pElement.getAttribute("aria-label")),
                trimToNull(pElement.getAttribute("title")),
                trimToNull(pElement.getAttribute("alt")),
                trimToNull(pElement.getAttribute("value")),
                trimToNull(pElement.getAttribute("formcontrolname"))
        );

        for (String iCandidate : iCandidates)
        {
            if (iCandidate != null && !iCandidate.isBlank())
            {
                return sanitize(iCandidate);
            }
        }

        return "Element";
    }

    private String buildXPath(WebElement pElement)
    {
        String iTag = pElement.getTagName();

        if (trimToNull(pElement.getAttribute("id")) != null)
        {
            return "//*[@" + "id='" + pElement.getAttribute("id") + "']";
        }

        if (trimToNull(pElement.getAttribute("name")) != null)
        {
            return "//" + iTag + "[@name='" + pElement.getAttribute("name") + "']";
        }

        if (trimToNull(pElement.getAttribute("formcontrolname")) != null)
        {
            return "//" + iTag + "[@formcontrolname='" + pElement.getAttribute("formcontrolname") + "']";
        }

        if (trimToNull(pElement.getAttribute("placeholder")) != null)
        {
            return "//" + iTag + "[@placeholder='" + pElement.getAttribute("placeholder") + "']";
        }

        if (trimToNull(pElement.getAttribute("aria-label")) != null)
        {
            return "//" + iTag + "[@aria-label='" + pElement.getAttribute("aria-label") + "']";
        }

        if (trimToNull(pElement.getText()) != null)
        {
            return "//" + iTag + "[contains(normalize-space(.),'" + pElement.getText().trim() + "')]";
        }

        return "//" + iTag;
    }

    private String buildCss(WebElement pElement)
    {
        String iTag = lower(pElement.getTagName());

        if (trimToNull(pElement.getAttribute("id")) != null)
        {
            return "#" + pElement.getAttribute("id");
        }

        if (trimToNull(pElement.getAttribute("name")) != null)
        {
            return iTag + "[name='" + pElement.getAttribute("name") + "']";
        }

        if (trimToNull(pElement.getAttribute("formcontrolname")) != null)
        {
            return iTag + "[formcontrolname='" + pElement.getAttribute("formcontrolname") + "']";
        }

        if (trimToNull(pElement.getAttribute("placeholder")) != null)
        {
            return iTag + "[placeholder='" + pElement.getAttribute("placeholder") + "']";
        }

        if (trimToNull(pElement.getAttribute("aria-label")) != null)
        {
            return iTag + "[aria-label='" + pElement.getAttribute("aria-label") + "']";
        }

        if (trimToNull(pElement.getAttribute("class")) != null)
        {
            String[] iClasses = pElement.getAttribute("class").trim().split("\\s+");
            if (iClasses.length > 0)
            {
                return iTag + "." + iClasses[0];
            }
        }

        return iTag;
    }

    private void writeToPropertiesFile(String pPageName, Map<String, List<LocatorEntry>> pCategorizedEntries, String pOutputFilePath)
    {
        try
        {
            File iFile = new File(pOutputFilePath);
            boolean iFileExists = iFile.exists();

            BufferedWriter iWriter = new BufferedWriter(new FileWriter(iFile, true));

            if (!iFileExists)
            {
                iWriter.write("# Object Repository");
                iWriter.newLine();
                iWriter.newLine();
            }

            iWriter.write("# ======================================================================");
            iWriter.newLine();
            iWriter.write("# Page : " + pPageName);
            iWriter.newLine();
            iWriter.write("# ======================================================================");
            iWriter.newLine();
            iWriter.newLine();

            for (Map.Entry<String, List<LocatorEntry>> iEntry : pCategorizedEntries.entrySet())
            {
                if (iEntry.getValue().isEmpty())
                {
                    continue;
                }

                iWriter.write("# ------------------------- " + iEntry.getKey() + " -------------------------");
                iWriter.newLine();

                for (LocatorEntry iLocatorEntry : iEntry.getValue())
                {
                    iWriter.write(iLocatorEntry.iKey + ".xpath=" + iLocatorEntry.iXPath);
                    iWriter.newLine();
                    iWriter.write(iLocatorEntry.iKey + ".css=" + iLocatorEntry.iCss);
                    iWriter.newLine();
                    iWriter.newLine();
                }
            }

            iWriter.close();
            System.out.println("Objects extracted successfully for page: " + pPageName);
        }
        catch (IOException iException)
        {
            throw new RuntimeException("Failed writing properties file", iException);
        }
    }

    private String makeUnique(String pBaseKey, Map<String, Integer> pDuplicateCounterMap)
    {
        if (!pDuplicateCounterMap.containsKey(pBaseKey))
        {
            pDuplicateCounterMap.put(pBaseKey, 1);
            return pBaseKey;
        }

        int iCount = pDuplicateCounterMap.get(pBaseKey) + 1;
        pDuplicateCounterMap.put(pBaseKey, iCount);
        return pBaseKey + "_" + iCount;
    }

    private String sanitize(String pValue)
    {
        return Normalizer.normalize(pValue, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String toPascalCase(String pValue)
    {
        String[] iWords = pValue.split("\\s+");
        StringBuilder iBuilder = new StringBuilder();

        for (String iWord : iWords)
        {
            if (!iWord.isBlank())
            {
                iBuilder.append(iWord.substring(0, 1).toUpperCase(Locale.ENGLISH));
                if (iWord.length() > 1)
                {
                    iBuilder.append(iWord.substring(1));
                }
            }
        }

        return iBuilder.toString();
    }

    private String trimToNull(String pValue)
    {
        if (pValue == null)
        {
            return null;
        }

        String iTrimmed = pValue.trim();
        return iTrimmed.isEmpty() ? null : iTrimmed;
    }

    private String lower(String pValue)
    {
        return pValue == null ? "" : pValue.trim().toLowerCase(Locale.ENGLISH);
    }

    private boolean hasRole(WebElement pElement, String pRole)
    {
        String iRole = trimToNull(pElement.getAttribute("role"));
        return iRole != null && iRole.equalsIgnoreCase(pRole);
    }

    private static class LocatorEntry
    {
        private final String iKey;
        private final String iXPath;
        private final String iCss;

        private LocatorEntry(String pKey, String pXPath, String pCss)
        {
            this.iKey = pKey;
            this.iXPath = pXPath;
            this.iCss = pCss;
        }
    }
}
