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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartfoodcluster.feedme.util.Constants;

public class ListOrdersTask extends AsyncTask<String, Void, List<Order>> {
    private static OrderEndpoint myApiService = null;
    private Context context;

    public static final String TAG = "AddOrderAsyncTask";

    public ListOrdersTask(Context context) {
    }

    @Override
    protected List<Order> doInBackground(String... params) {
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


        // List orders
        try {
            OrderEndpoint.ListOrders listOrders = myApiService.listOrders();
            List<Order> orders = new ArrayList<>();
            for (int i = 0; i < listOrders.size(); i++) {
                Order o = (Order) listOrders.get(i);
                if (o.getOrderUUID().equals(params[0]))
                    orders.add(o);
            }

        } catch (Exception e) {
            return Collections.EMPTY_LIST;
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