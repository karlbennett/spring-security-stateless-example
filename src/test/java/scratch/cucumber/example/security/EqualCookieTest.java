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

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomStrings.someAlphaNumericString;

public class EqualCookieTest {

    @Test
    public void Cookies_with_the_same_values_are_equal() {

        // Given
        final String name = someAlphaNumericString();
        final String value = someAlphaNumericString();

        final EqualCookie expected = new EqualCookie(name, value);

        // When
        final EqualCookie actual = new EqualCookie(name, value);

        // Then
        assertThat(actual, equalTo(actual));
        assertThat(actual, equalTo(expected));
        assertThat(actual, equalTo(new Cookie(name, value)));
        assertThat(new EqualCookie(name, null), equalTo(new EqualCookie(name, null)));

        assertThat(actual.hashCode(), equalTo(expected.hashCode()));
    }

    @Test
    public void Cookies_are_not_equal_to_other_classes() {

        // When
        final EqualCookie actual = new EqualCookie(someAlphaNumericString(), someAlphaNumericString());

        // Then
        assertThat(actual, not(equalTo(new Object())));
    }

    @Test
    public void Cookies_with_the_different_names_are_not_equal() {

        // Given
        final String value = someAlphaNumericString();
        final String name = someAlphaNumericString();

        final EqualCookie expected = new EqualCookie(someAlphaNumericString(), value);

        // When
        final EqualCookie actual = new EqualCookie(name, value);

        // Then
        assertThat(actual, not(equalTo(expected)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
    }

    @Test
    public void Cookies_with_the_different_values_are_not_equal() {

        // Given
        final String name = someAlphaNumericString();
        final String value = someAlphaNumericString();

        final EqualCookie expected = new EqualCookie(name, someAlphaNumericString());

        // When
        final EqualCookie actual = new EqualCookie(name, value);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new EqualCookie(name, null))));
        assertThat(new EqualCookie(name, null), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new EqualCookie(name, null).hashCode())));
    }
}
