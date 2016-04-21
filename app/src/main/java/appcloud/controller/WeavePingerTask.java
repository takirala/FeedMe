package appcloud.controller;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.g3smartfoodcluster.weavePinger.WeavePinger;
import com.appspot.g3smartfoodcluster.weavePinger.model.Weave;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import smartfoodcluster.feedme.util.Constants;

/**
 * Created by Srinivas on 4/21/2016.
 */
public class WeavePingerTask extends AsyncTask<Boolean, Void, Void> {

    private static final String TAG = "WeavePingerTask";
    private static WeavePinger myApiService = null;

    @Override
    protected Void doInBackground(Boolean... params) {
        if (myApiService == null) { // Only do this once
            WeavePinger.Builder builder = new WeavePinger.Builder(AndroidHttp.newCompatibleTransport(),
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
            Weave w = new Weave();
            w.setBlinkFirst(true);
            WeavePinger.BlinkLED blinkLED = myApiService.blinkLED(w);
            blinkLED.execute();
            Log.e(TAG, "First BlinkLED Status code > " + blinkLED.getLastStatusCode());
        } catch (IOException e) {
            Log.e(TAG, "First BlinkLED failed" + e.getMessage());
        }

        try {
            Weave w = new Weave();
            w.setBlinkSecond(true);
            Thread.sleep(5000);
            WeavePinger.BlinkLED blinkLED = myApiService.blinkLED(w);
            blinkLED.execute();
            Log.e(TAG, "Second BlinkLED Status code > " + blinkLED.getLastStatusCode());
        } catch (Exception e) {
            Log.e(TAG, "Second BlinkLED failed" + e.getMessage());
        }
        return null;
    }
}






