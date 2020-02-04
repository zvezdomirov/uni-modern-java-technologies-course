package bg.sofia.uni.fmi.mjt.stylechecker;

import static org.junit.Assert.assertEquals;


import bg.sofia.uni.fmi.mjt.stylechecker.enums.FixmeComments;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;

public class StyleCheckerTest {
    public static final String WILDCARD_IN_IMPORT = "import java.util.*;";
    public static final String LINE_WITH_MORE_THAN_ONE_STATEMENT =
            "doSomething(); counter++; doSomethingElse(counter);";
    public static final String LINE_LONGER_THAN_HUNDRED_CHARS =
            "String hello = \"Hey, it's Hannah, Hannah Baker. That's right. Don't adjust your... " +
                    "whatever device you're listening to this on. It's me, live and in stereo.\";";
    public static final String OPEN_BRACKET_ON_NEW_LINE =
            String.format("public static void main(String... args)%n{%n}");
    public static final String UPPER_CASE_LETTER_IN_PACKAGE_NAME = "package bg.uni.Sofia.fmi.mjt;";
    public static final String UNDERSCORE_IN_PACKAGE_NAME = "package bg.uni_sofia.fmi.mjt;";
    private static final StyleChecker CHECKER = new StyleChecker();
    private StringReader reader;
    private StringWriter writer;

    @Before
    public void initWriter() {
        this.writer = new StringWriter();
    }

    @Test
    public void checkStyleWildCardInImportGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(WILDCARD_IN_IMPORT);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s",
                FixmeComments.WILDCARD_IN_IMPORT.getComment(),
                WILDCARD_IN_IMPORT);

        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleMoreThanOneStatementPerLineGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(LINE_WITH_MORE_THAN_ONE_STATEMENT);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s",
                FixmeComments.LINE_WITH_MORE_THAN_ONE_STATEMENT.getComment(),
                LINE_WITH_MORE_THAN_ONE_STATEMENT);

        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleOneStatementWithManySemicolonsGivenShouldNotAppendComment() {
        // Arrange
        final String testLine = "doSomething;;;;";
        this.reader = new StringReader(testLine);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(testLine, actual);
    }

    @Test
    public void checkStyleLineLongerThanHundredCharsGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(LINE_LONGER_THAN_HUNDRED_CHARS);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s",
                FixmeComments.LINE_LONGER_THAN_HUNDRED_CHARS.getComment(),
                LINE_LONGER_THAN_HUNDRED_CHARS);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleImportLineLongerThanHundredCharsGivenShouldNotAppendComment() {
        // Arrange
        final String testLine = "import package.package.package.package.package.package" +
                ".package.package.package.package.package.package.package.package.package.package" +
                ".package.package.package.package;";
        this.reader = new StringReader(testLine);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(testLine, actual);
    }

    @Test
    public void checkStyleOpenBracketOnNewLineGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(OPEN_BRACKET_ON_NEW_LINE);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "public static void main(String... args)%n%s%n{%n}",
                FixmeComments.OPEN_BRACKET_ON_NEW_LINE.getComment());
        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleUpperCaseLetterInPackageNameGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(UPPER_CASE_LETTER_IN_PACKAGE_NAME);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s",
                FixmeComments.WRONG_PACKAGE_NAMING.getComment(),
                UPPER_CASE_LETTER_IN_PACKAGE_NAME);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleUnderscoreInPackageNameGivenShouldAppendComment() {
        // Arrange
        this.reader = new StringReader(UNDERSCORE_IN_PACKAGE_NAME);

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s",
                FixmeComments.WRONG_PACKAGE_NAMING.getComment(),
                UNDERSCORE_IN_PACKAGE_NAME);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void checkStyleInputWithMoreThanOneRuleViolatedGivenShouldAppendMultipleComments() {
        // Arrange
        final String firstViolated = "package bg.uni_sofia.fmi.mjt;";
        final String secondViolated = "import java.lang.*;";
        this.reader = new StringReader(
                String.format("%s%n%s",
                        firstViolated,
                        secondViolated));

        // Act
        CHECKER.checkStyle(this.reader, this.writer);
        final String expected = String.format(
                "%s%n%s%n%s%n%s",
                FixmeComments.WRONG_PACKAGE_NAMING.getComment(),
                firstViolated,
                FixmeComments.WILDCARD_IN_IMPORT.getComment(),
                secondViolated);
        final String actual = this.writer.toString();

        // Assert
        assertEquals(expected, actual);
    }
}
