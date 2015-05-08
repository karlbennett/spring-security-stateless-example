package scratch.cucumber.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scratch.cucumber.example.domain.User;

@Component
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;
    private final HashFactory hashFactory;

    @Autowired
    public SignInServiceImpl(UserRepository userRepository, HashFactory hashFactory) {
        this.userRepository = userRepository;
        this.hashFactory = hashFactory;
    }

    @Override
    public User signIn(String username, String password) {

        final User user = userRepository.findByUsername(username);

        if (user.getPassword().equals(password)) {
            return user;
        }

        throw new SignInException();
    }

    @Override
    public String findUsernameByToken(String token) {

        final String[] tokenSplit = token.split("\\|");

        final User user = userRepository.findByUsername(tokenSplit[0]);

        final String password = user.getPassword();

        if (tokenSplit[1].equals(hashFactory.encode(password))) {
            return user.getUsername();
        }

        return null;
    }
}
