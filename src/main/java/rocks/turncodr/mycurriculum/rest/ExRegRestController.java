package rocks.turncodr.mycurriculum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rocks.turncodr.mycurriculum.model.Curriculum;
import rocks.turncodr.mycurriculum.model.ExReg;
import rocks.turncodr.mycurriculum.model.ExRegSaveData;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.services.CurriculumJpaRepository;
import rocks.turncodr.mycurriculum.services.ExRegJpaRepository;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     * Creates a new exReg object and maps the selected modules to it.
     *
     * @param data
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/exreg/save", consumes = "application/json")
    public ResponseEntity<String> postExRegSave(@RequestBody ExRegSaveData data) {
        // if the json list is empty spring makes it null -> we want an empty list instead
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
            throw new IllegalArgumentException("Curriculum to be mapped does not exist in DB.");
        }

        //save new ExReg
        exReg = exRegJpaRepository.save(exReg);

        //map the new Module stubs to the new ExReg and save them to the DB
        for (Module module : data.getNewModuleStubs()) {
            module.setExReg(exReg);
        }
        moduleJpaRepository.saveAll(data.getNewModuleStubs());

        //map the already existing Modules to the new ExReg and save them
        List<Module> modulesToBeMapped = new ArrayList<>();
        for (Module moduleToBeMapped : data.getModulesToBeMapped()) {
            // fetch the module from database by id to ensure consistency in case changes happened in the meantime
            Optional possibleModule = moduleJpaRepository.findById(moduleToBeMapped.getId());
            if (possibleModule.isPresent()) {
                Module dbModule = (Module) possibleModule.get();
                dbModule.setExReg(exReg);
                dbModule.setSemester(moduleToBeMapped.getSemester());
                modulesToBeMapped.add(dbModule);
            } else {
                throw new IllegalArgumentException("Module to be mapped does not exist in DB.");
            }
        }
        moduleJpaRepository.saveAll(modulesToBeMapped);

        return new ResponseEntity<>("{\"status\": \"ExReg '" + exReg.getName() + "' was successfully created\"}", HttpStatus.OK);
    }

    @ExceptionHandler
    void handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

}
