package scratch.cucumber.example.controller;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import scratch.cucumber.example.service.SignInService;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class HomeControllerTest {

    @Test
    public void Can_display_home_page() {

        final SignInService signInService = mock(SignInService.class);
        final String username = "User Name";
        final String token = "token";

        // Given
        given(signInService.findUsernameByToken(token)).willReturn(username);

        // When
        final ModelAndView actual = new HomeController(signInService).display(token);

        // Then
        assertThat(actual.getViewName(), equalTo("home"));
        assertThat(actual.getModel().get("username").toString(), equalTo(username));
    }
}