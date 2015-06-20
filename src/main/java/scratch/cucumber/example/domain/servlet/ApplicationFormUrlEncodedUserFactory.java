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

package scratch.cucumber.example.domain.servlet;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.domain.UserFactory;
import scratch.cucumber.example.security.spring.HttpInputMessageFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Karl Bennett
 */
public class ApplicationFormUrlEncodedUserFactory implements UserFactory<HttpServletRequest> {

    private final HttpInputMessageFactory<HttpServletRequest> httpInputMessageFactory;
    private final FormHttpMessageConverter formHttpMessageConverter;
    private final UserFactory<MultiValueMap<String, String>> multiValueMapUserFactory;

    public ApplicationFormUrlEncodedUserFactory(
        HttpInputMessageFactory<HttpServletRequest> httpInputMessageFactory,
        FormHttpMessageConverter formHttpMessageConverter,
        UserFactory<MultiValueMap<String, String>> multiValueMapUserFactory
    ) {
        this.httpInputMessageFactory = httpInputMessageFactory;
        this.formHttpMessageConverter = formHttpMessageConverter;
        this.multiValueMapUserFactory = multiValueMapUserFactory;
    }

    @Override
    public User create(HttpServletRequest request) {
        try {
            return multiValueMapUserFactory.create(formHttpMessageConverter.read(null, httpInputMessageFactory.create(request)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
