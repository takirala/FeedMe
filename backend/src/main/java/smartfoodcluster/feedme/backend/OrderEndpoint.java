package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import smartfoodcluster.feedme.entities.Order;

import static smartfoodcluster.feedme.util.OfyService.ofy;

/**
 * Created by Srinivas on 4/17/2016.
 */
public class OrderEndpoint {

    public OrderEndpoint() {
    }

    /**
     * This inserts a new <code>Order</code> object.
     *
     * @param order The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "registerOrder")
    public Order registerOrder(Order order) throws ConflictException {

        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(order).now();
        return order;
    }

    @ApiMethod(name = "listOrders")
    public CollectionResponse<Order> listOrders(@Nullable @Named("cursor") String cursorString,
                                                @Nullable @Named("count") Integer count) {

        Query<Order> query = ofy().load().type(Order.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Order> records = new ArrayList<Order>();
        QueryResultIterator<Order> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }

        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<Order>builder().setItems(records).setNextPageToken(cursorString).build();
    }
}
