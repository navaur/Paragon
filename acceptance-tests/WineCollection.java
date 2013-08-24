import com.paragon.OrderSystem;
import fit.*;
import com.paragon.orders.Order;
import com.paragon.orders.OrderLedger;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.stock.Warehouse;
import java.util.*;
import java.util.List;

public class WineCollection {
    public static List<Offer> offersList;
    public static OrderSystem os = new OrderSystem();
    static  Map<UUID, Quote> quotes;
    static Quote selectedQuote ;

    static void select(Quote quote) {
        selectedQuote = quote;
    }

   static void search (String query) throws Exception {
       offersList=    os.searchForProduct(query)  ;
        quotes = os.getQuotes() ;
    }


}