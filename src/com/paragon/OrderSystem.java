package com.paragon;

import com.paragon.orders.Order;
import com.paragon.orders.OrderLedger;
import com.paragon.processing.DealProcessingChargeCalc;
import com.paragon.processing.ProcessingChargesCalc;
import com.paragon.processing.StdProcessingChargesCalc;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.stock.Warehouse;

import java.math.BigDecimal;
import java.util.*;

public class OrderSystem implements OrderService {
    private final ProcessingChargesCalc processingChargesCalc = new DealProcessingChargeCalc();



    public static final BigDecimal CASE_SIZE = new BigDecimal(12);


    public Map<UUID, Quote> getQuotes() {
        return quotes;
    }

    private Map<UUID, Quote> quotes = new HashMap<UUID, Quote>();

    @Override
    public List<Offer> searchForProduct(String query) {

        List<Offer> searchResults = Warehouse.getInstance().searchFor(query);
        for (Offer offer : searchResults) {
            quotes.put(offer.id, new Quote(offer, System.currentTimeMillis()));
        }
        return searchResults;
    }

    @Override
    public void confirmOrder(UUID id, String userAuthToken) {
        this.confirmOrder(id, userAuthToken, 0);
    }

    public Order confirmOrder(UUID id, String userAuthToken,  long time) {
        if (!quotes.containsKey(id)) {
            throw new NoSuchElementException("Offer ID is invalid");
        }

        Quote quote = quotes.get(id);

        long timeNow = System.currentTimeMillis();
        if(time != 0) {
            timeNow = time;
        }

        BigDecimal processingCharges = processingChargesCalc.calculateProcessingCharges(quote, timeNow);
        Order completeOrder = new Order(quote.offer.price.multiply(CASE_SIZE).add(processingCharges), quote, timeNow, userAuthToken);

        OrderLedger.getInstance().placeOrder(completeOrder);
        return completeOrder;
    }



}