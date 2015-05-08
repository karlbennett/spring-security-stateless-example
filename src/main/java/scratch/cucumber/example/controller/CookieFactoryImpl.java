package scratch.cucumber.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.HashFactory;

import javax.servlet.http.Cookie;

@Component
public class CookieFactoryImpl implements CookieFactory {

    private final HashFactory hashFactory;

    @Autowired
    public CookieFactoryImpl(HashFactory hashFactory) {
        this.hashFactory = hashFactory;
    }

    @Override
    public Cookie createSignIn(User signedInUser) {

        final String username = signedInUser.getUsername();
        final String token = hashFactory.encode(signedInUser.getPassword());

        return new Cookie("signIn", username + "|" + token);
    }
}
