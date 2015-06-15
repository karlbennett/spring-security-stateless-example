package scratch.cucumber.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import scratch.cucumber.example.domain.User;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = GET)
    public ModelAndView display(User user) {
        return new ModelAndView("home").addObject("username", user.getUsername());
    }
}
