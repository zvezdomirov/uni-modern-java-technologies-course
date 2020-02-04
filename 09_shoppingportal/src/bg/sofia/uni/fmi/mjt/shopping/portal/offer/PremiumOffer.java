package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PremiumOffer extends BaseOffer {
    private double discount;

    public PremiumOffer(String productName,
                        LocalDate date,
                        String description,
                        double price,
                        double shippingPrice,
                        double discount) {
        super(productName, date, description, price, shippingPrice);
        this.setDiscount(discount);
        this.addTotalPriceDiscount();
    }

    private double roundDiscount(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void addTotalPriceDiscount() {
        final double percentageMaxValue = 100.00;
        this.totalPrice -= this.totalPrice * this.discount / percentageMaxValue;
    }

    public double getDiscount() {
        return discount;
    }

    private void setDiscount(double discount) {
        final int discountUpperBound = 100;
        final int discountLowerBound = 0;
        if (discount < discountLowerBound || discount > discountUpperBound) {
            throw new IllegalArgumentException(
                    "\"discount\" should be in range [0, 100]");
        }
        this.discount = this.roundDiscount(discount, 2);
    }
}
