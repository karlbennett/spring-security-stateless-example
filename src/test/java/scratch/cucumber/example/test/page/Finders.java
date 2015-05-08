package scratch.cucumber.example.test.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static scratch.cucumber.example.test.page.Bys.byLabel;

public class Finders {

    public static String findIdByLabel(WebDriver driver, String text) {
        return driver.findElement(byLabel(text)).getAttribute("for");
    }

    public static void enterTextByLabel(WebDriver driver, String labelName, String text) {
        driver.findElement(By.id(findIdByLabel(driver, labelName))).sendKeys(text);
    }
}
