import bg.sofia.uni.fmi.mjt.shopping.portal.PriceStatistic;
import bg.sofia.uni.fmi.mjt.shopping.portal.ShoppingDirectory;
import bg.sofia.uni.fmi.mjt.shopping.portal.ShoppingDirectoryImpl;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.PremiumOffer;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.RegularOffer;
import java.time.LocalDate;
import java.util.Collection;

public class Main {
    public static void main(String[] args) throws OfferAlreadySubmittedException, ProductNotFoundException, NoOfferFoundException {
        ShoppingDirectory shoppingDirectory = new ShoppingDirectoryImpl();
        for (int i = 0; i < 5; i++) {
            Offer pOffer = new PremiumOffer(
                    "Apple",
                    LocalDate.of(2019, 11, 25),
                    "",
                    3.5 + i,
                    2.5 + i,
                    15 + i);
            shoppingDirectory.submitOffer(pOffer);
        }
        for (int i = 0; i < 5; i++) {
            Offer pOffer = new PremiumOffer(
                    "Apple",
                    LocalDate.of(2020, 12, 15),
                    "",
                    3.5 + i,
                    2.5 + i,
                    15 + i);
            shoppingDirectory.submitOffer(pOffer);
        }
        for (int i = 0; i < 5; i++) {
            Offer pOffer = new PremiumOffer(
                    "Apple",
                    LocalDate.of(2016, 6, 10),
                    "",
                    3.5 + i,
                    2.5 + i,
                    15 + i);
            shoppingDirectory.submitOffer(pOffer);
        }
        for (int i = 0; i < 5; i++) {
            Offer rOffer = new RegularOffer(
                    "Banana",
                    LocalDate.of(2019, 11, 25),
                    "",
                    4.5 + i,
                    3.5 + i);
            shoppingDirectory.submitOffer(rOffer);
        }
        for (int i = 0; i < 5; i++) {
            Offer rOffer = new RegularOffer(
                    "Banana",
                    LocalDate.of(2019, 10, 26),
                    "",
                    4.5 + i * 2,
                    3.5 + i);
            shoppingDirectory.submitOffer(rOffer);
        }
        Offer rOffer = new RegularOffer(
                "Banana",
                LocalDate.of(2019, 10, 28),
                "",
                5,
                5);
        shoppingDirectory.submitOffer(rOffer);
        Collection<PriceStatistic> bananaStats = shoppingDirectory.collectProductStatistics("Banana");
        Collection<PriceStatistic> appleStats = shoppingDirectory.collectProductStatistics("Apple");
        Offer bestBananaOffer = shoppingDirectory.findBestOffer("Banana");
        Offer bestAppleOffer = shoppingDirectory.findBestOffer("Apple");


//        Offer pOffer = new PremiumOffer(
//                "Banana",
//                LocalDate.of(2019, 10, 27),
//                "",
//                5,
//                5,
//                20);
//        shoppingDirectory.submitOffer(pOffer);
//        System.out.println(pOffer.getTotalPrice());
//        Collection<Offer> offers = shoppingDirectory.findAllOffers("Banana");
    }
}
