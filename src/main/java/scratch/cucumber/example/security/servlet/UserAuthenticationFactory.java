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

package scratch.cucumber.example.security.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.security.spring.UserAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Karl Bennett
 */
@Component
public class UserAuthenticationFactory implements HttpServletRequestBinder<Authentication> {

    private final HttpServletRequestBinder<String> httpServletRequestBinder;

    @Autowired
    public UserAuthenticationFactory(HttpServletRequestBinder<String> httpServletRequestBinder) {
        this.httpServletRequestBinder = httpServletRequestBinder;
    }

    @Override
    public void add(HttpServletResponse response, Authentication authentication) {
        httpServletRequestBinder.add(response, authentication.getName());
    }

    @Override
    public UserAuthentication retrieve(HttpServletRequest request) {

        final String username = httpServletRequestBinder.retrieve(request);

        if (username != null) {

            return new UserAuthentication(new User(username, ""));
        }

        return null;
    }
}
