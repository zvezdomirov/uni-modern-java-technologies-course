package bg.sofia.uni.fmi.mjt.youtube;

import bg.sofia.uni.fmi.mjt.youtube.model.TrendingVideo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class YoutubeTrendsExplorer {
    private List<TrendingVideo> trendingVideos;

    /**
     * Loads the dataset from the given {@code dataInput} stream.
     */
    public YoutubeTrendsExplorer(InputStream dataInput) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(dataInput))) {
            // Skip the line with metadata
            br.readLine();
            this.trendingVideos = br.lines()
                    .map(TrendingVideo::createTrendingVideo)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Something went wrong trying to read from file");
            e.printStackTrace();
        }
    }

    /**
     * Returns all videos loaded from the dataset.
     **/
    public Collection<TrendingVideo> getTrendingVideos() {
        return this.trendingVideos;
    }

    public String findIdOfLeastLikedVideo() {
        return this.trendingVideos.stream()
                .min(Comparator.comparingLong(TrendingVideo::getLikes))
                .get()
                .getId();

    }

    public String findIdOfMostLikedLeastDislikedVideo() {
        return this.trendingVideos.stream()
                .min((v1, v2) -> Math.toIntExact(
                        ((v1.getDislikes() - v1.getLikes()) - (v2.getDislikes() - v2.getLikes()))))
                .get()
                .getId();
    }

    public List<String> findDistinctTitlesOfTop3VideosByViews() {
        final int numOfVideoTitles = 3;
        return this.trendingVideos.stream()
                .sorted(Comparator.comparingLong(TrendingVideo::getViews).reversed())
                .distinct()
                .limit(numOfVideoTitles)
                .map(TrendingVideo::getTitle)
                .collect(Collectors.toList());
    }

    public String findIdOfMostTaggedVideo() {
        return this.trendingVideos.stream()
                .max(Comparator.comparingInt(v -> v.getTags().size()))
                .get()
                .getId();
    }

    public String findTitleOfFirstVideoTrendingBefore100KViews() {
        final int maxNumberOfViews = 100_000;
        return this.trendingVideos.stream()
                .filter(v -> v.getViews() < maxNumberOfViews)
                .min(Comparator.comparing(TrendingVideo::getPublishDate))
                .get()
                .getTitle();
    }

    public String findIdOfMostTrendingVideo() {
        Map<String, List<TrendingVideo>> groupedById = this.trendingVideos.stream()
                .collect(Collectors.groupingBy(TrendingVideo::getId));
        return groupedById.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .get()
                .getKey();

    }
}
