package scratch.cucumber.example.test.page;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePage {

    private final WebDriver driver;

    @Autowired
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isSignedIn() {
        try {
            driver.findElement(By.id("signed-in-username"));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
