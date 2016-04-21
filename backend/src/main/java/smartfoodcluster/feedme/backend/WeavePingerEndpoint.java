package smartfoodcluster.feedme.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;

import smartfoodcluster.feedme.entities.Weave;
import smartfoodcluster.feedme.util.WeaveService;

/**
 * Created by Srinivas on 4/21/2016.
 */

@Api(name = "weavePinger")
public class WeavePingerEndpoint {

    WeaveService ws = null;

    public WeavePingerEndpoint() {
    }


    @ApiMethod(name = "blinkLED")
    public void blinkLED(Weave command) throws ConflictException {
        if (ws == null) {
            ws = new WeaveService();
        }
        ws.run(command);
        return;
    }
}
