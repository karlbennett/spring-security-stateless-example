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

import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import scratch.cucumber.example.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

import static org.springframework.http.MediaType.parseMediaType;

/**
 * @author Karl Bennett
 */
public class ContentTypeUserFactory implements UserFactory<HttpServletRequest> {

    private final Map<MediaType, UserFactory<HttpServletRequest>> factories;

    public ContentTypeUserFactory(Map<MediaType, UserFactory<HttpServletRequest>> factories) {
        this.factories = factories;
    }

    @Override
    public User create(HttpServletRequest request) {

        final MediaType contentType = parseMediaType(request.getContentType());

        final UserFactory<HttpServletRequest> factory = factories.get(contentType);

        if (factory != null) {
            return factory.create(request);
        }

        try {
            // Using the HttpMediaTypeNotSupportedException to get a much more descriptive exception message.
            throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList<>(factories.keySet()));
        } catch (HttpMediaTypeNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
