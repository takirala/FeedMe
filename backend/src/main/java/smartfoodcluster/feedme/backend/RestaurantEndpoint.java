package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import smartfoodcluster.feedme.util.Restaurant;
import smartfoodcluster.feedme.util.User;

import static smartfoodcluster.feedme.util.OfyService.ofy;

/**
 * Created by Srinivas on 4/16/2016.
 */

@Api(name = "restaurantEndpoint")
public class RestaurantEndpoint {
    public RestaurantEndpoint() {
    }

    /**
     * This inserts a new <code>User</code> object.
     *
     * @param restaurant The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "registerRestaurant")
    public Restaurant registerRestaurant(Restaurant restaurant) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present
        if (restaurant.getId() != null) {
            if (findRecord(restaurant.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(restaurant).now();
        return restaurant;
    }

    /**
     * This updates an existing <code>Restaurant</code> object.
     *
     * @param restaurant The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateRestaurant")
    public Restaurant updateRestaurant(Restaurant restaurant) throws NotFoundException {
        if (findRecord(restaurant.getId()) == null) {
            throw new NotFoundException("Restaurant Record does not exist");
        }
        ofy().save().entity(restaurant).now();
        return restaurant;
    }

    /**
     * This deletes an existing <code>Restaurant</code> object.
     *
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeRestaurant")
    public void removeRestaurant(@Named("id") Long id) throws NotFoundException {
        Restaurant record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("Restaurant Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    @ApiMethod(name = "listRestaurants")
    public CollectionResponse<Restaurant> listRestauranr(@Nullable @Named("cursor") String cursorString,
                                              @Nullable @Named("count") Integer count) {

        Query<Restaurant> query = ofy().load().type(Restaurant.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<Restaurant> records = new ArrayList<Restaurant>();
        QueryResultIterator<Restaurant> iterator = query.iterator();
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
        return CollectionResponse.<Restaurant>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    //Private method to retrieve a <code>Quote</code> record
    private Restaurant findRecord(Long id) {
        return ofy().load().type(Restaurant.class).id(id).now();
    }
}

