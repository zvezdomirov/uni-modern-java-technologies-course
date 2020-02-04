package bg.sofia.uni.fmi.mjt.authapp.auditlog;

public enum LogType {
    ADD_ADMIN("Add Admin"),
    REMOVE_ADMIN("Remove admin"),
    FAILED_LOGIN("Failed Login");

    private String name;

    LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
