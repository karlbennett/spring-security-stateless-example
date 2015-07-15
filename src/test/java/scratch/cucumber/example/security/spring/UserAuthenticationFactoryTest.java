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

package scratch.cucumber.example.security.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import scratch.cucumber.example.security.TokenFactory;
import scratch.cucumber.example.security.servlet.UsernameFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationFactoryTest {

    private TokenFactory tokenFactory;
    private UserAuthenticationFactory userAuthenticationFactory;
    private UsernameFactory usernameFactory;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        tokenFactory = mock(TokenFactory.class);
        usernameFactory = mock(UsernameFactory.class);

        userAuthenticationFactory = new UserAuthenticationFactory(usernameFactory);
    }

    @Test
    public void Can_add_an_authentication_to_a_response() {

        final Authentication authentication = mock(Authentication.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String username = someString();

        // Given
        given(authentication.getName()).willReturn(username);

        // When
        userAuthenticationFactory.add(response, authentication);

        // Then
        verify(usernameFactory).add(response, username);
    }

    @Test
    public void Can_retrieve_an_authentication_from_a_request() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String username = someString();

        // Given
        given(usernameFactory.retrieve(request)).willReturn(username);

        // When
        final UserAuthentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertThat(actual.getName(), equalTo(username));
    }

    @Test
    public void Return_no_authentication_if_no_username_found() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(usernameFactory.retrieve(request)).willReturn(null);

        // When
        final Authentication actual = userAuthenticationFactory.retrieve(request);

        // Then
        assertThat(actual, nullValue());
        verifyZeroInteractions(tokenFactory);
    }
}
