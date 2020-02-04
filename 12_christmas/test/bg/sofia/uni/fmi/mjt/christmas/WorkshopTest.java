package bg.sofia.uni.fmi.mjt.christmas;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WorkshopTest {
    private Workshop workshop;

    @Before
    public void init() {
        this.workshop = new Workshop();
    }

    @Test
    public void instantiatingShouldInitElves() {
        // Assert
        for (Elf elf : this.workshop.getElves()) {
            Assert.assertNotNull(elf);
        }
    }

    @Test
    public void postWishShouldIncreaseQueueSize() {
        // Arrange
        Gift gift = Gift.getGift();
        int sizeBefore = this.workshop.getQueryingWishesCount();

        // Act
        this.workshop.postWish(gift);

        // Assert
        int sizeAfter = this.workshop.getQueryingWishesCount();
        Assert.assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    public void postWishShouldIncreaseTotalWishCount() {
        // Arrange
        Gift gift = Gift.getGift();
        int countBefore = this.workshop.getWishCount();

        // Act
        this.workshop.postWish(gift);

        // Assert
        int countAfter = this.workshop.getWishCount();
        Assert.assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void nextGiftShouldReturnNextQueueElement() {
        // Arrange
        Gift gift = Gift.getGift();
        this.workshop.postWish(gift);

        // Act
        Gift giftFromWishList = this.workshop.nextGift();

        // Assert
        Assert.assertEquals(gift, giftFromWishList);
    }
}
