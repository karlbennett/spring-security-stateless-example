package scratch.cucumber.example.test.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePage {

    private final WebDriver driver;
    private final BaseUrl baseUrl;

    @Autowired
    public HomePage(WebDriver driver, BaseUrl baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String signedInUsername() {
        try {
            return driver.findElement(By.id("signed-in-username")).getText();
        } catch (NoSuchElementException e) {
            return "The user is not signed in.";
        }
    }

    public HomePage visit() {
        driver.get(baseUrl.toString());
        return this;
    }
}
