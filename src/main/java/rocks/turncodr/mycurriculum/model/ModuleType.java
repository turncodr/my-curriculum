package rocks.turncodr.mycurriculum.model;

/**
 *  Attribute for Module.
 */
public enum ModuleType {
    MAIN_MODULE(0),
    PLACEHOLDER_MODULE(1),
    ELECTIVE_MODULE(2);

    private int index;

    ModuleType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
