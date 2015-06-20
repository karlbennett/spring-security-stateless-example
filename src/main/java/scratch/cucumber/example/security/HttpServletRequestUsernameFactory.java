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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Karl Bennett
 */
@Component
public class HttpServletRequestUsernameFactory implements UsernameFactory<HttpServletRequest> {

    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
    private final TokenFactory tokenFactory;

    @Autowired
    public HttpServletRequestUsernameFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void add(HttpServletResponse response, String username) {

        final String token = tokenFactory.create(username);

        response.addHeader(X_AUTH_TOKEN, token);
        response.addCookie(new Cookie(X_AUTH_TOKEN, token));
    }

    @Override
    public String retrieve(HttpServletRequest request) {

        final String cookieToken = findToken(request);

        if (cookieToken != null) {
            return tokenFactory.parseUsername(cookieToken);
        }

        return null;
    }

    private static String findToken(HttpServletRequest request) {

        final String headerToken = request.getHeader(X_AUTH_TOKEN);

        if (headerToken != null) {
            return headerToken;
        }

        final Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (X_AUTH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
