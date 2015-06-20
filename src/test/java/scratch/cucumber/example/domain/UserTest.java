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

package scratch.cucumber.example.domain;

import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomLongs.someLong;
import static shiver.me.timbers.data.random.RandomStrings.someAlphaNumericString;

public class UserTest {

    @Test
    public void Users_with_the_same_values_are_equal() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final User expected = new User(id, username, password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, equalTo(actual));
        assertThat(actual, equalTo(expected));
        assertThat(new User(null, username, password), equalTo(new User(null, username, password)));
        assertThat(new User(id, null, password), equalTo(new User(id, null, password)));
        assertThat(new User(id, username, null), equalTo(new User(id, username, null)));

        assertThat(actual.hashCode(), equalTo(expected.hashCode()));
    }

    @Test
    public void Users_are_not_equal_to_other_classes() {

        // When
        final User actual = new User(someAlphaNumericString(), someAlphaNumericString());

        // Then
        assertThat(actual, not(equalTo(new Object())));
    }

    @Test
    public void Users_with_the_different_ids_are_not_equal() {

        // Given
        final Long id = someLong();
        final String password = someAlphaNumericString();
        final String username = someAlphaNumericString();

        final User expected = new User(someLong(), username, password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(null, username, password))));
        assertThat(new User(null, username, password), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(null, username, password).hashCode())));
    }

    @Test
    public void Users_with_the_different_user_names_are_not_equal() {

        // Given
        final Long id = someLong();
        final String password = someAlphaNumericString();
        final String username = someAlphaNumericString();

        final User expected = new User(id, someAlphaNumericString(), password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(id, null, password))));
        assertThat(new User(id, null, password), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(id, null, password).hashCode())));
    }

    @Test
    public void Users_with_the_different_passwords_are_not_equal() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final User expected = new User(id, username, someAlphaNumericString());

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(id, username, null))));
        assertThat(new User(id, username, null), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(id, username, null).hashCode())));
    }

    @Test
    public void Can_to_string_a_user() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final String expected = format("User {\n  id=%d,\n  username='%s',\n  password='%s'\n}", id, username, password);

        // When
        final String actual = new User(id, username, password).toString();

        // Then
        assertThat(actual, equalTo(expected));
    }
}
