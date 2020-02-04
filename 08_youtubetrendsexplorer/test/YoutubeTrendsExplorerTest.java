import static org.junit.Assert.assertEquals;


import bg.sofia.uni.fmi.mjt.youtube.YoutubeTrendsExplorer;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class YoutubeTrendsExplorerTest {
    private static YoutubeTrendsExplorer explorer;

    @BeforeClass
    public static void init() throws FileNotFoundException {
        Path file = Path.of("./test/testUSvideos.txt");
        explorer = new YoutubeTrendsExplorer(
                new FileInputStream(file.toFile()));
    }

    @Test
    public void findIdOfLeastLikedVideoShouldWorkCorrectly() {
        // Arrange
        String expected = "_KEN-cxmGw8";

        // Act
        String actual = explorer.findIdOfLeastLikedVideo();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void findIdOfMostLikedLeastDislikedVideoShouldWorkCorrectly() {
        // Arrange
        String expected = "yHHnywiSwIE";

        // Act
        String actual = explorer.findIdOfMostLikedLeastDislikedVideo();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void findDistinctTitlesOfTop3VideosByViewsShouldWorkCorrectly() {
        // Arrange
        List<String> expected = List.of(
                "Casually Explained: Men's Fashion",
                "The Top Dan Memes of 2017",
                "What Jeremy Clarkson thinks about Tesla");

        // Act
        List<String> actual = explorer.findDistinctTitlesOfTop3VideosByViews();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void findIdOfMostTaggedVideoShouldWorkCorrectly() {
        // Arrange
        String expected = "yHHnywiSwIE";

        // Act
        String actual = explorer.findIdOfMostTaggedVideo();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void findTitleOfFirstVideoTrendingBefore100KViewsShouldWorkCorrectly() {
        // Arrange
        String expected = "I Became My Sim For A Day *CHALLENGE*";

        // Act
        String actual = explorer.findTitleOfFirstVideoTrendingBefore100KViews();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void findIdOfMostTrendingVideoShouldWorkCorrectly() {
        // Arrange
        String expected = "hDYPVhwULCc";

        // Act
        String actual = explorer.findIdOfMostTrendingVideo();

        // Assert
        assertEquals(expected, actual);
    }
}
