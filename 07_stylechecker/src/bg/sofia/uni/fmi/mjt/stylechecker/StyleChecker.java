package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.stream.Collectors;

/**
 * Checks adherence to Java style guidelines.
 */
public class StyleChecker {

    /**
     * For each line from the given {@code source} performs code style checks
     * and writes to the {@code output}
     * 1. a comment line for each style violation in the input line, if any
     * 2. the input line itself.
     *
     * @param source
     * @param output
     */
    public void checkStyle(Reader source, Writer output) {
        try (BufferedReader br = new BufferedReader(source);
             BufferedWriter bw = new BufferedWriter(output)) {
            final ViolatedRulesCommenter commenter = new ViolatedRulesCommenter();
            final String commentedCode = br.lines()
                    .map(commenter::comment)
                    .collect(Collectors.joining(""));
            bw.write(commentedCode.strip());
        } catch (IOException e) {
            System.err.println("Something went wrong when trying to read from source.");
            e.printStackTrace();
        }
    }

}