package bg.sofia.uni.fmi.mjt.jira.enums;

public enum WorkAction {
    RESEARCH("research"),
    DESIGN("design"),
    IMPLEMENTATION("implementation"),
    TESTS("tests"),
    FIX("fix");

    private String name;
    WorkAction(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}
