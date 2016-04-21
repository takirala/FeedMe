package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;

import smartfoodcluster.feedme.entities.Order;
import smartfoodcluster.feedme.util.WeaveService;

import static smartfoodcluster.feedme.util.OfyService.ofy;

/**
 * Created by Srinivas on 4/21/2016.
 */

@Api(name = "weavePinger")
public class WeavePinger {

    WeaveService ws = null;

    public WeavePinger() {
    }


    @ApiMethod(name = "blinkLED")
    public void blinkLED(@Named("doShiver") boolean doShiver) throws ConflictException {
        if (ws == null) {
            ws = new WeaveService();
        }
        ws.run();
        return;
    }
}
