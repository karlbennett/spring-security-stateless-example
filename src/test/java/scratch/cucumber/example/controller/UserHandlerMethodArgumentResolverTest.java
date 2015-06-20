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

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.security.UsernameFactory;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserHandlerMethodArgumentResolverTest {

    private UsernameFactory<HttpServletRequest> usernameFactory;
    private UserRepository userRepository;
    private UserHandlerMethodArgumentResolver resolver;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        usernameFactory = mock(UsernameFactory.class);
        userRepository = mock(UserRepository.class);

        resolver = new UserHandlerMethodArgumentResolver(usernameFactory, userRepository);
    }

    @Test
    public void Can_resolver_a_user() throws Exception {

        final NativeWebRequest request = mock(NativeWebRequest.class);

        final HttpServletRequest nativeRequest = mock(HttpServletRequest.class);
        final String username = someString();

        final User expected = mock(User.class);

        // Given
        given(request.getNativeRequest()).willReturn(nativeRequest);
        given(usernameFactory.retrieve(nativeRequest)).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(expected);

        // When
        final User actual = resolver.resolveArgument(
            mock(MethodParameter.class),
            mock(ModelAndViewContainer.class),
            request,
            mock(WebDataBinderFactory.class)
        );

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Supports_user_parameters() {

        final MethodParameter parameter = mock(MethodParameter.class);

        final Class userClass = User.class;

        // Given
        given(parameter.getParameterType()).willReturn(userClass);

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, equalTo(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Does_not_support_user_sub_type_parameters() {

        final MethodParameter parameter = mock(MethodParameter.class);

        class SubUser extends User {
            public SubUser() {
                super(null, null);
            }
        }

        final Class userClass = SubUser.class;

        // Given
        given(parameter.getParameterType()).willReturn(userClass);

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, equalTo(false));
    }
}
