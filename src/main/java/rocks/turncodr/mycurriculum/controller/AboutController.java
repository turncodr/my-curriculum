package rocks.turncodr.mycurriculum.controller;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @GetMapping("/about")
    public String about(Model model) {
        List<DegreeProgram> degreePrograms = degreeProgramService.findAll();
        logger.info(degreePrograms.toString());
        model.addAttribute("degreePrograms", degreePrograms);
        return "about";
    }
}
