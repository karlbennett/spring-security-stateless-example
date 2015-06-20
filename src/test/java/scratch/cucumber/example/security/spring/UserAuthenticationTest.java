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
import org.springframework.security.core.userdetails.UserDetails;
import scratch.cucumber.example.domain.User;

import javax.security.auth.Subject;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationTest {

    @Test
    public void Can_create_a_user_authentication() {

        final User user = mock(User.class);
        final String username = someString();
        final String password = someString();
        final Boolean authenticated = someBoolean();

        // Given
        given(user.getUsername()).willReturn(username);
        given(user.getPassword()).willReturn(password);

        // When
        final UserAuthentication actual = new UserAuthentication(user);
        actual.setAuthenticated(authenticated);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
        assertThat(actual.getName(), equalTo(username));
        assertThat(actual.getPrincipal(), equalTo(username));
        assertThat(actual.getCredentials(), equalTo(password));
        assertThat(actual.getAuthorities(), empty());
        assertThat(actual.isAuthenticated(), equalTo(authenticated));
        assertThat(actual.implies(new Subject()), equalTo(false));

        final UserDetails details = actual.getDetails();

        assertThat(details.getUsername(), equalTo(username));
        assertThat(details.getPassword(), equalTo(password));
        assertThat(details.getAuthorities(), empty());
        assertThat(details.isAccountNonExpired(), equalTo(true));
        assertThat(details.isAccountNonLocked(), equalTo(true));
        assertThat(details.isCredentialsNonExpired(), equalTo(true));
        assertThat(details.isEnabled(), equalTo(true));
    }
}
