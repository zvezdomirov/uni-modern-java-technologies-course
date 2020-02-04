package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListShoppingCart implements ShoppingCart {

    private List<Item> items;

    public ListShoppingCart() {
        this.items = new ArrayList<>();
    }

    @Override
    public Collection<Item> getUniqueItems() {
        return new HashSet<>(items);
    }

    @Override
    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    @Override
    public void removeItem(Item item) throws ItemNotFoundException {
        if (!items.contains(item)) {
            throw new ItemNotFoundException();
        }
        items.remove(item);
    }

    @Override
    public boolean containsItem(Item item) {
        return this.items.contains(item);
    }

    @Override
    public double getTotal() {
        return this.items.stream()
                .mapToDouble(Item::getPrice)
                .sum();
    }

    @Override
    public int getCartSize() {
        return this.items.size();
    }

    @Override
    public List<Item> getSortedItems() {
        return this.toMap().entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<Item, Integer> toMap() {
        Map<Item, Integer> itemsWithQuantity = new HashMap<>();
        for (Item item : this.items) {
            itemsWithQuantity.merge(item, 1, Integer::sum);
        }
        return itemsWithQuantity;
    }
}

