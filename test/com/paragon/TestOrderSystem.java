package com.paragon;
import com.paragon.processing.DealProcessingChargeCalc;

import com.paragon.orders.Order;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(value = JUnit4.class)
public class TestOrderSystem {

    OrderSystem orderSystem = new OrderSystem();


    @Test
    public void searchForProductTest() {

        List<Offer> offers =
                orderSystem.searchForProduct("South Australia");
        String str;
        int size;
        str = offers.get(0).description;
        size =   offers.size();

        Assert.assertEquals(size, 1);

        offers = orderSystem.searchForProduct("Argentina");
        size =   offers.size();

        Assert.assertEquals(size, 8);

    }


    @Test
    public void confirmOrderWithinTwoMinutesTest() {

        List<Offer> offers =
                orderSystem.searchForProduct("South Australia");

        Map<UUID, Quote> quotes = orderSystem.getQuotes();

        UUID id = offers.get(0).id;
        String userAuthToken = "test";

        long time = quotes.get(id).timestamp + (2 * 60 * 1000);

        Order order = orderSystem.confirmOrder(id,userAuthToken,time);

        BigDecimal actual = order.totalPrice.subtract(offers.get(0).price.multiply(OrderSystem.CASE_SIZE));

        BigDecimal expected = new BigDecimal(0);
        Assert.assertTrue(expected.compareTo(actual) == 0);

    }

    @Test
    public void confirmOrderWithinTenMinutesPriceMoreTwoHundredTest() throws Exception {
        UUID selectedId = null;
        List<Offer> offers =
                orderSystem.searchForProduct("Argentina");
        Map<UUID, Quote> quotes = orderSystem.getQuotes();
        for (Offer offer :offers) {
            if (offer.price.multiply(OrderSystem.CASE_SIZE).doubleValue()> 200) {
                selectedId = offer.id;
                break;
            }
        }
        if (selectedId == null){
            throw  new Exception("No Suck Quote")      ;

        }

        String userAuthToken = "test";
        long time = quotes.get(selectedId).timestamp + (9 * 60 * 1000);

        Order order = orderSystem.confirmOrder(selectedId,userAuthToken,time);

        BigDecimal actual = order.totalPrice.subtract( quotes.get(selectedId).offer.price.multiply(OrderSystem.CASE_SIZE));

        BigDecimal expected = new BigDecimal(10);

        Assert.assertTrue(expected.compareTo(actual) == 0);
    }

    @Test
    public void confirmOrderWithinTenMinutesPriceLessTwoHundredTest() throws Exception {

        UUID selectedId = null;
        List<Offer> offers =
                orderSystem.searchForProduct("Argentina");
        Map<UUID, Quote> quotes = orderSystem.getQuotes();
        Offer selectedOffer = null;
        for (Offer offer :offers) {
            if (offer.price.multiply(OrderSystem.CASE_SIZE).doubleValue()< 200) {
                selectedId = offer.id;
                selectedOffer = offer;
                break;
            }
        }
        if (selectedId == null){
            throw  new Exception("No Suck Quote")      ;

        }

        String userAuthToken = "test";
        long time = quotes.get(selectedId).timestamp + (9 * 60 * 1000);

        Order order = orderSystem.confirmOrder(selectedId,userAuthToken,time);

        BigDecimal processingCharges = order.totalPrice.subtract( selectedOffer.price.multiply(OrderSystem.CASE_SIZE));

        BigDecimal actualPerc  =  processingCharges.divide(selectedOffer.price.multiply(OrderSystem.CASE_SIZE)).multiply(new BigDecimal(100));
        String actualPercStr =    actualPerc.intValue()+"%";

        Assert.assertEquals(actualPercStr, "5%");



    }

    @Test
    public void confirmOrderMoreThanTwentyMinutesThrowsIllegalStateExceptionTest() {

        List<Offer> offers =
                orderSystem.searchForProduct("South Australia");
        Exception exception = null;

        Map<UUID, Quote> quotes = orderSystem.getQuotes();

        UUID id = offers.get(0).id;
        String userAuthToken = "test";
        long time = quotes.get(id).timestamp + (30 * 60 * 1000);

        try{
        Order order = orderSystem.confirmOrder(id,userAuthToken,time);
        }
         catch (IllegalStateException ex){ exception = ex;}

        shouldValidateThrowsExceptionWithMessage(exception, "Quote expired, please get a new price");

    }

    private void shouldValidateThrowsExceptionWithMessage(final Exception e, final String message) {
        Assert.assertNotNull(e);
        Assert.assertTrue(e.getMessage().contains(message));
    }


}
