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

import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class StatelessAuthenticationFilterTest {

    @Test
    public void Can_authenticate_request() throws IOException, ServletException {

        final AuthenticationFactory authenticationFactory = mock(AuthenticationFactory.class);
        final SecurityContextHolder contextHolder = mock(SecurityContextHolder.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final ServletResponse response = mock(ServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final Authentication authentication = mock(Authentication.class);
        final SecurityContext securityContext = mock(SecurityContext.class);

        // Given
        given(authenticationFactory.retrieve(request)).willReturn(authentication);
        given(contextHolder.getContext()).willReturn(securityContext);

        // When
        new StatelessAuthenticationFilter(authenticationFactory, contextHolder).doFilter(request, response, filterChain);

        // Then
        final InOrder order = inOrder(securityContext, filterChain);
        order.verify(securityContext).setAuthentication(authentication);
        order.verify(filterChain).doFilter(request, response);
    }
}
