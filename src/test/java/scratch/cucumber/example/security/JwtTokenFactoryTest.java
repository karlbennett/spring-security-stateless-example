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

import io.jsonwebtoken.JwtBuilder;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class JwtTokenFactoryTest {

    @Test
    public void Can_create_a_token_from_an_authentication() {

        final JwtBuilder jwtBuilder = mock(JwtBuilder.class);
        final String secret = someString();

        final Authentication authentication = mock(Authentication.class);

        final String username = someString();
        final JwtBuilder signWithJwtBuilder = mock(JwtBuilder.class);
        final JwtBuilder compactJwtBuilder = mock(JwtBuilder.class);

        final String expected = someString();

        // Given
        given(authentication.getName()).willReturn(username);
        given(jwtBuilder.setSubject(authentication.getName())).willReturn(signWithJwtBuilder);
        given(signWithJwtBuilder.signWith(HS512, secret)).willReturn(compactJwtBuilder);
        given(compactJwtBuilder.compact()).willReturn(expected);

        // When
        final String actual = new JwtTokenFactory(jwtBuilder, secret).create(authentication);

        // Then
        assertThat(actual, equalTo(expected));
    }
}
