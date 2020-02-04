package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;
import java.util.Objects;

public abstract class BaseOffer implements Offer {
    protected double totalPrice;
    private String productName;
    private LocalDate date;
    private String description;
    private double price;
    private double shippingPrice;

    public BaseOffer(String productName,
                     LocalDate date,
                     String description,
                     double price,
                     double shippingPrice) {
        this.productName = productName;
        this.date = date;
        this.description = description;
        this.price = price;
        this.shippingPrice = shippingPrice;
        this.totalPrice = this.price + this.shippingPrice;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getShippingPrice() {
        return shippingPrice;
    }

    @Override
    public double getTotalPrice() {
        return this.totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseOffer baseOffer = (BaseOffer) o;
        return Double.compare(baseOffer.getTotalPrice(), this.getTotalPrice()) == 0 &&
                this.getProductName().equalsIgnoreCase(baseOffer.getProductName()) &&
                Objects.equals(this.getDate(), baseOffer.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName.toLowerCase(), date, totalPrice);
    }

    @Override
    public int compareTo(Offer offer) {
        int comparisonResult = offer.getDate()
                .compareTo(this.getDate());
        if (comparisonResult == 0) {
            comparisonResult = Double
                    .compare(this.getTotalPrice(), offer.getTotalPrice());
        }
        if (comparisonResult == 0) {
            comparisonResult = this.getProductName()
                    .compareToIgnoreCase(offer.getProductName());
        }
        return comparisonResult;
    }
}
