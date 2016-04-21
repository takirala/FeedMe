package appcloud.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.g3smartfoodcluster.orderEndpoint.OrderEndpoint;
import com.appspot.g3smartfoodcluster.orderEndpoint.model.CollectionResponseOrder;
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

public class ListOrdersTask extends AsyncTask<Void, Void, List<Order>> {
    private static OrderEndpoint myApiService = null;
    private Context context;

    public static final String TAG = "AddOrderAsyncTask";

    public AsyncResponse delegate = null;

    public ListOrdersTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(List<Order> output);
    }

    public ListOrdersTask(Context context) {
    }

    @Override
    protected List<Order> doInBackground(Void... params) {
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
        List<Order> orders = new ArrayList<>();
        try {
            CollectionResponseOrder listOrders = myApiService.listOrders().execute();
            return listOrders.getItems();
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }


    @Override
    protected void onPostExecute(List<Order> places) {
        delegate.processFinish(places);
    }


}