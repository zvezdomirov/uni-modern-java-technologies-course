package bg.sofia.uni.fmi.mjt.christmas;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class KidTest {
    private Workshop workshop;
    private Kid kid;

    @Before
    public void init() {
        this.workshop = new Workshop();
        this.kid = new Kid(this.workshop);
    }

    @Test
    public void startingThreadShouldPostWishInWorkshop() {
        // Arrange
        int wishesBefore = this.workshop.getQueryingWishesCount();

        // Act
        this.kid.start();
        try {
            this.kid.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int wishesAfter = this.workshop.getQueryingWishesCount();

        // Assert
        Assert.assertEquals(wishesBefore + 1, wishesAfter);
    }
}