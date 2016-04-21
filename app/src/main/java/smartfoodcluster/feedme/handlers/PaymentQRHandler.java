package smartfoodcluster.feedme.handlers;

import android.os.AsyncTask;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Lavenger on 4/17/2016.
 */
public class PaymentQRHandler extends AsyncTask<Void, Void, Void> {

    public static final String TAG = "PaymentQRHandler";

    private final AsyncResponse delegate;

    @Override
    protected Void doInBackground(Void... params) {
        String uuid = null;
        try {
            Thread.sleep(1000);
            Log.e(TAG, "Received background slept");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface AsyncResponse {
        void processFinish();
    }

    public PaymentQRHandler(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        delegate.processFinish();
    }
}
