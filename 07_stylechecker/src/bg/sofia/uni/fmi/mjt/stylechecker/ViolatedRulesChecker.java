package bg.sofia.uni.fmi.mjt.stylechecker;

import bg.sofia.uni.fmi.mjt.stylechecker.enums.FixmeComments;

public class ViolatedRulesChecker {
    public String checkLineForMoreThanOneStatement(String lineToCheck) {
        String commentedLine = "";
        final String trimmedLine = lineToCheck.trim();
        if (!trimmedLine.matches("[^;]*\\s*;*")) {
            commentedLine = String.format("%s%n",
                    FixmeComments.LINE_WITH_MORE_THAN_ONE_STATEMENT.getComment());

        }
        return commentedLine;
    }

    public String checkLineForWrongPackageNaming(String lineToCheck) {
        String commentedLine = "";
        final String trimmedLine = lineToCheck.trim();
        if (trimmedLine.startsWith("package")) {
            if (!trimmedLine.matches("\\s*package\\s+[a-z.]*;\\s*")) {
                commentedLine = String.format("%s%n",
                        FixmeComments.WRONG_PACKAGE_NAMING.getComment());
            }
        }
        return commentedLine;
    }

    public String checkLineForWildCardInImport(String lineToCheck) {
        String commentedLine = "";
        final String trimmedLine = lineToCheck.trim();
        if (trimmedLine.startsWith("import") &&
                trimmedLine.contains("*")) {
            commentedLine = String.format("%s%n",
                    FixmeComments.WILDCARD_IN_IMPORT.getComment());
        }
        return commentedLine;
    }

    public String checkLineForOpenBracketAtBeginning(String lineToCheck) {
        String commentedLine = "";
        final String trimmedLine = lineToCheck.trim();
        if (trimmedLine.startsWith("{")) {
            commentedLine = String.format("%s%n",
                    FixmeComments.OPEN_BRACKET_ON_NEW_LINE.getComment());
        }
        return commentedLine;
    }

    public String checkLineForLengthMoreThanHundredChars(String lineToCheck) {
        String commentedLine = "";
        final String trimmedLine = lineToCheck.trim();
        final int maxLineLength = 100;
        if (trimmedLine.length() > maxLineLength &&
                !trimmedLine.startsWith("import")) {
            commentedLine = String.format("%s%n",
                    FixmeComments.LINE_LONGER_THAN_HUNDRED_CHARS.getComment());

        }
        return commentedLine;
    }
}
