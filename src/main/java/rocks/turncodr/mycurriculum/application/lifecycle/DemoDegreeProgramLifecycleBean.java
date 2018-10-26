package rocks.turncodr.mycurriculum.application.lifecycle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import rocks.turncodr.mycurriculum.model.DegreeProgram;
import rocks.turncodr.mycurriculum.model.Module;
import rocks.turncodr.mycurriculum.services.DegreeProgramJpaRepository;
import rocks.turncodr.mycurriculum.services.ModuleJpaRepository;

/**
 * Lifecycle bean that creates demo degree programs.
 *
 *
 */
@Component
public class DemoDegreeProgramLifecycleBean implements SmartLifecycle {

    private boolean running;

    @Autowired
    private DegreeProgramJpaRepository degreeProgramService;
    @Autowired
    private ModuleJpaRepository moduleJpaRepository;

    @Override
    public void start() {
        running = true;
        this.createDegreePrograms();
        this.createModules();
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void createDegreePrograms() {
        List<DegreeProgram> degreePrograms = degreeProgramService.findAll();
        if (degreePrograms.size() == 0) {
            DegreeProgram wib = new DegreeProgram();
            wib.setName("Wirtschaftsinformatik");
            wib.setDegree("Bachelor of Science (BSc.)");
            wib.setNumberOfSemesters(7);
            degreePrograms.add(wib);
            DegreeProgram wim = new DegreeProgram();
            wim.setName("Wirtschaftsinformatik");
            wim.setDegree("Master of Science (MSc.)");
            wim.setNumberOfSemesters(3);
            degreePrograms.add(wim);
            degreeProgramService.saveAll(degreePrograms);
        }
    }

    /**
     * Creates example modules and saves them to the database.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    private void createModules() {
        List<Module> modules = moduleJpaRepository.findAll();

        if (modules.isEmpty()) {
            Module module1 = new Module();
            module1.setCode("0081");
            module1.setTitle("Fortgeschrittene Programmierung");
            module1.setSubtitle("");
            module1.setOfferFrequency("jedes Semester");
            module1.setModuleCoordinator("Prof. Dr. rer. nat. Martin Schmollinger");
            module1.setLecturers("Prof. Dr. rer. nat. Martin Schmollinger");
            module1.setTeachingLanguage("deutsch");
            module1.setCredits(7);
            module1.setPrerequisites("0031");
            module1.setRecommendedPrerequisites("0011, 0041");
            module1.setLearningOutcomes("Vertiefung der Informatik-Grundlagen");
            module1.setContents("Entwicklung objektorientierter Programme");
            module1.setTeachingMethodology("Vorlesung im seminaristischen [sic!] Stil.");
            module1.setReadingList("Sprechen Sie Java?");
            modules.add(module1);

            Module module2 = new Module();
            module2.setCode("0091");
            module2.setTitle("Wirtschaftsmathematik");
            module2.setSubtitle("");
            module2.setOfferFrequency("jedes Semester");
            module2.setModuleCoordinator("Prof. Dr. Bernhard Mößner");
            module2.setLecturers("Frau Gisela Filip");
            module2.setTeachingLanguage("deutsch");
            module2.setCredits(8);
            module2.setPrerequisites("keine");
            module2.setRecommendedPrerequisites("0041");
            module2.setLearningOutcomes("Solide BWL-Grundlagen");
            module2.setContents("Kenntnis der grundlegenden Konzepte");
            module2.setTeachingMethodology("Vorlesung mit begleitendem Praktikum.");
            module2.setReadingList("Mathematik für Wirtschaftswissenschaftler");
            modules.add(module2);

            moduleJpaRepository.saveAll(modules);
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        // TODO Auto-generated method stub
        return running;
    }

    @Override
    public int getPhase() {
        return 1;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        running = false;
        callback.run();
    }
}
