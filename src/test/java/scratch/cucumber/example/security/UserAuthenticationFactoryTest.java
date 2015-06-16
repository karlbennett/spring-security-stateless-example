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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import scratch.cucumber.example.domain.User;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationFactoryTest {

    private UserFactory userFactory;
    private TokenFactory tokenFactory;
    private UserAuthenticationFactory userAuthenticationFactory;

    @Before
    public void setUp() {
        userFactory = mock(UserFactory.class);
        tokenFactory = mock(TokenFactory.class);
        userAuthenticationFactory = new UserAuthenticationFactory(userFactory, tokenFactory);
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
        final Authentication actual = userAuthenticationFactory.create(request);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
        assertThat(actual.getName(), equalTo(username));
        assertThat(actual.getPrincipal().toString(), equalTo(username));
        assertThat(actual.getCredentials().toString(), equalTo(password));

        final UserDetails details = (UserDetails) actual.getDetails();

        assertThat(details.getUsername(), equalTo(username));
        assertThat(details.getPassword(), equalTo(password));
        assertThat(details.getAuthorities(), empty());
        assertThat(details.isAccountNonExpired(), equalTo(true));
        assertThat(details.isAccountNonLocked(), equalTo(true));
        assertThat(details.isCredentialsNonExpired(), equalTo(true));
        assertThat(details.isEnabled(), equalTo(true));
    }

    @Test
    public void Can_create_an_authentication_from_a_user() {

        final User user = mock(User.class);
        final String username = someString();
        final String password = someString();

        // Given
        given(user.getUsername()).willReturn(username);
        given(user.getPassword()).willReturn(password);

        // When
        final Authentication actual = userAuthenticationFactory.create(user);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
        assertThat(actual.getName(), equalTo(username));
        assertThat(actual.getPrincipal().toString(), equalTo(username));
        assertThat(actual.getCredentials().toString(), equalTo(password));
        assertThat(actual.getAuthorities(), empty());
        assertThat(actual.isAuthenticated(), equalTo(true));
        assertThat(actual.implies(new Subject()), equalTo(false));

        final UserDetails details = (UserDetails) actual.getDetails();

        assertThat(details.getUsername(), equalTo(username));
        assertThat(details.getPassword(), equalTo(password));
        assertThat(details.getAuthorities(), empty());
        assertThat(details.isAccountNonExpired(), equalTo(true));
        assertThat(details.isAccountNonLocked(), equalTo(true));
        assertThat(details.isCredentialsNonExpired(), equalTo(true));
        assertThat(details.isEnabled(), equalTo(true));
    }

    @Test
    public void Can_apply_an_authentication_to_a_response() {

        final Authentication authentication = mock(Authentication.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String token = someString();

        // Given
        given(tokenFactory.create(authentication)).willReturn(token);

        // When
        userAuthenticationFactory.apply(authentication, response);

        // Then
        verify(response).addHeader("X-AUTH-TOKEN", token);
        verify(response).addCookie(new EqualCookie("X-AUTH-TOKEN", token));
    }
}
