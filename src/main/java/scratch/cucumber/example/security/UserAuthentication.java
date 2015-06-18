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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import scratch.cucumber.example.domain.User;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * @author Karl Bennett
 */
public class UserAuthentication implements Authentication {

    private final User user;
    private UserDetails userDetails;
    private boolean authenticated = true;

    public UserAuthentication(User user) {
        this.user = user;
        this.userDetails = new UserDetails(user);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getCredentials() {
        return user.getPassword();
    }

    @Override
    public UserDetails getDetails() {
        return userDetails;
    }

    @Override
    public String getPrincipal() {
        return user.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
