package rocks.turncodr.mycurriculum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class directing to the about page of the application.
 *
 */
@Controller
public class AboutController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
