package rocks.turncodr.mycurriculum.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Comparator;

/**
 * Entity module.
 */
@Entity
public class Module {

    public static final Comparator<Module> ALPHABETICAL_ORDER = new Comparator<Module>() {
        public int compare(Module module1, Module module2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(module1.getCode(), module2.getCode());
            if (res == 0) {
                res = module1.getCode().compareTo(module2.getCode());
            }
            return res;
        }
    };
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private ExReg exReg;
    /**
     * The code/abbreviation for this module (e.g. 0011).
     */
    private String code;
    /**
     * The full name of the module without subtitle.
     */
    @NotEmpty
    private String title;
    /**
     * The subtitle of the module. May be empty.
     */
    private String subtitle;
    /**
     * How often the module is offered (winter semester/summer semester/both).
     */
    private String offerFrequency;
    /**
     * The professor or lecturer responsible for this module.
     */
    private String moduleCoordinator;
    /**
     * The lecturers that will teach this module.
     */
    private String lecturers;
    private String teachingLanguage;
    /**
     * ECTS awarded for passing this module.
     */
    @PositiveOrZero
    private int credits;
    /**
     * Requirements for enrolling in this module.
     */
    private String prerequisites;
    /**
     * Recommendation for other modules or the credit score a student should have before enrolling in this module.
     */
    private String recommendedPrerequisites;
    /**
     * Learning goals of this module and competencies acquired.
     */
    @Lob
    private String learningOutcomes;
    @Lob
    private String contents;
    /**
     * The mode the contents are taught in (e.g. seminar and tutorial) .
     */
    @Lob
    private String teachingMethodology;
    /**
     * A selection of relevant literature.
     */
    @Lob
    private String readingList;
    /**
     * The ExReg semester that the module is scheduled to be in.
     */
    private int semester;

    @NotNull
    @ManyToOne
    private AreaOfStudies areaOfStudies;

    /**
     * @return if subtitle is set: {@code code title. subtitle} <br/>
     * if subtitle is not set: {@code code title}
     */
    public String getShortInfo() {
        String shortInfo = code + " " + title;
        if (subtitle != null && !subtitle.isEmpty()) {
            shortInfo += ". " + subtitle;
        }
        return shortInfo;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getOfferFrequency() {
        return offerFrequency;
    }

    public void setOfferFrequency(String offerFrequency) {
        this.offerFrequency = offerFrequency;
    }

    public String getModuleCoordinator() {
        return moduleCoordinator;
    }

    public void setModuleCoordinator(String moduleCoordinator) {
        this.moduleCoordinator = moduleCoordinator;
    }

    public String getLecturers() {
        return lecturers;
    }

    public void setLecturers(String lecturers) {
        this.lecturers = lecturers;
    }

    public String getTeachingLanguage() {
        return teachingLanguage;
    }

    public void setTeachingLanguage(String teachingLanguage) {
        this.teachingLanguage = teachingLanguage;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getRecommendedPrerequisites() {
        return recommendedPrerequisites;
    }

    public void setRecommendedPrerequisites(String recommendedPrerequisites) {
        this.recommendedPrerequisites = recommendedPrerequisites;
    }

    public String getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(String learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTeachingMethodology() {
        return teachingMethodology;
    }

    public void setTeachingMethodology(String teachingMethodology) {
        this.teachingMethodology = teachingMethodology;
    }

    public String getReadingList() {
        return readingList;
    }

    public void setReadingList(String readingList) {
        this.readingList = readingList;
    }

    public ExReg getExReg() {
        return exReg;
    }

    public void setExReg(ExReg exReg) {
        this.exReg = exReg;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public AreaOfStudies getAreaOfStudies() {
        return areaOfStudies;
    }

    public void setAreaOfStudies(AreaOfStudies areaOfStudies) {
        this.areaOfStudies = areaOfStudies;
    }
}
