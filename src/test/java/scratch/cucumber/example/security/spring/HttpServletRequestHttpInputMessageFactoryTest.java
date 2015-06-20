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
import org.springframework.http.HttpInputMessage;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;

public class HttpServletRequestHttpInputMessageFactoryTest {

    @Test
    public void Can_create_an_http_input_message_from_an_http_servlet_request() {

        // Given
        final HttpServletRequest request = mock(HttpServletRequest.class);

        // When
        final HttpInputMessage actual = new HttpServletRequestHttpInputMessageFactory().create(request);

        // Then
        assertThat(actual, not(nullValue()));
        assertPropertyReflectionEquals("servletRequest", request, actual);
    }
}
