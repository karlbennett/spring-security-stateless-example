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

import com.fasterxml.jackson.databind.ObjectMapper;
import scratch.cucumber.example.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Karl Bennett
 */
public class ApplicationJsonUserFactory implements UserFactory<HttpServletRequest> {

    private final ObjectMapper objectMapper;

    public ApplicationJsonUserFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public User create(HttpServletRequest request) {
        try {
            return objectMapper.readValue(request.getInputStream(), User.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
