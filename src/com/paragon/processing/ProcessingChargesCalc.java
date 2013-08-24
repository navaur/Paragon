package com.paragon.processing;

import com.paragon.stock.Quote;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Navneet
 * Date: 23/08/13
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
    public interface ProcessingChargesCalc {
    public static final long MAX_QUOTE_AGE_MILLIS = 20 * 60 * 1000;
    BigDecimal calculateProcessingCharges(Quote quote, long timeNow);
}
