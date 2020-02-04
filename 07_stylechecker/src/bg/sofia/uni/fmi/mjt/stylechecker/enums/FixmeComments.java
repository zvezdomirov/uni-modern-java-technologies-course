package bg.sofia.uni.fmi.mjt.stylechecker.enums;

public enum FixmeComments {
    WILDCARD_IN_IMPORT(
            "// FIXME Wildcards are not allowed in import statements"),
    LINE_WITH_MORE_THAN_ONE_STATEMENT(
            "// FIXME Only one statement per line is allowed"),
    LINE_LONGER_THAN_HUNDRED_CHARS(
            "// FIXME Length of line should not exceed 100 characters"),
    OPEN_BRACKET_ON_NEW_LINE(
            "// FIXME Opening brackets should be placed on the same line as the declaration"),
    WRONG_PACKAGE_NAMING(
            "// FIXME Package name should not contain upper-case letters or underscores");

    private String comment;

    FixmeComments(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }
}
