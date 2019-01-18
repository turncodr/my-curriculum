package rocks.turncodr.mycurriculum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import rocks.turncodr.mycurriculum.model.AreaOfStudies;
import rocks.turncodr.mycurriculum.model.Curriculum;
import rocks.turncodr.mycurriculum.model.ExReg;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.model.Syllabus;
import rocks.turncodr.mycurriculum.model.Syllabus.Semester;
import rocks.turncodr.mycurriculum.services.AreaOfStudiesJpaRepository;
import rocks.turncodr.mycurriculum.services.CurriculumJpaRepository;
import rocks.turncodr.mycurriculum.services.ExRegJpaRepository;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;
import rocks.turncodr.mycurriculum.services.PdfConfigManager;
import rocks.turncodr.mycurriculum.services.PdfGeneratorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Controller for the Examination Regulations sites.
 */
@Controller
public class ExRegController {

    @Autowired
    private ModuleJpaRepository moduleJpaRepository;

    @Autowired
    private ExRegJpaRepository exRegJpaRepository;

    @Autowired
    private CurriculumJpaRepository curriculumService;

    @Autowired
    private AreaOfStudiesJpaRepository areaOfStudiesJpaRepository;

    @Autowired
    private PdfGeneratorUtil pdfGeneratorUtil;

    @Autowired
    private PdfConfigManager pdfConfigManager;

    @GetMapping("/exreg/create")
    public String getExRegCreate(Model model) {
        // fetching not mapped modules in a moduleList by selecting only modules, where
        // exReg-attribute = null
        List<Module> moduleList = moduleJpaRepository.findByExReg(null);
        List<AreaOfStudies> areaOfStudiesList = areaOfStudiesJpaRepository.findAll();
        HashMap<Integer, AreaOfStudies> areaOfStudiesMap = new HashMap<>();
        model.addAttribute("moduleList", moduleList);
        for (AreaOfStudies areaOfStudies : areaOfStudiesList) {
            areaOfStudiesMap.put(areaOfStudies.getId(), areaOfStudies);
        }
        model.addAttribute("areaOfStudiesMap", areaOfStudiesMap);

        List<Curriculum> curriculumList = curriculumService.findAll();
        model.addAttribute("curriculumList", curriculumList);

        return "exregCreate";
    }

    @GetMapping("/exreg/syllabus")
    public String getExRegSyllabus(@RequestParam(value = "id", required = false, defaultValue = "0") String idFromUrl, Model model) {
        Integer id;
        try {
            id = Integer.parseInt(idFromUrl);
        } catch (NumberFormatException e) {
            id = 0;
        }
        //find exreg for the given id
        Optional<ExReg> exregResult = exRegJpaRepository.findById(id);
        if (exregResult.isPresent()) {
            Map<String, Object> exregData = getAndSetExregData(exregResult);
            for (Entry<String, ?> entry : exregData.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }
        } else {
            //the given id has no corresponding exreg --> display error message
            model.addAttribute("error", "exregSyllabus.exregDoesntExist");
        }
        return "exregSyllabus";
    }

    @RequestMapping(value = "/exreg/syllabus/pdf", method = RequestMethod.GET) //produces="application/pdf"
    public ResponseEntity<?> getExRegSyllabusPdf(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        Optional<ExReg> exregResult = exRegJpaRepository.findById(id);
        if (exregResult.isPresent()) {
            Map<String, Object> data = getAndSetExregData(exregResult);
            HashMap<String, String> textStyles = pdfConfigManager.getAllFonts();
            data.put("textStyles", textStyles);
            return pdfGeneratorUtil.createPdfDownload("exregSyllabusPdf", data, request, response);
        } else {
            //the given id has no corresponding exreg --> display error message
            //TODO
            return new ResponseEntity<>("ERROR 404: THE REQUESTED RESOURCE DOESN'T EXIST!", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Returns a Map which contains all the data for an exreg.
     */
    private Map<String, Object> getAndSetExregData(Optional<ExReg> exregResult) {
        Map<String, Object> exregData = new HashMap<>();
        ExReg exreg = exregResult.get();

        //find modules for the given exreg
        List<Module> moduleList = moduleJpaRepository.findByExReg(exreg);
        exregData.put("exreg", exreg);

        Map<Integer, List<Module>> semesterMap = new HashMap<>();
        for (Module module : moduleList) {
            int semester = module.getSemester();
            List<Module> modules = semesterMap.get(semester);

            if (modules == null) {
                modules = new ArrayList<>();
                semesterMap.put(semester, modules);
            }
            modules.add(module);
        }
        List<Integer> keys = new ArrayList<>(semesterMap.keySet());
        Collections.sort(keys);
        Syllabus syllabus = new Syllabus();

        for (Integer key : keys) {
            List<Module> modules = semesterMap.get(key);
            Collections.sort(modules, Module.ALPHABETICAL_ORDER);
            Semester semester = new Semester(modules, key);
            syllabus.getSemesters().add(semester);
        }
        exregData.put("syllabus", syllabus);
        return exregData;
    }

    @GetMapping("/exreg/list")
    public String getExRegList(Model model, @RequestParam(value = "curriculum", required = false) Integer cid) {
        List<ExReg> exRegList = new LinkedList<>();
        if (cid == null) {
            exRegList = exRegJpaRepository.findAll();
        } else {
            if (curriculumService.existsById(cid)) {
                Optional<Curriculum> curriculum = curriculumService.findById(cid);
                model.addAttribute("curriculum", curriculum.get());
                exRegList = exRegJpaRepository.findByCurriculum(curriculum.get());
            }
        }
        model.addAttribute("exRegList", exRegList);
        return "exregList";
    }

    @GetMapping("/exreg/overview")
    public String getExregOverview(@RequestParam(value = "id", required = false, defaultValue = "0") String urlId, Model model) {
        Integer id = Integer.parseInt(urlId);
        Optional<ExReg> exregResult = exRegJpaRepository.findById(id);
        if (exRegJpaRepository.existsById(id)) {
            ExReg exreg = exregResult.get();
            model.addAttribute("exRegSemester", getNumberOfSemesters(exreg));
            model.addAttribute("exRegModules", moduleJpaRepository.findByExReg(exreg));
            model.addAttribute("exreg", exreg);

            TreeSet<AreaOfStudies> areaOfStudiesUsed = new TreeSet<>();
            for (Module module : moduleJpaRepository.findByExReg(exreg)) {
                areaOfStudiesUsed.add(module.getAreaOfStudies());
            }
            model.addAttribute("areaOfStudiesUsed", areaOfStudiesUsed);
        } else {
            model.addAttribute("error", "exregSyllabus.exregDoesntExist");
        }
        return "exregOverview";
    }

    private int getNumberOfSemesters(ExReg exreg) {
        int semesters = 0;
        for (Module module : moduleJpaRepository.findByExReg(exreg)) {
            int moduleSemester = module.getSemester();
            semesters = (moduleSemester > semesters) ? moduleSemester : semesters;
        }
        return semesters;
    }
}
