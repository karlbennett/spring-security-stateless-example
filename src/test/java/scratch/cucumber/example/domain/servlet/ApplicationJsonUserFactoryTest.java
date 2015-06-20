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

package scratch.cucumber.example.domain.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scratch.cucumber.example.domain.User;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ApplicationJsonUserFactoryTest {

    private ObjectMapper objectMapper;
    private ApplicationJsonUserFactory userFactory;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        userFactory = new ApplicationJsonUserFactory(objectMapper);
    }

    @Test
    public void Can_create_a_user_from_a_json_request() throws IOException {

        final ServletInputStream inputStream = mock(ServletInputStream.class);
        final User expected = mock(User.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getInputStream()).willReturn(inputStream);
        given(objectMapper.readValue(inputStream, User.class)).willReturn(expected);

        // When
        final User actual = userFactory.create(request);

        // Then
        Assert.assertThat(actual, equalTo(expected));
    }

    @Test(expected = IllegalStateException.class)
    public void Exception_is_thrown_if_request_cannot_be_read() throws IOException {

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getInputStream()).willThrow(new IOException());

        // When
        userFactory.create(request);
    }
}
