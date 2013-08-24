package com.paragon.processing;

import com.paragon.OrderSystem;
import com.paragon.stock.Quote;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Navneet
 * Date: 23/08/13
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public class DealProcessingChargeCalc implements ProcessingChargesCalc{
    public static final BigDecimal TIER1_PROCESSING_CHARGE = new BigDecimal(0);
    public static final BigDecimal TIER2_PROCESSING_CHARGE = new BigDecimal(10);
    public static final BigDecimal TIER2_PROCESSING_CHARGE_PERC = new BigDecimal(0.05);
    public static final BigDecimal TIER3_PROCESSING_CHARGE = new BigDecimal(20);

    private static final long TIER1_QUOTE_AGE_MILLIS = 2 * 60 * 1000;
    private static final long TIER2_QUOTE_AGE_MILLIS = 10 * 60 * 1000;
    private static final long TIER3_QUOTE_AGE_MILLIS = 20 * 60 * 1000;
    @Override
    public BigDecimal calculateProcessingCharges(Quote quote, long timeNow) {
        long timeLag = timeNow - quote.timestamp;
        if (timeLag > MAX_QUOTE_AGE_MILLIS) {
            throw new IllegalStateException("Quote expired, please get a new price");
        }
        // processing for variable processing charges
        BigDecimal processingCharges = TIER1_PROCESSING_CHARGE;
        if(timeLag > TIER1_QUOTE_AGE_MILLIS && timeLag <= TIER2_QUOTE_AGE_MILLIS ){
            processingCharges  = TIER2_PROCESSING_CHARGE;
            BigDecimal varProcessingCharges = quote.offer.price.multiply(OrderSystem.CASE_SIZE).multiply(TIER2_PROCESSING_CHARGE_PERC) ;
            if (varProcessingCharges.compareTo(processingCharges)==-1) {
                processingCharges = varProcessingCharges;
            }
        }     else if (timeLag > TIER2_QUOTE_AGE_MILLIS && timeLag <=TIER3_QUOTE_AGE_MILLIS)    {
            processingCharges  = TIER3_PROCESSING_CHARGE;
        }
        return processingCharges;
    }
}
