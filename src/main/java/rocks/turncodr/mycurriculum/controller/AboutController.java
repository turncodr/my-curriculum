package rocks.turncodr.mycurriculum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import rocks.turncodr.mycurriculum.model.DegreeProgram;
import rocks.turncodr.mycurriculum.services.DegreeProgramJpaRepository;

/**
 * Controller class directing to the about page of the application.
 *
 */
@Controller
public class AboutController {
    @Autowired
    private DegreeProgramJpaRepository degreeProgramService;

    @GetMapping("/about")
    public String about(Model model) {
        List<DegreeProgram> degreePrograms = degreeProgramService.findAll();
        model.addAttribute("degreePrograms", degreePrograms);
        return "about";
    }
}
