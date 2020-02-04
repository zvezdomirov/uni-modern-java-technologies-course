package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;

public class PriceStatistic {
    private LocalDate date;
    private double lowestPrice;
    private double averagePrice;

    public PriceStatistic(LocalDate date,
                          double lowestPrice,
                          double averagePrice) {
        this.date = date;
        this.lowestPrice = lowestPrice;
        this.averagePrice = averagePrice;
    }

    /**
     * Returns the date for which the statistic is
     * collected.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Returns the lowest total price from the offers
     * for this product for the specific date.
     */
    public double getLowestPrice() {
        return this.lowestPrice;
    }

    /**
     * Return the average total price from the offers
     * for this product for the specific date.
     */
    public double getAveragePrice() {
        return this.averagePrice;
    }
}
