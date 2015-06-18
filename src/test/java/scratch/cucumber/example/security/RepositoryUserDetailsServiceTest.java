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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.domain.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class RepositoryUserDetailsServiceTest {

    @Test
    public void Can_load_user() {

        final String username = someString();
        final UserRepository userRepository = mock(UserRepository.class);

        final User expected = mock(User.class);

        // Given
        given(userRepository.findByUsername(username)).willReturn(expected);

        // When
        final UserDetails actual = new RepositoryUserDetailsService(userRepository).loadUserByUsername(username);

        // Then
        assertPropertyReflectionEquals("user", expected, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void Get_a_meaningful_error_when_the_user_cannot_be_loaded() {

        final String username = someString();
        final UserRepository userRepository = mock(UserRepository.class);

        // Given
        given(userRepository.findByUsername(username)).willReturn(null);

        // When
        new RepositoryUserDetailsService(userRepository).loadUserByUsername(username);
    }
}
