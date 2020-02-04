package bg.sofia.uni.fmi.mjt.authorship.detection;

import static org.junit.Assert.assertEquals;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class AuthorshipDetectorTest {

    private static final String SIGNATURES_FILE =
            "./testresources/knownSignaturesTest.txt";

    private static final String SAMPLE_TEXT_PATH =
            "./testresources/sampleText.txt";

    private static final double[] FEATURE_WEIGHTS = {10, 30, 50, 1, 4};

    private AuthorshipDetector detector;

    @Before
    public void init() throws FileNotFoundException {
        this.detector =
                new AuthorshipDetectorImpl(
                        new FileInputStream(SIGNATURES_FILE),
                        FEATURE_WEIGHTS);
    }

    @Test
    public void calculateSimilarityShouldCalculateCorrectly() {
        // Arrange
        final List<LinguisticSignature> testSignatures = this.detector
                .getAuthorSignatures()
                .values().stream()
                .limit(2)
                .collect(Collectors.toList());
        final double expected = 104;

        // Act
        double actual = this.detector.calculateSimilarity(
                testSignatures.get(0), testSignatures.get(1));

        // Assert
        assertEquals(expected, actual, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateSimilarityNullFirstParameterGivenShouldThrowException() {
        // Act
        this.detector.calculateSimilarity(
                null, new LinguisticSignature(Collections.emptyMap()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateSimilarityNullSecondParameterGivenShouldThrowException() {
        // Act
        this.detector.calculateSimilarity(
                new LinguisticSignature(Collections.emptyMap()), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateSignatureNullParameterGivenShouldThrowException() {
        // Act
        this.detector.calculateSignature(null);
    }

    @Test
    public void calculateSignatureShouldWorkCorrectly() throws FileNotFoundException {
        // Arrange
        final LinguisticSignature expected = getPrecomputedSignature();

        // Act
        LinguisticSignature actual = this.detector.calculateSignature(
                new FileInputStream(SAMPLE_TEXT_PATH));

        // Assert
        final FeatureType[] featureTypes = FeatureType.values();
        Map<FeatureType, Double> expectedFeatures = expected.getFeatures();
        Map<FeatureType, Double> actualFeatures = actual.getFeatures();
        for (FeatureType featureType : featureTypes) {
            assertEquals(
                    expectedFeatures.get(featureType),
                    actualFeatures.get(featureType));
        }
    }

    private LinguisticSignature getPrecomputedSignature() {
        final double avgWordLength = 3.9615384615384617;
        final double typeTokenRatio = 0.7692307692307693;
        final double hapaxLegomenaRatio = 0.5769230769230769;
        final double avgSentenceLength = 6.5;
        final double avgSentenceComplexity = 1.5;
        Map<FeatureType, Double> typeValues = new HashMap<>() {
            {
                put(FeatureType.AVERAGE_WORD_LENGTH, avgWordLength);
                put(FeatureType.TYPE_TOKEN_RATIO, typeTokenRatio);
                put(FeatureType.HAPAX_LEGOMENA_RATIO, hapaxLegomenaRatio);
                put(FeatureType.AVERAGE_SENTENCE_LENGTH, avgSentenceLength);
                put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, avgSentenceComplexity);
            }
        };
        return new LinguisticSignature(typeValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAuthorNullParameterGivenShouldThrowException() {
        // Act
        this.detector.findAuthor(null);
    }

    @Test
    public void findAuthorShouldWorkCorrectly() throws FileNotFoundException {
        // Arrange
        final String expected = "Agatha Christie";

        // Act
        String actual = this.detector.findAuthor(
                new FileInputStream(SAMPLE_TEXT_PATH));

        // Assert
        assertEquals(expected, actual);
    }
}
