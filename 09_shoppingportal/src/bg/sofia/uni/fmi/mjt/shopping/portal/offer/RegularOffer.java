package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;

public class RegularOffer extends BaseOffer {

    public RegularOffer(String productName,
                        LocalDate date,
                        String description,
                        double price,
                        double shippingPrice) {
        super(productName, date, description, price, shippingPrice);
    }
}
