package rocks.turncodr.mycurriculum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/curriculum/create")
    public String createCurriculumForm(Model model) {
        model.addAttribute("curriculum", new Curriculum());
        return "curriculumCreate";
    }

    @PostMapping("/curriculum/create")
    public String curriculumSubmit(@ModelAttribute Curriculum curriculum) {
        curriculumJpaRepository.save(curriculum);
        return "redirect:/curriculum/list";
    }
}
