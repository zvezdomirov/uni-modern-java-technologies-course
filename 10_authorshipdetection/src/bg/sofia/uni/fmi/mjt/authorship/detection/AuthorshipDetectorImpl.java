package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorshipDetectorImpl implements AuthorshipDetector {
    private static final String NULL_ARGUMENT_MESSAGE =
            "Null argument is not acceptable for this method";
    private static final String IOE_MESSAGE =
            "Something went wrong while reading from InputStream";
    private Map<FeatureType, Double> featureWeights;

    private Map<String, LinguisticSignature> authorSignatures;

    public AuthorshipDetectorImpl(InputStream signaturesDataset,
                                  double[] weights) {
        this.setAuthorSignatures(signaturesDataset);
        this.setFeatureWeights(weights);
    }

    private void setAuthorSignatures(InputStream signaturesDataset) {
        this.authorSignatures = new HashMap<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(signaturesDataset))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(", ");
                final String authorName = tokens[0];
                final Map<FeatureType, Double> featureValues =
                        this.extractFeatureValues(tokens);
                LinguisticSignature authorSignature =
                        new LinguisticSignature(featureValues);
                this.authorSignatures.put(authorName, authorSignature);
            }
        } catch (IOException e) {
            System.err.println(IOE_MESSAGE);
        }
    }

    private void setFeatureWeights(double[] weights) {
        this.featureWeights = new HashMap<>();
        final FeatureType[] featureTypes = FeatureType.values();
        for (int i = 0; i < featureTypes.length; i++) {
            this.featureWeights.put(featureTypes[i], weights[i]);
        }
    }

    private Map<FeatureType, Double> extractFeatureValues(String[] tokens) {
        final FeatureType[] featureTypes = FeatureType.values();
        final Map<FeatureType, Double> signatureValues = new HashMap<>();
        // Link every feature type with its corresponding value.
        for (int i = 0; i < featureTypes.length; i++) {
            // Values in tokens start from 1 (0th token is author's name).
            signatureValues.put(featureTypes[i],
                    Double.parseDouble(tokens[i + 1]));
        }
        return signatureValues;
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        this.ensureNotNull(mysteryText);
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(mysteryText))) {
            List<String> lines = br.lines()
                    .collect(Collectors.toList());
            return this.calculateSignature(lines);
        } catch (IOException e) {
            System.err.println(IOE_MESSAGE);
        }
        return null;
    }

    private void ensureNotNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException(
                    NULL_ARGUMENT_MESSAGE);
        }
    }

    private LinguisticSignature calculateSignature(List<String> lines) {
        Map<String, Integer> words = this.extractWords(lines);
        List<String> sentences = this.extractSentences(lines);
        List<String> phrases = this.extractPhrases(sentences);
        double avgWordLength = this.calculateAvgWordLength(words);
        double typeTokenRatio = this.calculateTypeTokenRatio(words);
        double hapaxLegomenaRatio = this.calculateHapaxLegomenaRatio(words);
        double avgSentenceLength = this.calculateAvgSentenceLength(words, sentences);
        double avgSentenceComplexity = this.calculateAvgSentenceComplexity(phrases, sentences);
        Map<FeatureType, Double> featureValues = this.createFeatureValuesMap(
                avgWordLength, typeTokenRatio, hapaxLegomenaRatio,
                avgSentenceLength, avgSentenceComplexity);
        return new LinguisticSignature(featureValues);
    }

    private Map<String, Integer> extractWords(List<String> lines) {
        Map<String, Integer> words = new HashMap<>();
        lines.stream()
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .map(this::cleanUp)
                .filter(word -> !word.isEmpty())
                .forEach(word -> words.merge(word, 1, Integer::sum));
        return words;
    }

    private List<String> extractSentences(List<String> lines) {
        List<String> sentences = new ArrayList<>();
        StringBuilder currentSentence = new StringBuilder();
        for (String line : lines) {
            for (char ch : line.toCharArray()) {
                if (ch == '.' || ch == '!' || ch == '?') {
                    String stripped = currentSentence
                            .toString().strip();
                    if (!stripped.isEmpty()) {
                        sentences.add(stripped);
                        currentSentence.setLength(0);
                    }
                } else {
                    currentSentence.append(ch);
                }
            }
            currentSentence.append(System.lineSeparator());
        }
        // Add the last sentence (until EOF)
        currentSentence.deleteCharAt(currentSentence.length() - 1);
        if (currentSentence.length() != 0) {
            sentences.add(currentSentence
                    .toString().strip());
        }
        return sentences;
    }

    private List<String> extractPhrases(List<String> sentences) {
        return sentences.stream()
                .map(sentence -> sentence.split("[,:;]+"))
                .filter(tokens -> tokens.length > 0)
                .flatMap(Arrays::stream)
                .map(String::strip)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private double calculateAvgWordLength(Map<String, Integer> words) {
        long allWordsCount = getAllWordsCount(words);
        long allCharactersCount = words.entrySet().stream()
                .mapToLong(w -> w.getKey().length() * w.getValue())
                .sum();
        return ((double) allCharactersCount / allWordsCount);
    }

    private double calculateTypeTokenRatio(Map<String, Integer> words) {
        long allWordsCount = getAllWordsCount(words);
        return ((double) words.keySet().size() / allWordsCount);
    }

    private double calculateHapaxLegomenaRatio(Map<String, Integer> words) {
        long allWordsCount = getAllWordsCount(words);
        long uniqueWordsCount = words
                .values().stream()
                .filter(count -> count == 1)
                .count();
        return ((double) uniqueWordsCount / allWordsCount);
    }

    private double calculateAvgSentenceLength(Map<String, Integer> words,
                                              List<String> sentences) {
        long allWordsCount = getAllWordsCount(words);
        return ((double) allWordsCount / sentences.size());
    }

    private double calculateAvgSentenceComplexity(List<String> phrases,
                                                  List<String> sentences) {
        return ((double) phrases.size() / sentences.size());
    }

    private Map<FeatureType, Double> createFeatureValuesMap(
            double avgWordLength, double typeTokenRatio, double hapaxLegomenaRatio,
            double avgSentenceLength, double avgSentenceComplexity) {
        return new HashMap<>() {
            {
                put(FeatureType.AVERAGE_WORD_LENGTH, avgWordLength);
                put(FeatureType.TYPE_TOKEN_RATIO, typeTokenRatio);
                put(FeatureType.HAPAX_LEGOMENA_RATIO, hapaxLegomenaRatio);
                put(FeatureType.AVERAGE_SENTENCE_LENGTH, avgSentenceLength);
                put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, avgSentenceComplexity);
            }
        };
    }

    private long getAllWordsCount(Map<String, Integer> words) {
        return words
                .values().stream()
                .mapToLong(Long::valueOf)
                .sum();
    }

    private String cleanUp(String word) {
        return word.toLowerCase()
                .replaceAll("[!\"',;:.\\-?)(\\[\\]<>*#\\n\\t\\r]+$", "");
    }

    @Override
    public double calculateSimilarity(LinguisticSignature firstSignature,
                                      LinguisticSignature secondSignature) {
        this.ensureNotNull(firstSignature);
        this.ensureNotNull(secondSignature);
        Map<FeatureType, Double> firstFeatures = firstSignature.getFeatures();
        Map<FeatureType, Double> secondFeatures = secondSignature.getFeatures();
        double similarity = 0;
        for (FeatureType featureType : FeatureType.values()) {
            similarity +=
                    Math.abs(firstFeatures.get(featureType) - secondFeatures.get(featureType))
                            * this.featureWeights.get(featureType);
        }
        return similarity;
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        this.ensureNotNull(mysteryText);
        LinguisticSignature textSignature = this.calculateSignature(mysteryText);
        return this.findAuthorWithMostSimilarSignature(textSignature);
    }

    private String findAuthorWithMostSimilarSignature(LinguisticSignature textSignature) {
        String mostSimilarAuthor = "";
        double currentSimilarity;
        double bestSimilarity = Double.MAX_VALUE;
        for (var signature : this.authorSignatures.entrySet()) {
            currentSimilarity = this.calculateSimilarity(
                    signature.getValue(),
                    textSignature);
            if (Double.compare(currentSimilarity, bestSimilarity) < 0) {
                bestSimilarity = currentSimilarity;
                mostSimilarAuthor = signature.getKey();
            }
        }
        return mostSimilarAuthor;
    }

    public Map<String, LinguisticSignature> getAuthorSignatures() {
        return authorSignatures;
    }
}
