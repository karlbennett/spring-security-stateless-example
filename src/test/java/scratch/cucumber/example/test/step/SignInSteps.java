package scratch.cucumber.example.test.step;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import scratch.cucumber.example.test.page.HomePage;
import scratch.cucumber.example.test.page.SignInPage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class SignInSteps extends SpringBootIntegrationSteps {

    @Autowired
    private SignInPage signInPage;

    @Autowired
    private HomePage homePage;

    @Given("^the user is not signed in$")
    public void the_user_is_not_signed_in() {
        signInPage.signOut();
    }

    @Given("^the user enters a username of \"([^\"]*)\"$")
    public void the_user_enter_a_username_of(String username) {
        signInPage.enterUsername(username);
    }

    @Given("^the user enters a password of \"([^\"]*)\"$")
    public void the_user_enter_a_password_of(String password) {
        signInPage.enterPassword(password);
    }

    @When("^the user goes to the home page$")
    public void the_user_goes_to_the_home_page() {
        homePage.visit();
    }

    @When("^the user signs in$")
    public void the_user_signs_in() {
        signInPage.signIn();
    }

    @Then("^the user should be on the sign in page$")
    public void the_user_should_be_on_the_sign_in_page() {
        assertThat(signInPage.getTitle(), containsString("Sign In"));
    }

    @Then("^the user should be on the home page$")
    public void the_user_should_be_on_the_home_page() {
        assertThat(homePage.getTitle(), containsString("Home Page"));
    }

    @Then("^the user should be signed in as \"([^\"]*)\"$")
    public void the_user_should_be_signed_in_as(String username) {
        assertThat(homePage.signedInUsername(), equalTo(username));
    }
}
