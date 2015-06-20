package scratch.cucumber.example.test.page;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignInPage {

    private final WebDriver driver;
    private final BaseUrl baseUrl;
    private final Finders finders;
    private final Bys bys;

    @Autowired
    public SignInPage(WebDriver driver, BaseUrl baseUrl, Finders finders, Bys bys) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.finders = finders;
        this.bys = bys;
    }

    public void enterUsername(String username) {
        finders.enterTextByLabel("User Name", username);
    }

    public void enterPassword(String password) {
        finders.enterTextByLabel("Password", password);
    }

    public HomePage signIn() {
        driver.findElement(bys.byValue("Sign In")).click();
        return new HomePage(driver, baseUrl);
    }

    public SignInPage signOut() {
        driver.manage().deleteAllCookies();
        return this;
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
