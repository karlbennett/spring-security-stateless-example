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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.util.matcher.RequestMatcher;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.domain.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class StatelessSignInFilterTest {

    private AuthenticationFactory authenticationFactory;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private SecurityContextHolder securityContextHolder;

    private StatelessSignInFilter statelessSignInFilter;
    private RequestMatcher requestMatcher;

    @Before
    public void setUp() {
        authenticationFactory = mock(AuthenticationFactory.class);
        authenticationManager = mock(AuthenticationManager.class);
        userRepository = mock(UserRepository.class);
        securityContextHolder = mock(SecurityContextHolder.class);
        requestMatcher = mock(RequestMatcher.class);

        statelessSignInFilter = new StatelessSignInFilter(
            requestMatcher,
            authenticationFactory,
            authenticationManager,
            userRepository,
            securityContextHolder
        );
    }

    @Test
    public void Can_attempt_to_sign_in_user() throws IOException, ServletException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Authentication authentication = mock(Authentication.class);
        final Authentication expected = mock(Authentication.class);

        // Given
        given(authenticationFactory.create(request)).willReturn(authentication);
        given(authenticationManager.authenticate(authentication)).willReturn(expected);

        // When
        final Authentication actual = statelessSignInFilter.attemptAuthentication(request, response);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_complete_the_sign_in_of_a_user() throws IOException, ServletException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);
        final Authentication authentication = mock(Authentication.class);

        final String userName = someString();
        final User user = mock(User.class);
        final Authentication userAuthentication = mock(Authentication.class);
        final SecurityContext securityContext = mock(SecurityContext.class);

        // Given
        given(authentication.getName()).willReturn(userName);
        given(userRepository.findByUsername(userName)).willReturn(user);
        given(authenticationFactory.create(user)).willReturn(userAuthentication);
        given(securityContextHolder.getContext()).willReturn(securityContext);

        // When
        statelessSignInFilter.successfulAuthentication(request, response, filterChain, authentication);

        // Then
        verify(authenticationFactory).add(response, userAuthentication);
        verify(securityContext).setAuthentication(userAuthentication);
    }
}
