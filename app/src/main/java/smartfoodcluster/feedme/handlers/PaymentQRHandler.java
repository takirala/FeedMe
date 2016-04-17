package smartfoodcluster.feedme.handlers;

import android.os.AsyncTask;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;

import java.util.List;

/**
 * Created by Lavenger on 4/17/2016.
 */
public class PaymentQRHandler extends AsyncTask {

    private final AsyncResponse delegate;

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    public interface AsyncResponse {
        void processFinish();
    }

    public PaymentQRHandler(AsyncResponse delegate) {
        this.delegate = delegate;
    }
}
