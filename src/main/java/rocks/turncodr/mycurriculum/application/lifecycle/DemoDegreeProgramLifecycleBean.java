package rocks.turncodr.mycurriculum.application.lifecycle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import rocks.turncodr.mycurriculum.model.DegreeProgram;
import rocks.turncodr.mycurriculum.services.DegreeProgramJpaRepository;

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

    @Override
    public void start() {
        running = true;
        this.createDegreePrograms();
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
