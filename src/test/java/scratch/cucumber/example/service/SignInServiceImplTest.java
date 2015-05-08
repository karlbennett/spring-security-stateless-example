package scratch.cucumber.example.service;

import org.junit.Before;
import org.junit.Test;
import scratch.cucumber.example.domain.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SignInServiceImplTest {

    private UserRepository userRepository;
    private SignInServiceImpl service;
    private HashFactory hashFactory;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        hashFactory = mock(HashFactory.class);
        service = new SignInServiceImpl(userRepository, hashFactory);
    }

    @Test
    public void canSignInWithValidPassword() {

        String username = "username";
        String password = "password";
        User expected = mock(User.class);

        //Given
        given(userRepository.findByUsername(username)).willReturn(expected);
        given(expected.getPassword()).willReturn(password);

        //When
        final User actual = service.signIn(username, password);

        //Then
        assertThat(actual, equalTo(expected));

    }

    @Test(expected = SignInException.class)
    public void canNotSignInWithInvalidPassword() {

        String username = "username";
        String password = "invalid";
        User expected = mock(User.class);

        //Given
        given(userRepository.findByUsername(username)).willReturn(expected);
        given(expected.getPassword()).willReturn("valid");

        //When
        service.signIn(username, password);
    }

    @Test
    public void canFindUsernameByToken() {

        User user = mock(User.class);
        String password = "password";
        String passwordhash = "passwordhash";
        String expected = "username";

        //Given
        given(userRepository.findByUsername(expected)).willReturn(user);
        given(user.getPassword()).willReturn(password);
        given(user.getUsername()).willReturn(expected);
        given(hashFactory.encode(password)).willReturn(passwordhash);

        //When
        final String actual = service.findUsernameByToken(expected + "|" + passwordhash);

        //Then
        assertThat(actual, equalTo(expected));

    }


    @Test
    public void cannotFindUsernameByInvalidToken() {

        User user = mock(User.class);
        String password = "password";
        String passwordhash = "passwordhash";
        String expected = "username";

        //Given
        given(userRepository.findByUsername(expected)).willReturn(user);
        given(user.getPassword()).willReturn(password);
        given(hashFactory.encode(password)).willReturn(passwordhash);

        //When
        final String actual = service.findUsernameByToken(expected + "|invalidHash");

        //Then
        assertThat(actual, nullValue());

    }
}