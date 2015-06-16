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
import scratch.cucumber.example.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

public class ContentTypeUserFactoryTest {

    @Test
    public void Can_create_a_user_for_a_specific_content_type() {

        final String contentTypeOne = APPLICATION_FORM_URLENCODED_VALUE;
        final String contentTypeTwo = APPLICATION_JSON_VALUE;
        final UserFactory userFactoryOne = mock(UserFactory.class);
        final UserFactory userFactoryTwo = mock(UserFactory.class);
        final Map<String, UserFactory> factories = new HashMap<String, UserFactory>() {{
            put(contentTypeOne, userFactoryOne);
            put(contentTypeTwo, userFactoryTwo);
        }};

        final HttpServletRequest request = mock(HttpServletRequest.class);

        final User expected = mock(User.class);

        // Given
        given(request.getContentType()).willReturn(contentTypeTwo);
        given(userFactoryTwo.create(request)).willReturn(expected);

        // When
        final User actual = new ContentTypeUserFactory(factories).create(request);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_a_meaningful_error_when_the_content_type_is_not_supported() {

        final Map<String, UserFactory> factories = new HashMap<String, UserFactory>() {{
            put(APPLICATION_FORM_URLENCODED_VALUE, mock(UserFactory.class));
            put(APPLICATION_JSON_VALUE, mock(UserFactory.class));
        }};

        final HttpServletRequest request = mock(HttpServletRequest.class);

        // Given
        given(request.getContentType()).willReturn(APPLICATION_XML_VALUE);

        // When
        new ContentTypeUserFactory(factories).create(request);
    }
}
