package scratch.cucumber.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import scratch.cucumber.example.domain.User;
import scratch.cucumber.example.service.SignInService;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "signIn")
public class SignInController {

    private final SignInService signInService;
    private final CookieFactory cookieFactory;

    @Autowired
    public SignInController(SignInService signInService, CookieFactory cookieFactory) {
        this.signInService = signInService;
        this.cookieFactory = cookieFactory;
    }

    @RequestMapping(method = GET)
    public String display() {
        return "signIn";
    }

    @RequestMapping(method = POST)
    public String signIn(String username, String password, HttpServletResponse response) {

        final User user = signInService.signIn(username, password);

        response.addCookie(cookieFactory.createSignIn(user));

        return "redirect:";
    }
}
