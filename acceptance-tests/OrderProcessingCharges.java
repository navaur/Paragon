/**
 * Created with IntelliJ IDEA.
 * User: Navneet
 * Date: 19/08/13
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */

import com.paragon.OrderSystem;
import fit.*;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.orders.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderProcessingCharges extends ColumnFixture  {
    public String quoteId;
    public String description;
    public long time;


    public float processing() {

        Map.Entry<UUID, Quote> quoteEntry = WineCollection.quotes.entrySet().iterator().next()  ;
        UUID id     =      quoteEntry.getKey();
        Quote quote =  quoteEntry.getValue() ;

        System.out.println ("Quote for id " +id + "is " +  quote) ;
        Order order =   WineCollection.os.confirmOrder(id, "auth",    quote.timestamp + time*60*1000) ;
        return order.totalPrice.floatValue()-quote.offer.price.floatValue()*OrderSystem.CASE_SIZE.floatValue();
    }



}
