package scratch.cucumber.example.test.step;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scratch.cucumber.example.CucumberConfiguration;
import scratch.cucumber.example.test.page.HomePage;
import scratch.cucumber.example.test.page.SignInPage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CucumberConfiguration.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class SignInSteps {

    @Autowired
    private SignInPage signInPage;

    @Autowired
    private HomePage homePage;

    @Given("^the user is on the login page$")
    public void the_user_is_on_the_login_page() {
        signInPage.visit();
    }

    @Given("^the user enters a username of \"([^\"]*)\"$")
    public void the_user_enter_a_username_of(String username) {
        signInPage.enterUsername(username);
    }

    @Given("^the user enters a password of \"([^\"]*)\"$")
    public void the_user_enter_a_password_of(String password) {
        signInPage.enterPassword(password);
    }

    @When("^the user signs in$")
    public void the_user_signs_in() {
        signInPage.signIn();
    }

    @Then("^the user should be on the home page$")
    public void the_user_should_be_on_the_home_page() {
        assertThat(homePage.getTitle(), containsString("Home Page"));
    }

    @Then("^the user should be signed in$")
    public void the_user_should_be_signed_in() {
        assertThat(homePage.isSignedIn(), equalTo(true));
    }
}
