package bg.sofia.uni.fmi.mjt.shopping.portal;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ShoppingDirectoryImpl implements ShoppingDirectory {
    private static final String NO_OFFERS_FOUND_MESSAGE_TEMPLATE =
            "There are no available offers for product {0}";
    private Map<String, SortedSet<Offer>> productOffers;

    public ShoppingDirectoryImpl() {
        this.productOffers = new HashMap<>();
    }

    @Override
    public Collection<Offer> findAllOffers(String productName)
            throws ProductNotFoundException {
        final int numOfDaysToShowOffersFor = 29;
        this.ensureParameterNotNull(productName);
        SortedSet<Offer> foundOffers = this.productOffers.get(productName);
        this.ensureOffersNotNull(productName, foundOffers);
        List<Offer> latestOffers = this.getOffersForLatestNDays(foundOffers, numOfDaysToShowOffersFor);
        latestOffers.sort(Comparator.comparingDouble(Offer::getTotalPrice));
        return latestOffers;
    }

    private void ensureParameterNotNull(Object parameter) {
        if (parameter == null) {
            throw new IllegalArgumentException(
                    "Parameter should not be null.");
        }
    }

    private void ensureOffersNotNull(String productName, SortedSet<Offer> foundOffers)
            throws ProductNotFoundException {
        if (foundOffers == null) {
            throw new ProductNotFoundException(
                    MessageFormat.format(NO_OFFERS_FOUND_MESSAGE_TEMPLATE,
                            productName));
        }
    }

    private List<Offer> getOffersForLatestNDays(SortedSet<Offer> foundOffers,
                                                int numOfDays) {
        List<Offer> offersToReturn = new ArrayList<>();
        LocalDate queryDate = LocalDate.now();
        for (Offer offer : foundOffers) {
            if (offer.getDate().isBefore(
                    ChronoLocalDate.from(queryDate.minusDays(numOfDays)))) {
                break;
            }
            offersToReturn.add(offer);
        }
        return offersToReturn;
    }

    @Override
    public Offer findBestOffer(String productName)
            throws ProductNotFoundException, NoOfferFoundException {
        Offer[] allProductOffers = this.findAllOffers(productName)
                .toArray(Offer[]::new);
        if (allProductOffers.length == 0) {
            throw new NoOfferFoundException(
                    MessageFormat.format(
                            NO_OFFERS_FOUND_MESSAGE_TEMPLATE,
                            productName));
        }
        return allProductOffers[0];
    }

    @Override
    public Collection<PriceStatistic> collectProductStatistics(String productName)
            throws ProductNotFoundException {
        this.ensureParameterNotNull(productName);
        Map<LocalDate, SortedSet<Offer>> offersByDate = this.groupOffersByDate(productName);
        return this.collectProductStatistics(offersByDate);
    }

    private Map<LocalDate, SortedSet<Offer>> groupOffersByDate(String productName)
            throws ProductNotFoundException {
        SortedSet<Offer> offersForProduct = this.productOffers.get(productName);
        this.ensureOffersNotNull(productName, offersForProduct);
        Map<LocalDate, SortedSet<Offer>> map = new LinkedHashMap<>();
        for (Offer offer : offersForProduct) {
            SortedSet<Offer> offers = new TreeSet<>();
            offers.add(offer);
            map.merge(offer.getDate(), offers, (oldSet, newSet) -> {
                oldSet.addAll(newSet);
                return oldSet;
            });
        }
        return map;
    }

    private Collection<PriceStatistic> collectProductStatistics(
            Map<LocalDate, SortedSet<Offer>> offersByDate) {
        List<PriceStatistic> everyDateStatistics = new ArrayList<>();
        for (var entry : offersByDate.entrySet()) {
            double currentDateTotalPrice = 0;
            double currentDateLowestPrice = Integer.MAX_VALUE;
            final SortedSet<Offer> offersForCurrentDate = entry.getValue();

            // Calculate the lowest and average price for every offer from the current date
            for (Offer offer : offersForCurrentDate) {
                final double currentOfferPrice = offer.getTotalPrice();
                if (currentOfferPrice < currentDateLowestPrice) {
                    currentDateLowestPrice = currentOfferPrice;
                }
                currentDateTotalPrice += currentOfferPrice;
            }
            final PriceStatistic currentDateStatistic =
                    new PriceStatistic(entry.getKey(),
                            currentDateLowestPrice,
                            currentDateTotalPrice / offersForCurrentDate.size());
            everyDateStatistics.add(currentDateStatistic);
        }
        return everyDateStatistics;
    }

    @Override
    public void submitOffer(Offer offer) throws OfferAlreadySubmittedException {
        this.ensureParameterNotNull(offer);
        String productName = offer.getProductName();
        SortedSet<Offer> offers = this.productOffers.get(productName);
        if (offers == null) {
            offers = new TreeSet<>();
            offers.add(offer);
            this.productOffers.put(productName, offers);
        } else if (offers.contains(offer)) {
            throw new OfferAlreadySubmittedException(
                    "You have already submitted the same offer for product: " +
                            productName);
        } else {
            offers.add(offer);
        }
    }
}
