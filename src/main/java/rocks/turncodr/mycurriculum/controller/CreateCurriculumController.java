package rocks.turncodr.mycurriculum.controller;

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
public class CreateCurriculumController {
    String textField;
    @Autowired
    private CurriculumJpaRepository curriculumService;
    @GetMapping("/createCurriculum")
    public String createCurriculumForm(Model model) {
        model.addAttribute("curriculum", new Curriculum());
        return "createCurriculum";
    }
    @PostMapping("/createCurriculum")
    public String curriculumSubmit(@ModelAttribute Curriculum curriculum) {
        curriculumService.save(curriculum);
        return "redirect:/curriculum/list";
    }
}
