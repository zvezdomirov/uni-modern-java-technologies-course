import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WordAnalyzer {

    public static void main(String[] args) {
        System.out.println(getSharedLetters("house", "home"));
        System.out.println(getSharedLetters("Micky", "mouse"));
        System.out.println(getSharedLetters("house", "villa"));
    }

    public static String getSharedLetters(String word1, String word2) {
        List<Character> matchingChars = new ArrayList<>();
        boolean[] refTable = new boolean[125];
        for (char ch : word1.toLowerCase().toCharArray()) {
            refTable[ch % 125] = true;
        }
        char[] letters = word2.toLowerCase().toCharArray();
        for (int i = 0 ; i < letters.length; i++) {
            if (refTable[letters[i]]) {
                matchingChars.add(letters[i]);
            }
        }
        return matchingChars.stream()
                .sorted(Comparator.naturalOrder())
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
