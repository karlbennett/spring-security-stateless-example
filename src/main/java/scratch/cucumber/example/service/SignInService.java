package scratch.cucumber.example.service;

import scratch.cucumber.example.domain.User;

public interface SignInService {

    User signIn(String username, String password) throws SignInException;

    String findUsernameByToken(String token);
}
