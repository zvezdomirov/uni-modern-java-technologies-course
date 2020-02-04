package bg.sofia.uni.fmi.mjt.shopping;

import static org.junit.Assert.assertEquals;


import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import org.junit.Before;
import org.junit.Test;

/*This test class is mainly for more code coverage*/
public class ItemTest {
    private static final String APPLE_DESCRIPTION = "Golden-delicious";
    private static final double APPLE_PRICE = 1.9;
    private static final String CHOCOLATE_DESCRIPTION = "Milka";
    private static final double CHOCOLATE_PRICE = 2.2;
    private static final String CHOCOLATE_NAME = "Chocolate";
    private static final String APPLE_NAME = "Apple";
    private Apple apple;
    private Chocolate chocolate;

    @Before
    public void initItems() {
        this.apple = new Apple(
                APPLE_NAME, APPLE_DESCRIPTION, APPLE_PRICE);
        this.chocolate = new Chocolate(
                CHOCOLATE_NAME, CHOCOLATE_DESCRIPTION, CHOCOLATE_PRICE);
    }

    @Test
    public void getDescriptionShouldReturnTheInitializedDescription() {
        assertEquals(APPLE_DESCRIPTION, this.apple.getDescription());
        assertEquals(CHOCOLATE_DESCRIPTION, this.chocolate.getDescription());
    }

    @Test
    public void getPriceShouldReturnTheInitializedPrice() {
        assertEquals(APPLE_PRICE, this.apple.getPrice(), 0);
        assertEquals(CHOCOLATE_PRICE, this.chocolate.getPrice(), 0);
    }
}
