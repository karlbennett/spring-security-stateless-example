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

package scratch.cucumber.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import scratch.cucumber.example.data.UserRepository;
import scratch.cucumber.example.security.ApplicationFormUrlEncodedUserFactory;
import scratch.cucumber.example.security.ApplicationJsonUserFactory;
import scratch.cucumber.example.security.AuthenticationFactory;
import scratch.cucumber.example.security.ContentTypeUserFactory;
import scratch.cucumber.example.security.HttpServletRequestHttpInputMessageFactory;
import scratch.cucumber.example.security.JwtTokenFactory;
import scratch.cucumber.example.security.MultiValueMapUserFactory;
import scratch.cucumber.example.security.SecurityContextHolder;
import scratch.cucumber.example.security.StatelessSignInFilter;
import scratch.cucumber.example.security.UserAuthenticationFactory;
import scratch.cucumber.example.security.UserFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityContextHolder securityContextHolder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/spring/signIn").permitAll();
        http.logout().logoutUrl("/spring/signOut").logoutSuccessUrl("/spring/");
        http.addFilterBefore(
            new StatelessSignInFilter(
                new AntPathRequestMatcher("/spring/signIn"),
                authenticationFactory(),
                authenticationManagerBean(),
                userRepository,
                securityContextHolder
            ),
            UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean
    public AuthenticationFactory authenticationFactory() {
        return new UserAuthenticationFactory(
            new ContentTypeUserFactory(new HashMap<MediaType, UserFactory<HttpServletRequest>>() {{
                put(
                    APPLICATION_FORM_URLENCODED,
                    new ApplicationFormUrlEncodedUserFactory(
                        new HttpServletRequestHttpInputMessageFactory(),
                        new FormHttpMessageConverter(),
                        new MultiValueMapUserFactory()
                    ));
                put(APPLICATION_JSON, new ApplicationJsonUserFactory(new ObjectMapper()));
            }}),
            new JwtTokenFactory(Jwts.builder(), "some secret string")
        );
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // This override is required to properly configure the AuthenticationManager above. If it is not present then
        // the wiring will fail.
    }
}
