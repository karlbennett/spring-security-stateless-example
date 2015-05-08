package scratch.cucumber.example.test.page;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static scratch.cucumber.example.test.page.Bys.byValue;
import static scratch.cucumber.example.test.page.Finders.enterTextByLabel;

@Component
public class SignInPage {

    private final WebDriver driver;
    private final BaseUrl baseUrl;

    @Autowired
    public SignInPage(WebDriver driver, BaseUrl baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }

    public void visit() {
        driver.get(baseUrl + "/signIn");
    }

    public void enterUsername(String username) {
        enterTextByLabel(driver, "User Name", username);
    }

    public void enterPassword(String password) {
        enterTextByLabel(driver, "Password", password);
    }

    public HomePage signIn() {
        driver.findElement(byValue("Sign In")).click();
        return new HomePage(driver);
    }
}
