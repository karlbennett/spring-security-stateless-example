/*
 * Copyright (C) 2015  Karl Bennett
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package scratch.cucumber.example.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.domain.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationFactoryTest {

    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private UserFactory<HttpServletRequest> userFactory;
    private TokenFactory tokenFactory;
    private UserAuthenticationFactory userAuthenticationFactory;
    private UserRepository userRepository;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        userFactory = mock(UserFactory.class);
        tokenFactory = mock(TokenFactory.class);
        userRepository = mock(UserRepository.class);

        userAuthenticationFactory = new UserAuthenticationFactory(userFactory, tokenFactory, userRepository);
    }

    @Test
    public void Can_create_an_authentication_from_a_request() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final User user = mock(User.class);
        final String username = someString();
        final String password = someString();

        // Given
        given(userFactory.create(request)).willReturn(user);
        given(user.getUsername()).willReturn(username);
        given(user.getPassword()).willReturn(password);

        // When
        final UsernamePasswordAuthenticationToken actual = userAuthenticationFactory.create(request);

        // Then
        assertThat(actual.getName(), equalTo(username));
        assertThat(actual.getPrincipal().toString(), equalTo(username));
        assertThat(actual.getCredentials().toString(), equalTo(password));
    }

    @Test
    public void Can_create_an_authentication_from_a_user() {

        // Given
        final User user = mock(User.class);

        // When
        final UserAuthentication actual = userAuthenticationFactory.create(user);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
    }

    @Test
    public void Can_add_an_authentication_to_a_response() {

        final Authentication authentication = mock(Authentication.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String username = someString();
        final String token = someString();

        // Given
        given(authentication.getName()).willReturn(username);
        given(tokenFactory.create(username)).willReturn(token);

        // When
        userAuthenticationFactory.add(response, authentication);

        // Then
        verify(response).addHeader(X_AUTH_TOKEN, token);
        verify(response).addCookie(new EqualCookie(X_AUTH_TOKEN, token));
    }

    @Test
    public void Can_retrieve_an_authentication_from_a_request_header() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String token = someString();
        final String username = someString();
        final User user = mock(User.class);

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(token);
        given(request.getCookies()).willReturn(null);
        given(tokenFactory.parseUsername(token)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(user);

        // When
        final UserAuthentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
    }

    @Test
    public void Can_retrieve_an_authentication_from_a_request_cookie() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String token = someString();
        final String username = someString();
        final User user = mock(User.class);

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(new Cookie[]{new Cookie(X_AUTH_TOKEN, token)});
        given(tokenFactory.parseUsername(token)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(user);

        // When
        final UserAuthentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
    }

    @Test
    public void Cannot_retrieve_an_authentication_for_a_user_that_does_not_exist() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String token = someString();
        final String username = someString();

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(new Cookie[]{new Cookie(X_AUTH_TOKEN, token)});
        given(tokenFactory.parseUsername(token)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(null);

        // When
        final UserAuthentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertThat(actual, nullValue());
    }

    @Test
    public void Return_no_authentication_if_no_token_supplied() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(null);

        // When
        final Authentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertThat(actual, nullValue());
        verifyZeroInteractions(tokenFactory);
    }

    @Test
    public void Return_no_authentication_if_no_token_supplied_in_cookies() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(new Cookie[0]);

        // When
        final Authentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertThat(actual, nullValue());
        verifyZeroInteractions(tokenFactory);
    }
}
