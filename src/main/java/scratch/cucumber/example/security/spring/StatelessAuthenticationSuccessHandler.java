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

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Karl Bennett
 */
public class StatelessAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationFactory authenticationFactory;
    private final SimpleUrlAuthenticationSuccessHandler handler;

    public StatelessAuthenticationSuccessHandler(
        AuthenticationFactory authenticationFactory,
        SimpleUrlAuthenticationSuccessHandler handler
    ) {
        this.authenticationFactory = authenticationFactory;
        this.handler = handler;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        authenticationFactory.add(response, authentication);

        handler.onAuthenticationSuccess(request, response, authentication);
    }
}
