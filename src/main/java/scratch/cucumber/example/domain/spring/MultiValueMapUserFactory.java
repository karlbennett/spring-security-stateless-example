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

package scratch.cucumber.example.domain.spring;

import org.springframework.util.MultiValueMap;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.domain.UserFactory;

/**
 * @author Karl Bennett
 */
public class MultiValueMapUserFactory implements UserFactory<MultiValueMap<String, String>> {

    @Override
    public User create(MultiValueMap<String, String> multiValueMap) {

        final String username = multiValueMap.getFirst("username");
        final String password = multiValueMap.getFirst("password");

        if (username == null && password == null) {
            throw new IllegalArgumentException("A username and password must be supplied.");
        }

        if (username == null) {
            throw new IllegalArgumentException("A username must be supplied.");
        }

        if (password == null) {
            throw new IllegalArgumentException("A password must be supplied.");
        }

        return new User(username, password);
    }
}