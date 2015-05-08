package scratch.cucumber.example.controller;

import scratch.cucumber.example.domain.User;

import javax.servlet.http.Cookie;

public interface CookieFactory {

    public Cookie createSignIn(User signedInUser);
}
