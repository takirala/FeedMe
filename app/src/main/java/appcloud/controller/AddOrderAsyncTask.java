package appcloud.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.appspot.g3smartfoodcluster.orderEndpoint.OrderEndpoint;
import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import smartfoodcluster.feedme.util.Constants;

/**
 * Created by Srinivas on 4/17/2016.
 */
public class AddOrderAsyncTask extends AsyncTask<Object, Void, List<Order>> {
    private static OrderEndpoint myApiService = null;
    private Context context;

    public static final String TAG = "AddOrderAsyncTask";

    public AddOrderAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Order> doInBackground(Object... params) {
        if (myApiService == null) { // Only do this once
            OrderEndpoint.Builder builder = new OrderEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(Constants.url)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            myApiService = builder.build();
        }
        try {
            Order o = (Order) params[0];
            OrderEndpoint.RegisterOrder registerOrder = myApiService.registerOrder(o);
            registerOrder.execute();
            Log.e(TAG, "Status code > " + registerOrder.getLastStatusCode());
        } catch (IOException e) {
            Log.e(TAG, "Registering order failed" + e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void onPostExecute(List<Order> result) {
        for (Order q : result) {
            Toast.makeText(context, q.getId() + " : " + q.getStatus(), Toast.LENGTH_LONG).show();
        }
    }
}