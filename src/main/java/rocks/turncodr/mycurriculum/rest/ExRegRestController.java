package rocks.turncodr.mycurriculum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rocks.turncodr.mycurriculum.error.Message;
import rocks.turncodr.mycurriculum.json.ExRegSaveData;
import rocks.turncodr.mycurriculum.json.Response;
import rocks.turncodr.mycurriculum.model.Curriculum;
import rocks.turncodr.mycurriculum.model.ExReg;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.services.CurriculumJpaRepository;
import rocks.turncodr.mycurriculum.services.ExRegJpaRepository;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Method consuming a json and saving into a repository.
 */
@RestController
public class ExRegRestController {

    @Autowired
    private ExRegJpaRepository exRegJpaRepository;

    @Autowired
    private ModuleJpaRepository moduleJpaRepository;

    @Autowired
    private CurriculumJpaRepository curriculumJpaRepository;
    /**
     * Utility to get i18n without using the thymeleaf engine.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Creates a new exReg object and maps the selected modules to it.
     *
     * @param data
     * @return
     */
    @PostMapping(value = "/exreg/save", consumes = "application/json")
    public Response postExRegSave(@RequestBody ExRegSaveData data, Locale locale) {
        List<Message> messages = new ArrayList<>();

        // if the json list is empty, spring makes it null -> we want an empty list instead
        if (data.getModulesToBeMapped() == null) {
            data.setModulesToBeMapped(new ArrayList<>());
        }
        if (data.getNewModuleStubs() == null) {
            data.setNewModuleStubs(new ArrayList<>());
        }

        ExReg exReg = data.getExReg();

        //fetch the chosen Curriculum to map it
        Optional curriculumFetch = curriculumJpaRepository.findById(exReg.getCurriculum().getId());
        if (curriculumFetch.isPresent()) {
            Curriculum curriculum = (Curriculum) curriculumFetch.get();
            exReg.setCurriculum(curriculum);
        } else {
            String errorMessage = messageSource.getMessage("rest.exregCreate.curriculumDoesNotExistInDb", new String[]{}, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        //save new ExReg
        exReg = exRegJpaRepository.save(exReg);

        this.mapModuleStubs(exReg, data.getNewModuleStubs());

        this.mapExistingModules(exReg, data.getModulesToBeMapped(), messages, locale);

        String[] i18nParameters = new String[]{exReg.getName()};
        String message = messageSource.getMessage(messages.isEmpty() ? "rest.exregCreate.successfullyCreated" : "rest.exregCreate.createdWithWarnings", i18nParameters, locale);
        Message success = new Message(message, Message.Type.SUCCESS);
        messages.add(success);

        return new Response(null, messages, "/exreg/list");
    }

    /**
     * maps the already existing Modules to the new ExReg and saves them.
     *
     * @param exReg
     * @param existingModules
     * @param messages
     */
    private void mapExistingModules(ExReg exReg, List<Module> existingModules, List<Message> messages, Locale locale) {

        List<Module> modulesToBeMapped = new ArrayList<>();

        for (Module moduleToBeMapped : existingModules) {
            // fetch the module from database by id to ensure consistency in case changes happened in the meantime
            Optional possibleModule = moduleJpaRepository.findById(moduleToBeMapped.getId());
            if (possibleModule.isPresent()) {
                Module dbModule = (Module) possibleModule.get();

                if (dbModule.getExReg() != null && !dbModule.getExReg().equals(exReg)) {
                    // the module has already been mapped to a different ExReg
                    // in this case, the module will be skipped and a message will be displayed
                    String[] i18nParameters = new String[]{dbModule.getShortInfo(), dbModule.getExReg().getName(), exReg.getName()};
                    String message = messageSource.getMessage("rest.exregCreate.moduleIsAlreadyMapped", i18nParameters, locale);
                    Message warning = new Message(message, Message.Type.WARNING);
                    messages.add(warning);
                    continue;
                }

                dbModule.setExReg(exReg);
                dbModule.setSemester(moduleToBeMapped.getSemester());
                modulesToBeMapped.add(dbModule);

            } else {
                // the module was not found in the database
                // this does not cause the whole operation to fail, however the user should be made aware of it
                String[] i18nParameters = new String[]{moduleToBeMapped.getShortInfo()};
                String message = messageSource.getMessage("rest.exregCreate.moduleDoesNotExistInDb", i18nParameters, locale);
                Message warning = new Message(message, Message.Type.WARNING);
                messages.add(warning);
            }
        }
        moduleJpaRepository.saveAll(modulesToBeMapped);
    }

    private void mapModuleStubs(ExReg exReg, List<Module> newModuleStubs) {
        //map the new Module stubs to the new ExReg and save them to the DB
        for (Module module : newModuleStubs) {
            module.setExReg(exReg);
        }
        moduleJpaRepository.saveAll(newModuleStubs);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

}
