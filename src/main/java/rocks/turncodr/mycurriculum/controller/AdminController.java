package rocks.turncodr.mycurriculum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class directing to the admin page.
 * Secured with username and password.
 */
@Controller
public class AdminController {
    @GetMapping("/admin")
    public String getAdminHome() {
        return "admin/index";
    }

    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/accessDenied";
    }
}
