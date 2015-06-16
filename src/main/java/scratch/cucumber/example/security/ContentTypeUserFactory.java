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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.parseMediaType;

/**
 * @author Karl Bennett
 */
public class ContentTypeUserFactory implements UserFactory {

    private final Map<MediaType, UserFactory> factories;

    public ContentTypeUserFactory(Map<String, UserFactory> factories) {
        this.factories = transform(factories);
    }

    private static Map<MediaType, UserFactory> transform(Map<String, UserFactory> factories) {

        final Map<MediaType, UserFactory> mediaTypeFactories = new HashMap<>(factories.size());

        for (Map.Entry<String, UserFactory> entry : factories.entrySet()) {
            mediaTypeFactories.put(parseMediaType(entry.getKey()), entry.getValue());
        }

        return mediaTypeFactories;
    }

    @Override
    public User create(HttpServletRequest request) {

        final MediaType contentType = parseMediaType(request.getContentType());

        final UserFactory factory = factories.get(contentType);

        if (factory != null) {
            return factory.create(request);
        }

        try {
            throw new HttpMediaTypeNotSupportedException(contentType, new ArrayList<>(factories.keySet()));
        } catch (HttpMediaTypeNotSupportedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
