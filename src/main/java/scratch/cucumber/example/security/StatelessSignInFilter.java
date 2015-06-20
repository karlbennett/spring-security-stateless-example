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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import scratch.cucumber.example.data.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Karl Bennett
 */
public class StatelessSignInFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationFactory authenticationFactory;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SecurityContextHolder securityContextHolder;

    public StatelessSignInFilter(
        RequestMatcher requestMatcher,
        AuthenticationFactory authenticationFactory,
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        SecurityContextHolder securityContextHolder
    ) {
        super(requestMatcher);
        this.authenticationFactory = authenticationFactory;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.securityContextHolder = securityContextHolder;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if (request instanceof HttpServletRequest && "POST".equals(((HttpServletRequest) request).getMethod())) {
            super.doFilter(request, response, chain);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        return authenticationManager.authenticate(authenticationFactory.create(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        final Authentication userAuthentication = authenticationFactory.create(
            userRepository.findByUsername(authentication.getName())
        );

        authenticationFactory.add(response, userAuthentication);

        securityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
