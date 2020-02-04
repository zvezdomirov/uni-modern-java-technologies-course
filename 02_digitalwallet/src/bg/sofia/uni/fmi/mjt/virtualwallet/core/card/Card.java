package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

import java.util.Objects;

public abstract class Card {
    private String name;
    private double amount;


    public Card(String name) {
        this.name = name;
    }

    public abstract boolean executePayment(double cost);

    public String getName() {
        return this.name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return name.equals(card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
