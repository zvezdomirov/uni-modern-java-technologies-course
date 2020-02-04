package bg.sofia.uni.fmi.mjt.shopping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class ListShoppingCartTest {
    private static final Apple DEFAULT_APPLE =
            new Apple("Apple", "", 2.50);
    private static final Chocolate DEFAULT_CHOCOLATE =
            new Chocolate("Chocolate", "", 2.50);
    private ShoppingCart cart;

    @Before
    public void initList() {
        this.cart = new ListShoppingCart();
    }

    @Test
    public void getUniqueItemsListWithDuplicatesGivenShouldReturnCollectionWithLesserSize() {
        // Arrange
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_APPLE);
        int sizeWithDuplicates = this.cart.getCartSize();

        // Act
        Collection<Item> uniqueItems = this.cart.getUniqueItems();

        // Assert
        assertEquals(sizeWithDuplicates - 1, uniqueItems.size());
    }

    @Test
    public void addItemNonNullItemGivenShouldIncreaseCartSize() {
        // Arrange
        int sizeBeforeAdd = this.cart.getCartSize();

        // Act
        this.cart.addItem(DEFAULT_APPLE);
        int sizeAfterAdd = this.cart.getCartSize();

        // Assert
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Test
    public void addItemNullItemGivenShouldNotIncreaseCartSize() {
        // Arrange
        int sizeBeforeAdd = this.cart.getCartSize();

        // Act
        this.cart.addItem(null);
        int sizeAfterAdd = this.cart.getCartSize();

        // Assert
        assertEquals(sizeBeforeAdd, sizeAfterAdd);
    }

    @Test
    public void removeItemExistentItemGivenShouldWorkCorrectly()
            throws ItemNotFoundException {
        // Arrange
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_CHOCOLATE);

        // Act
        this.cart.removeItem(DEFAULT_APPLE);

        // Assert
        assertFalse(this.cart.containsItem(DEFAULT_APPLE));
    }

    @Test(expected = ItemNotFoundException.class)
    public void removeItemNonExistentItemGivenShouldThrowException()
            throws ItemNotFoundException {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);

        // Act
        this.cart.removeItem(DEFAULT_APPLE);
    }

    @Test
    public void containsItemContainedItemGivenShouldReturnTrue() {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_APPLE);

        // Act
        boolean actual = this.cart.containsItem(DEFAULT_APPLE);

        // Assert
        assertTrue(actual);
    }

    @Test
    public void containsItemNotContainedItemGivenShouldReturnTrue() {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);

        // Act
        boolean actual = this.cart.containsItem(DEFAULT_APPLE);

        // Assert
        assertFalse(actual);
    }

    @Test
    public void getTotalItemsInCartGivenShouldWorkCorrectly()
            throws ItemNotFoundException {
        // Arrange
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_CHOCOLATE);

        // Act
        double expectedTotal =
                DEFAULT_APPLE.getPrice() + DEFAULT_CHOCOLATE.getPrice() * 2;
        double actualTotal = this.cart.getTotal();

        // Assert
        assertEquals(expectedTotal, actualTotal, 0);
    }

    @Test
    public void getCartSizeCartWithItemsGivenShouldReturnCorrectValue() {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_CHOCOLATE);
        final int expectedSize = 3;

        // Act
        int actualSize = this.cart.getCartSize();

        // Assert
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void getSortedItemsUnorderedItemsGivenShouldReturnUniqueItems() {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_APPLE);

        // Act
        Collection<Item> sorted = this.cart.getSortedItems();

        // Assert
        assertEquals(2, sorted.size());
    }

    @Test
    public void getSortedItemsUnorderedItemsGivenShouldSortCorrectly() {
        // Arrange
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_CHOCOLATE);
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_APPLE);
        this.cart.addItem(DEFAULT_APPLE);

        // Act
        Item[] sorted = this.cart.getSortedItems()
                .toArray(Item[]::new);

        // Assert
        assertEquals(DEFAULT_APPLE, sorted[0]);
        assertEquals(DEFAULT_CHOCOLATE, sorted[1]);
    }
}
