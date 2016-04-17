package smartfoodcluster.feedme.handlers;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;

import java.util.List;
import java.util.UUID;

/**
 * Created by Lavenger on 4/17/2016.
 */
public class PaymentQRHandler extends AsyncTask<Void, Void, String> {

    public static final String TAG = "PaymentQRHandler";

    private final AsyncResponse delegate;

    @Override
    protected String doInBackground(Void... params) {
        Log.e(TAG, "Received background sleep");
        String uuid = null;
        try {
            Thread.sleep(5000);
            Log.e(TAG, "Received background slept");
            uuid = UUID.randomUUID().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public interface AsyncResponse {
        void processFinish(String uuid);
    }

    public PaymentQRHandler(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @Override
    protected void onPostExecute(String uuid) {
        delegate.processFinish(uuid);
    }
}
