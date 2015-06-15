package scratch.cucumber.example.controller;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import scratch.cucumber.example.domain.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class HomeControllerTest {

    @Test
    public void Can_display_home_page() {

        final String username = "User Name";
        final User user = mock(User.class);

        // Given
        given(user.getUsername()).willReturn(username);

        // When
        final ModelAndView actual = new HomeController().display(user);

        // Then
        assertThat(actual.getViewName(), equalTo("home"));
        assertThat(actual.getModel().get("username").toString(), equalTo(username));
    }
}