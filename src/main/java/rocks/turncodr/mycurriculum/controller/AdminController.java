package rocks.turncodr.mycurriculum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rocks.turncodr.mycurriculum.model.Curriculum;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.services.CurriculumJpaRepository;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;

import java.util.List;

/**
 * Controller class directing to the admin page.
 * Secured with username and password.
 */
@Controller
public class AdminController {

    @Autowired
    private CurriculumJpaRepository curriculumRepository;

    @Autowired
    private ModuleJpaRepository moduleRepository;

    /**
     * Loads all modules and curricula and adds them to the model. Then, it returns the index.html
     * @param model the spring boot model
     * @return the index.html for the admin page
     */
    @GetMapping("/admin")
    public String getAdminHome(Model model) {
        List<Curriculum> curricula = curriculumRepository.findAll();
        List<Module> modules = moduleRepository.findAll();
        model.addAttribute("moduleList", modules);
        model.addAttribute("curricula", curricula);
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
