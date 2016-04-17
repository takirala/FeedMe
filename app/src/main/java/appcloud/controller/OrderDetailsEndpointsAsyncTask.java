package appcloud.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.appspot.myapplicationid.orderDetailsEndpoint.OrderDetailsEndpoint;
import com.appspot.myapplicationid.orderDetailsEndpoint.model.OrderDetails;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Srinivas on 4/17/2016.
 */
public class OrderDetailsEndpointsAsyncTask extends AsyncTask<Void, Void, OrderDetails> {
    private static OrderDetailsEndpoint myApiService = null;
    private Context context;

    OrderDetailsEndpointsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected OrderDetails doInBackground(Void... params) {
        if (myApiService == null) { // Only do this once
            OrderDetailsEndpoint.Builder builder = new OrderDetailsEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
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
            return myApiService.getOrderDetails().execute();
        } catch (IOException e) {
            return (OrderDetails) null;
        }
    }

 /*   @Override
    protected void onPostExecute(OrderDetails result) {
        for (OrderDetails q : result) {
            Toast.makeText(context, q.getOrderId() + " : " + q.getItemTotal(), Toast.LENGTH_LONG).show();
        }
    }*/
}