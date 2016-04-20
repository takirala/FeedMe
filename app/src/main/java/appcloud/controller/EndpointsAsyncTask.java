package appcloud.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.appspot.myapplicationid.userEndpoint.UserEndpoint;
import com.appspot.myapplicationid.userEndpoint.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//import com.appspot.myapplicationid.restaurantEndpoint.RestaurantEndpoint;
//import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;

class EndpointsAsyncTask extends AsyncTask<Void, Void, List<User>> {
    private static UserEndpoint myApiService = null;
    private Context context;

    EndpointsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        if (myApiService == null) { // Only do this once
            UserEndpoint.Builder builder = new UserEndpoint.Builder(AndroidHttp.newCompatibleTransport(),
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
            return myApiService.listUsers().execute().getItems();
        } catch (IOException e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    protected void onPostExecute(List<User> result) {
        for (User q : result) {
            Toast.makeText(context, q.getEmail() + " : " + q.getName(), Toast.LENGTH_LONG).show();
        }
    }
}