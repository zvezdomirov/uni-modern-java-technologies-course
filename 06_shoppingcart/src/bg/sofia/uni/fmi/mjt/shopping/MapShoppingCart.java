package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapShoppingCart implements ShoppingCart {

    private Map<Item, Integer> itemsWithQuantity = new HashMap<>();

    @Override
    public Collection<Item> getUniqueItems() {
        return itemsWithQuantity.keySet();
    }

    @Override
    public void addItem(Item item) {
        if (item != null) {
            this.itemsWithQuantity
                    .merge(item, 1, Integer::sum);
        }
    }

    @Override
    public void removeItem(Item item) throws ItemNotFoundException {
        if (!itemsWithQuantity.containsKey(item)) {
            throw new ItemNotFoundException();
        }
        int itemQuantity = itemsWithQuantity.get(item);
        if (itemQuantity == 1) {
            itemsWithQuantity.remove(item);
        } else {
            itemsWithQuantity.put(item, itemQuantity - 1);
        }
    }

    @Override
    public double getTotal() {
        return this.itemsWithQuantity.entrySet().stream()
                .mapToDouble(entry -> entry.getKey()
                        .getPrice() * entry.getValue())
                .sum();
    }

    @Override
    public int getCartSize() {
        return this.itemsWithQuantity.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public boolean containsItem(Item item) {
        return this.itemsWithQuantity.containsKey(item);
    }

    @Override
    public Collection<Item> getSortedItems() {
        return this.itemsWithQuantity.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
