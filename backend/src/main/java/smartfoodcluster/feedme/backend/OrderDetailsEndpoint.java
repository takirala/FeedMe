package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.ConflictException;

import smartfoodcluster.feedme.entities.OrderDetails;

import static smartfoodcluster.feedme.util.OfyService.ofy;

/**
 * Created by Srinivas on 4/17/2016.
 */
public class OrderDetailsEndpoint {

    public OrderDetailsEndpoint() {

    }

    /**
     * This inserts a new <code>OrderDetails</code> object.
     *
     * @param orderDetails The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "createOrderDetails")
    public OrderDetails registerOrderDetails(OrderDetails orderDetails) throws ConflictException {

        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(orderDetails).now();
        return orderDetails;
    }

    /**
     * This fetches <code>OrderDetails</code> object.
     *
     * @param orderId The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "getOrderDetails")
    public OrderDetails getOrderDetails(@Nullable @Named("orderId") Long orderId) throws ConflictException {

        OrderDetails orderDetails = ofy().load().type(OrderDetails.class).filter("id", orderId).first().now();
        return orderDetails;
    }
}
