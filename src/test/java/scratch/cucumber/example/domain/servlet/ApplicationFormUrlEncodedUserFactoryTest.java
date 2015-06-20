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

import org.junit.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.domain.UserFactory;
import scratch.cucumber.example.security.spring.HttpInputMessageFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ApplicationFormUrlEncodedUserFactoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void Can_create_a_user_from_an_application_form_url_encoded_request() throws IOException {

        final HttpInputMessageFactory<HttpServletRequest> httpInputMessageFactory = mock(HttpInputMessageFactory.class);
        final FormHttpMessageConverter formHttpMessageConverter = mock(FormHttpMessageConverter.class);
        final UserFactory<MultiValueMap<String, String>> multiValueMapUserFactory = mock(UserFactory.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);
        final MultiValueMap<String, String> multiValueMap = mock(MultiValueMap.class);

        final User expected = mock(User.class);

        // Given
        given(httpInputMessageFactory.create(request)).willReturn(httpInputMessage);
        given(formHttpMessageConverter.read(null, httpInputMessage)).willReturn(multiValueMap);
        given(multiValueMapUserFactory.create(multiValueMap)).willReturn(expected);

        // When
        final User actual = new ApplicationFormUrlEncodedUserFactory(
            httpInputMessageFactory,
            formHttpMessageConverter,
            multiValueMapUserFactory
        ).create(request);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test(expected = IllegalStateException.class)
    @SuppressWarnings("unchecked")
    public void Exception_is_thrown_if_request_cannot_be_read() throws IOException {

        final HttpInputMessageFactory<HttpServletRequest> httpInputMessageFactory = mock(HttpInputMessageFactory.class);
        final FormHttpMessageConverter formHttpMessageConverter = mock(FormHttpMessageConverter.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final HttpInputMessage httpInputMessage = mock(HttpInputMessage.class);

        // Given
        given(httpInputMessageFactory.create(request)).willReturn(httpInputMessage);
        given(formHttpMessageConverter.read(null, httpInputMessage)).willThrow(new IOException());

        // When
        new ApplicationFormUrlEncodedUserFactory(
            httpInputMessageFactory,
            formHttpMessageConverter,
            mock(UserFactory.class)
        ).create(request);
    }
}
