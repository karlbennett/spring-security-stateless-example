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

package scratch.cucumber.example.controller;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.security.UsernameFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Karl Bennett
 */
public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final UsernameFactory<HttpServletRequest> usernameFactory;
    private final UserRepository userRepository;

    public UserHandlerMethodArgumentResolver(UsernameFactory<HttpServletRequest> usernameFactory, UserRepository userRepository) {
        this.usernameFactory = usernameFactory;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                WebDataBinderFactory binderFactory) throws Exception {
        return userRepository.findByUsername(usernameFactory.retrieve((HttpServletRequest) request.getNativeRequest()));
    }
}
