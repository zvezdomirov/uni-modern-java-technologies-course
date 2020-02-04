package bg.sofia.uni.fmi.mjt.shopping.item;

import java.util.Objects;

public abstract class BaseItem implements Item {

    private String name;
    private String description;
    private double price;

    BaseItem(String name, String desc, double price) {
        this.name = name;
        this.description = desc;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseItem baseItem = (BaseItem) o;
        return Double.compare(baseItem.price, price) == 0 &&
                Objects.equals(name, baseItem.name) &&
                Objects.equals(description, baseItem.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price);
    }
}
