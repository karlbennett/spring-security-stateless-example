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

package scratch.cucumber.example.security.token;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

/**
 * @author Karl Bennett
 */
public class JwtTokenFactory implements TokenFactory {

    private final String secret;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;

    public JwtTokenFactory(String secret, JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this.secret = secret;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
    }

    @Override
    public String create(String username) {
        return jwtBuilder.setSubject(username).signWith(HS512, secret).compact();
    }

    @Override
    public String parseUsername(String token) {
        return jwtParser.setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
