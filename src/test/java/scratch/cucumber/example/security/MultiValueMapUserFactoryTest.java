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
import org.springframework.util.MultiValueMap;
import scratch.cucumber.example.domain.User;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class MultiValueMapUserFactoryTest {

    @Test
    public void Can_create_a_user_from_a_multi_value_map() {

        @SuppressWarnings("unchecked")
        final MultiValueMap<String, String> multiValueMap = mock(MultiValueMap.class);

        final String username = someString();
        final String password = someString();

        // Given
        given(multiValueMap.getFirst("username")).willReturn(username);
        given(multiValueMap.getFirst("password")).willReturn(password);

        // When
        final User actual = new MultiValueMapUserFactory().create(multiValueMap);

        // Then
        assertThat(actual.getUsername(), equalTo(username));
        assertThat(actual.getPassword(), equalTo(password));
    }

    @Test(expected = IllegalArgumentException.class)
    public void A_meaningful_error_is_produced_if_no_username_exists() {

        @SuppressWarnings("unchecked")
        final MultiValueMap<String, String> multiValueMap = mock(MultiValueMap.class);

        final String password = someString();

        // Given
        given(multiValueMap.getFirst("password")).willReturn(password);

        // When
        new MultiValueMapUserFactory().create(multiValueMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void A_meaningful_error_is_produced_if_no_password_exists() {

        @SuppressWarnings("unchecked")
        final MultiValueMap<String, String> multiValueMap = mock(MultiValueMap.class);

        final String username = someString();

        // Given
        given(multiValueMap.getFirst("username")).willReturn(username);

        // When
        new MultiValueMapUserFactory().create(multiValueMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void A_meaningful_error_is_produced_if_no_username_or_password_exists() {

        @SuppressWarnings("unchecked")
        final MultiValueMap<String, String> multiValueMap = mock(MultiValueMap.class);

        // When
        new MultiValueMapUserFactory().create(multiValueMap);
    }
}
