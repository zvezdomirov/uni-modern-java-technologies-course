package bg.sofia.uni.fmi.mjt.authapp.server;

public enum CommandFormat {
    REGISTER("register --username [^\\s]+ --password [^\\s]+ --first-name " +
            "[^\\s]+ --last-name [^\\s]+ --email [^\\s]+\\@[^\\s]+\\.[^\\s]+"),

    LOGIN("login --username [^\\s]+ --password [^\\s]+|login --session-id [^\\s]+"),

    UPDATE_USER("update-user --session-id [^\\s]+( --new-username [^\\s]+){0,1}" +
            "( --new-first-name [^\\s]+){0,1}( --new-last-name [^\\s]+){0,1}" +
            "( --new-email [^\\s]+\\@[^\\s]+.[^\\s]+){0,1}"),

    ADD_ADMIN_USER("add-admin-user --session-id [^\\s]+ --username [^\\s]+"),

    REMOVE_ADMIN_USER("remove-admin-user --session-id [^\\s]+ --username [^\\s]+"),

    DELETE_USER("delete-user --session-id [^\\s]+ --username [^\\s]+"),

    LOGOUT("logout --session-id [^\\s]+"),

    DISCONNECT("^\\s*disconnect\\s*$"),

    RESET_PASSWORD("reset-password --session-id [^\\s]+ --username [^\\s]+ --old-password [^\\s]+ --new-password [^\\s]+");

    private String pattern;

    CommandFormat(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
