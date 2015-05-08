package scratch.cucumber.example.controller;


import org.junit.Before;
import org.junit.Test;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.SignInService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SignInControllerTest {

    private SignInService signInService;
    private SignInController controller;
    private CookieFactory cookieFactory;

    @Before
    public void setUp() throws Exception {
        signInService = mock(SignInService.class);
        cookieFactory = mock(CookieFactory.class);
        controller = new SignInController(signInService, cookieFactory);
    }

    @Test
    public void canDisplaySignIn() throws Exception {

        //When
        final String actual = controller.display();

        //Then
        assertThat(actual, equalTo("signIn"));
    }

    @Test
    public void canSignIn() throws Exception {

        final String username = "username";
        final String password = "password";
        final HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie cookie = mock(Cookie.class);
        User signedInUser = mock(User.class);

        //Given
        given(signInService.signIn(username, password)).willReturn(signedInUser);
        given(cookieFactory.createSignIn(signedInUser)).willReturn(cookie);

        //When
        final String actual = controller.signIn(username, password, response);

        //Then
        verify(response).addCookie(cookie);
        assertThat(actual, equalTo("redirect:"));
    }
}