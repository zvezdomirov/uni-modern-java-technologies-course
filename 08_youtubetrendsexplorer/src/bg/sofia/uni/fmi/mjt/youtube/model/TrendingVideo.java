package bg.sofia.uni.fmi.mjt.youtube.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TrendingVideo {

    private String id;
    private String title;
    private LocalDate publishDate;
    private Set<String> tags;
    private long views;
    private long likes;
    private long dislikes;

    private TrendingVideo(String id, String title, LocalDate publishDate,
                          Set<String> tags, long views, long likes, long dislikes) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.tags = tags;
        this.views = views;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    /**
     * Returns a Movie instance, based on the given @{line} from the dataset.
     **/
    public static TrendingVideo createTrendingVideo(String line) {

        final int idInd = 0;
        final int titleInd = 2;
        final int publishDateInd = 3;
        final int tagsInd = 4;
        final int viewsInd = 5;
        final int likesInd = 6;
        final int dislikesInd = 7;

        String[] tokens = line.split("\t");

        String id = tokens[idInd];
        String title = tokens[titleInd];

        String parsedDate = tokens[publishDateInd]
                .substring(0, tokens[publishDateInd].indexOf('T')); // 2017-11-13T17:13:01.000Z
        LocalDate publishDate = LocalDate.parse(
                parsedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Set<String> tags = Stream.of(tokens[tagsInd].split("\\|"))
                .map(s -> s.replace("\"", ""))
                .collect(Collectors.toSet());

        long views = Long.parseLong(tokens[viewsInd]);
        long likes = Long.parseLong(tokens[likesInd]);
        long dislikes = Long.parseLong(tokens[dislikesInd]);

        return new TrendingVideo(id, title, publishDate, tags, views, likes, dislikes);

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public long getViews() {
        return views;
    }

    public long getLikes() {
        return likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrendingVideo)) {
            return false;
        }
        TrendingVideo trendingVideo = (TrendingVideo) o;
        return id.equals(trendingVideo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}