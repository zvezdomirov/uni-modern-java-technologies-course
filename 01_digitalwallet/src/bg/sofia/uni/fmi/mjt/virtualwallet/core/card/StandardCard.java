package bg.sofia.uni.fmi.mjt.virtualwallet.core.card;

public class StandardCard extends Card {

    public StandardCard(String name) {
        super(name);
    }

    @Override
    public boolean executePayment(double cost) {
        double cardAmount = super.getAmount();
        if (cardAmount < 0 ||
                cost < 0 ||
                cardAmount < cost) {
            return false;
        }
        super.setAmount(cardAmount - cost);
        return true;
    }
}
