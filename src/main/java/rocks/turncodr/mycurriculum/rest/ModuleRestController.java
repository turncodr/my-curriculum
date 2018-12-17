package rocks.turncodr.mycurriculum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;

/**
 * Module Rest controller.
 *
 */
@Controller
public class ModuleRestController {

    @Autowired
    private ModuleJpaRepository moduleJpaRepository;

    @RequestMapping("/module/details/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String getModuleDetails(@PathVariable("id") String id, Model model) {
        Module module = moduleJpaRepository.getOne(Integer.parseInt(id));
        model.addAttribute("module", module);
        return "/fragments/popupModuleOverview :: showModuleDetails";
    }
}
