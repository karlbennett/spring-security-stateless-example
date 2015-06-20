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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import org.junit.Before;
import org.junit.Test;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class JwtTokenFactoryTest {

    private String secret;
    private JwtBuilder jwtBuilder;
    private JwtTokenFactory factory;
    private JwtParser jwtParser;

    @Before
    public void setUp() {
        secret = someString();
        jwtBuilder = mock(JwtBuilder.class);
        jwtParser = mock(JwtParser.class);

        factory = new JwtTokenFactory(secret, jwtBuilder, jwtParser);
    }

    @Test
    public void Can_create_a_token_from_an_authentication() {

        final String username = someString();
        final JwtBuilder signWithJwtBuilder = mock(JwtBuilder.class);
        final JwtBuilder compactJwtBuilder = mock(JwtBuilder.class);

        final String expected = someString();

        // Given
        given(jwtBuilder.setSubject(username)).willReturn(signWithJwtBuilder);
        given(signWithJwtBuilder.signWith(HS512, secret)).willReturn(compactJwtBuilder);
        given(compactJwtBuilder.compact()).willReturn(expected);

        // When
        final String actual = factory.create(username);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_create_an_authentication_from_a_token() {

        final String token = someString();

        final JwtParser signingKeyJwtParser = mock(JwtParser.class);
        @SuppressWarnings("unchecked")
        final Jws<Claims> jws = mock(Jws.class);
        final Claims body = mock(Claims.class);
        final String expected = someString();

        // Given
        given(jwtParser.setSigningKey(secret)).willReturn(signingKeyJwtParser);
        given(signingKeyJwtParser.parseClaimsJws(token)).willReturn(jws);
        given(jws.getBody()).willReturn(body);
        given(body.getSubject()).willReturn(expected);

        // When
        final String actual = factory.parseUsername(token);

        // Then
        assertThat(actual, equalTo(expected));
    }
}
