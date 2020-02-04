import bg.sofia.uni.fmi.mjt.authorship.detection.AuthorshipDetectorImpl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        final double[] featureWeights = {10, 30, 50, 1, 4};
        AuthorshipDetectorImpl detector =
                new AuthorshipDetectorImpl(
                        new FileInputStream("./resources/knownSignatures.txt"),
                        featureWeights);
//        detector.calculateSignature
//                (new FileInputStream("./testresources/sampleText.txt"));

        System.out.println(detector.findAuthor(new FileInputStream("./resources/mysteryFiles/mystery5.txt")));
    }
}