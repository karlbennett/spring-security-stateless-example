package scratch.cucumber.example.test.step;

import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.UserRepository;

public class BackgroundUserSteps extends SpringBootIntegrationSteps {

    @Autowired
    private UserRepository repository;

    @Given("^a user with the username \"([^\"]*)\" and password \"([^\"]*)\" exists$")
    public void a_user_with_the_username_and_password_exists(String username, String password) {
        repository.save(new User(username, password));
    }
}
