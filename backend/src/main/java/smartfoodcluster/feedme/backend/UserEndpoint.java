package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
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

import smartfoodcluster.feedme.util.User;

import static smartfoodcluster.feedme.util.OfyService.ofy;

/**
 * Created by Lavenger on 4/16/2016.
 */
@Api(name = "userEndpoint")
public class UserEndpoint {
    public UserEndpoint() {

    }

    /**
     * This inserts a new <code>User</code> object.
     *
     * @param user The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "registerUser")
    public User registerUser(User user) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present
        if (user.getId() != null) {
            if (findRecord(user.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This updates an existing <code>User</code> object.
     *
     * @param user The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateUser")
    public User updateUser(User user) throws NotFoundException {
        if (findRecord(user.getId()) == null) {
            throw new NotFoundException("Quote Record does not exist");
        }
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This deletes an existing <code>User</code> object.
     *
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeUser")
    public void removeUser(@Named("id") Long id) throws NotFoundException {
        User record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    @ApiMethod(name = "listUsers")
    public CollectionResponse<User> listQuote(@Nullable @Named("cursor") String cursorString,
                                              @Nullable @Named("count") Integer count) {

        Query<User> query = ofy().load().type(User.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }

        List<User> records = new ArrayList<User>();
        QueryResultIterator<User> iterator = query.iterator();
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
        return CollectionResponse.<User>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    //Private method to retrieve a <code>Quote</code> record
    private User findRecord(Long id) {
        return ofy().load().type(User.class).id(id).now();
    }
}
