
package runner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utilities.ObjectRepositoryExtractor;

import static commonFunctions.CommonFunctions.log;

public class ExtractionRunner
{
    public static void main(String[] args)
    {
        WebDriver driver = new ChromeDriver();
        ObjectRepositoryExtractor iObjectRepositoryExtractor = new ObjectRepositoryExtractor(driver);

        driver.get("https://agfood.icentest.agriculture.gov.ie/sso-portal-ui/#/agfood\n");

        log.info("[STEP] Then the agent should land on the BISS Home page");
        iObjectRepositoryExtractor.extractPageObjects("Home", "src/test/resources/ObjectRepository.properties");

        log.info("[STEP] Then the agent should land on the BISS Home page");
        iObjectRepositoryExtractor.extractPageObjects("MyClients", "src/test/resources/ObjectRepository.properties");

        log.info("[STEP] Then the agent should land on the BISS Home page");
        iObjectRepositoryExtractor.extractPageObjects("FarmerDashboard", "src/test/resources/ObjectRepository.properties");

        driver.quit();
    }
}
