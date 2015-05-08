package scratch.cucumber.example.controller;

import org.junit.Test;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.HashFactory;

import javax.servlet.http.Cookie;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class CookieFactoryImplTest {

    @Test
    public void Can_create_sign_in_cookie() {

        final HashFactory hashFactory = mock(HashFactory.class);
        final User user = mock(User.class);
        final String username = "username";
        final String password = "password";
        final String base64Password = "base64Password";

        // Given
        given(user.getUsername()).willReturn(username);
        given(user.getPassword()).willReturn(password);
        given(hashFactory.encode(password)).willReturn(base64Password);

        // When
        final Cookie actual = new CookieFactoryImpl(hashFactory).createSignIn(user);

        // Then
        assertThat(actual.getName(), equalTo("signIn"));
        assertThat(actual.getValue(), equalTo(username + "|" + base64Password));
    }
}