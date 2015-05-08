package scratch.cucumber.example.test.step;

import cucumber.api.java.en.Given;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scratch.cucumber.example.CucumberConfiguration;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CucumberConfiguration.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class BackgroundUserSteps {

    @Autowired
    private UserRepository repository;

    @Given("^a user with the username \"([^\"]*)\" and password \"([^\"]*)\" exists$")
    public void a_user_with_the_username_and_password_exists(String username, String password) {
        repository.save(new User(username, password));
    }
}
