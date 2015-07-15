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
import org.springframework.web.filter.GenericFilterBean;
import scratch.cucumber.example.security.servlet.HttpServletRequestBinder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Karl Bennett
 */
public class StatelessAuthenticationFilter extends GenericFilterBean {

    private final HttpServletRequestBinder<Authentication> authenticationBinder;
    private final SecurityContextHolder contextHolder;

    public StatelessAuthenticationFilter(
        HttpServletRequestBinder<Authentication> authenticationBinder,
        SecurityContextHolder contextHolder
    ) {
        this.authenticationBinder = authenticationBinder;
        this.contextHolder = contextHolder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        contextHolder.getContext().setAuthentication(authenticationBinder.retrieve((HttpServletRequest) request));
        filterChain.doFilter(request, response);
    }
}
