package bg.sofia.uni.fmi.mjt.christmas;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElfTest {
    private static final int ELF_ID = 0;
    private Elf elf;
    private Workshop workshop;

    @Before
    public void init() {
        this.workshop = new Workshop();
        this.elf = new Elf(ELF_ID, this.workshop);
    }

    @Test
    public void craftGiftShouldRemoveFromWorkshopWishes() {
        // Arrange
        Gift gift = Gift.getGift();
        this.workshop.postWish(gift);
        int wishesBefore = this.workshop.getQueryingWishesCount();

        // Act
        this.elf.craftGift();
        int wishesAfter = this.workshop.getQueryingWishesCount();

        // Assert
        Assert.assertEquals(wishesBefore - 1, wishesAfter);
    }

    @Test
    public void craftGiftShouldIncreaseTotalGiftsCrafted() {
        // Arrange
        Gift gift = Gift.getGift();
        this.workshop.postWish(gift);
        int craftedBefore = this.elf.getTotalGiftsCrafted();

        // Act
        this.elf.craftGift();
        int craftedAfter = this.elf.getTotalGiftsCrafted();

        // Assert
        Assert.assertEquals(craftedBefore + 1, craftedAfter);
    }

    @Test
    public void startingThreadShouldCraftAllGiftsFromWorkshopWishList() {
        // Arrange
        final int addedWishes = 10;
        for (int i = 0; i < addedWishes; i++) {
            this.workshop.postWish(Gift.getGift());
        }
        int sizeBefore = this.workshop.getQueryingWishesCount();

        // Act
        this.elf.start();
        try {
            this.elf.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int sizeAfter = this.workshop.getQueryingWishesCount();

        // Assert
        Assert.assertEquals(addedWishes, sizeBefore);
        Assert.assertEquals(sizeBefore - addedWishes, sizeAfter);
    }
}
