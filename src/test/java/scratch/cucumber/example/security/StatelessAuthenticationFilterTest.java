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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.mock;

public class StatelessAuthenticationFilterTest {

    @Test
    public void Can_authenticate_request() throws IOException, ServletException {

        // Given
        final ServletRequest request = mock(ServletRequest.class);
        final ServletResponse response = mock(ServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        // When
        new StatelessAuthenticationFilter().doFilter(request, response, filterChain);

        // Then
    }
}
