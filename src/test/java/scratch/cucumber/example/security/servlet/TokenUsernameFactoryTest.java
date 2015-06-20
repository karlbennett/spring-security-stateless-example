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

package scratch.cucumber.example.security.servlet;

import org.junit.Before;
import org.junit.Test;
import scratch.cucumber.example.security.EqualCookie;
import scratch.cucumber.example.security.TokenFactory;

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
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class TokenUsernameFactoryTest {

    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private TokenFactory tokenFactory;
    private TokenUsernameFactory factory;

    @Before
    public void setUp() {
        tokenFactory = mock(TokenFactory.class);
        factory = new TokenUsernameFactory(tokenFactory);
    }

    @Test
    public void Can_add_a_token_to_a_response() {

        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String username = someString();
        final String token = someString();

        // Given
        given(tokenFactory.create(username)).willReturn(token);

        // When
        factory.add(response, username);

        // Then
        verify(response).addHeader(X_AUTH_TOKEN, token);
        verify(response).addCookie(new EqualCookie(X_AUTH_TOKEN, token));
    }

    @Test
    public void Can_retrieve_an_authentication_from_a_request_header() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String token = someString();
        final String expected = someString();

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(token);
        given(request.getCookies()).willReturn(null);
        given(tokenFactory.parseUsername(token)).willReturn(expected);

        // When
        final String actual = factory.retrieve(request);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_retrieve_an_authentication_from_a_request_cookie() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final String token = someString();
        final String expected = someString();

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(new Cookie[]{new Cookie(X_AUTH_TOKEN, token)});
        given(tokenFactory.parseUsername(token)).willReturn(expected);

        // When
        final String actual = factory.retrieve(request);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Return_no_authentication_if_no_token_supplied() {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getHeader(X_AUTH_TOKEN)).willReturn(null);
        given(request.getCookies()).willReturn(null);

        // When
        final String actual = factory.retrieve(request);

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
        final String actual = factory.retrieve(request);

        // Then
        assertThat(actual, nullValue());
        verifyZeroInteractions(tokenFactory);
    }
}
