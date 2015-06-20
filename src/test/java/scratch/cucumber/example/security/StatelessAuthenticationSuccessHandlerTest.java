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

import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class StatelessAuthenticationSuccessHandlerTest {

    @Test
    public void Can_add_successful_authentication_to_the_response() throws IOException, ServletException {

        final AuthenticationFactory authenticationFactory = mock(AuthenticationFactory.class);
        final SimpleUrlAuthenticationSuccessHandler handler = mock(SimpleUrlAuthenticationSuccessHandler.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Authentication authentication = mock(Authentication.class);

        // When
        new StatelessAuthenticationSuccessHandler(authenticationFactory, handler)
            .onAuthenticationSuccess(request, response, authentication);

        // Then
        final InOrder order = inOrder(authenticationFactory, handler);
        order.verify(authenticationFactory).add(response, authentication);
        order.verify(handler).onAuthenticationSuccess(request, response, authentication);
    }
}
