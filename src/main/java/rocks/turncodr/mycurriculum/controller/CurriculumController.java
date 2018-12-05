package rocks.turncodr.mycurriculum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import rocks.turncodr.mycurriculum.model.Curriculum;
import rocks.turncodr.mycurriculum.services.CurriculumJpaRepository;

/**
 * Controller class directing to the about page of the application.
 *
 */
@Controller
public class CurriculumController {
    @Autowired
    private CurriculumJpaRepository curriculumJpaRepository;

    @GetMapping("/curriculum/list")
    public String getCurriculumList(Model model) {
        List<Curriculum> curricula = curriculumJpaRepository.findAll();
        model.addAttribute("curricula", curricula);
        return "curriculumList";
    }
}
