import com.paragon.OrderSystem;
import fit.*;
import com.paragon.orders.Order;
import com.paragon.orders.OrderLedger;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.stock.Warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class SearchWines extends Fixture {

    static int timeLag;
    public void search (String query) throws Exception {
        WineCollection.search(query);
    }
    public int totalWines(){
        return WineCollection.offersList.size();
   }


    public void time (int lag){
        this.timeLag= lag;
    }

    public void select (int i){
        Map.Entry<UUID, Quote> quoteEntry = (Map.Entry<UUID, Quote>)WineCollection.quotes.entrySet().toArray()[i-1]  ;

        WineCollection.select(quoteEntry.getValue()) ;

    }

    public void chooseTotalPriceHigher (double i)throws Exception{
        UUID selectedId = null;
        for (Offer offer : WineCollection.offersList) {
            if (offer.price.multiply(OrderSystem.CASE_SIZE).doubleValue() > i) {
                selectedId = offer.id;
                break;
            }
        }
        if (selectedId == null){
                throw  new Exception("No Suck Quote")      ;

        }
        Quote quote= WineCollection.quotes.get(selectedId);

        WineCollection.select(quote) ;

    }

    public void chooseTotalPriceLower (double i) throws Exception {
        UUID selectedId = null;
        for (Offer offer : WineCollection.offersList) {
            if (offer.price.multiply(OrderSystem.CASE_SIZE).doubleValue() < i) {
                selectedId = offer.id;
                break;
            }
        }
        if (selectedId == null){
            throw  new Exception("No Suck Quote");

        }
        Quote quote= WineCollection.quotes.get(selectedId);

        WineCollection.select(quote) ;


    }

    public double processing() {

    Order order =   WineCollection.os.confirmOrder(WineCollection.selectedQuote.offer.id, "",    WineCollection.selectedQuote.timestamp + timeLag*60*1000) ;
    return order.totalPrice.subtract(WineCollection.selectedQuote.offer.price.multiply(OrderSystem.CASE_SIZE)).doubleValue();
   }

    public String processingPercent() {

        Order order =   WineCollection.os.confirmOrder(WineCollection.selectedQuote.offer.id, "",    WineCollection.selectedQuote.timestamp + timeLag*60*1000) ;
        BigDecimal basePrice =       WineCollection.selectedQuote.offer.price.multiply(OrderSystem.CASE_SIZE);

        int processingPercent =      order.totalPrice.subtract(basePrice).divide(basePrice).multiply(new BigDecimal("100")).intValue();
        return processingPercent+"%" ;
    }

    public double  price() {
        return WineCollection.selectedQuote.offer.price.multiply(OrderSystem.CASE_SIZE).doubleValue();
    }

    public String  name()     {
        return WineCollection.selectedQuote.offer.description;
    }
}
