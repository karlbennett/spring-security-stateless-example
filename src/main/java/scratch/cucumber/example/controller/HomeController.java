package scratch.cucumber.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import scratch.cucumber.example.service.SignInService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {

    private final SignInService signInService;

    @Autowired
    public HomeController(SignInService signInService) {
        this.signInService = signInService;
    }

    @RequestMapping(method = GET)
    public ModelAndView display(@CookieValue("signIn") String token) {

        final String username = signInService.findUsernameByToken(token);

        return new ModelAndView("home").addObject("username", username);
    }
}
