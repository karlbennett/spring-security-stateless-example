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

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import scratch.cucumber.example.security.JwtTokenFactory;
import scratch.cucumber.example.security.TokenFactory;
import scratch.cucumber.example.security.spring.AuthenticationFactory;
import scratch.cucumber.example.security.spring.SecurityContextHolder;
import scratch.cucumber.example.security.spring.StatelessAuthenticationFilter;
import scratch.cucumber.example.security.spring.StatelessAuthenticationSuccessHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityContextHolder securityContextHolder;

    @Autowired
    private AuthenticationFactory authenticationFactory;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // The http.formLogin().defaultSuccessUrl("/path/") method is required when using stateless Spring Security
        // because the session cannot be used to redirect to the page that was requested while signed out. Unfortunately
        // using this configuration method will cause our custom success handler (below) to be overridden with the
        // default success handler. So to replicate the defaultSuccessUrl("/path/") configuration we will instead
        // correctly configure and delegate to the default success handler.
        final SimpleUrlAuthenticationSuccessHandler delegate = new SimpleUrlAuthenticationSuccessHandler();
        delegate.setDefaultTargetUrl("/spring/");

        // Make Spring Security stateless. This means no session will be created by Spring Security, nor will it use any
        // previously existing session.
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        // Disable the CSRF prevention because it requires the session, which of course is not available in a
        // stateless application. It also greatly complicates the requirements for the sign in POST request.
        http.csrf().disable();
        // Viewing any page requires authentication.
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin()
            // Viewing the sign in page does not require authentication.
            .loginPage("/spring/signIn").permitAll()
            // Override the sign in success handler with our stateless implementation. This will update the response
            // with any headers and cookies that are required for subsequent authenticated requests.
            .successHandler(new StatelessAuthenticationSuccessHandler(authenticationFactory, delegate));
        http.logout().logoutUrl("/spring/signOut").logoutSuccessUrl("/spring/");
        // Add our stateless authentication filter before the default sign in filter. The default sign in filter is
        // still used for the initial sign in, but if a user is authenticated we need to acknowledge this before it is
        // reached.
        http.addFilterBefore(
            new StatelessAuthenticationFilter(authenticationFactory, securityContextHolder),
            UsernamePasswordAuthenticationFilter.class
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set a custom user details service that reads the user credentials from an external data source. This will
        // only ever be used once within the Spring Security process, that is during the initial sign in. The
        // verification of all authenticated requests are stateless, that is it does not require access to any internal
        // or external state.
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public TokenFactory tokenFactory() {
        return new JwtTokenFactory("some secret string", Jwts.builder(), Jwts.parser());
    }
}
