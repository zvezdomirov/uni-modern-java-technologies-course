package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class GoldenCard extends Card {

    public GoldenCard(String name) {
        super(name);
    }

    @Override
    public boolean executePayment(double cost) {
        final double DISCOUNT_FACTOR = 0.85;
        double cardAmount = super.getAmount();
        if (cardAmount < 0 ||
                cost < 0 ||
                cardAmount < cost) {
            return false;
        }
        super.setAmount(cardAmount - cost * DISCOUNT_FACTOR);
        return true;
    }
}
