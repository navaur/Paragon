package com.paragon.processing;

import com.paragon.OrderSystem;
import com.paragon.stock.Quote;

import java.math.BigDecimal;

public class StdProcessingChargesCalc implements ProcessingChargesCalc {
    public static final BigDecimal STANDARD_PROCESSING_CHARGE = new BigDecimal(5);

    @Override
    public BigDecimal calculateProcessingCharges(Quote quote, long timeNow) {
        long timeLag = timeNow - quote.timestamp;
        if (timeLag > MAX_QUOTE_AGE_MILLIS) {
            throw new IllegalStateException("Quote expired, please get a new price");
        }
        return STANDARD_PROCESSING_CHARGE;
    }
}