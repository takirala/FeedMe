package smartfoodcluster.feedme.handlers;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.g3smartfoodcluster.restaurantEndpoint.model.Restaurant;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lavenger on 4/16/2016.
 */
public class LocationHandler extends AsyncTask<double[], Void, List<Restaurant>> {

    //public static String API_KEY = "AIzaSyB0wZSmNvQgaPj4TdvtGBqzTE2qCfmJCgQ";
    //public static String API_KEY = "AIzaSyBQiQzsH6YNX4ZUtM1Uzxm2_5pd7kWH7iI";
    public static String API_KEY = "AIzaSyDxXRuLPwgLebC_tzEleXoJnK8cjFdAOUo";

    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";

    private static final HttpTransport transport = new ApacheHttpTransport();
    private static final boolean PRINT_AS_STRING = false;
    private static final String TAG = "LocationHandler";

    public LocationHandler(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {

        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                HttpHeaders headers = new HttpHeaders();
                headers.setUserAgent("Google-Places-FeedMe");
                request.setHeaders(headers);
                JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
                request.setParser(parser);
            }
        });
    }

    @Override
    protected List<Restaurant> doInBackground(double[]... params) {
        List<Restaurant> solution = new ArrayList<>();
        try {
            double[] location = params[0];

            Log.e(TAG, "Perform Search ....");
            Log.e(TAG, "-------------------");
            HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
            GenericUrl url = new GenericUrl(PLACES_SEARCH_URL);
            url.put("key", API_KEY);
            url.put("location", location[0] + "," + location[1]);
            url.put("radius", 500);
            url.put("sensor", "false");
            url.put("rankby", "prominence");
            url.put("type", "restaurant");
            HttpRequest request = httpRequestFactory.buildGetRequest(url);
            if (PRINT_AS_STRING) {
                Log.e(TAG, request.execute().parseAsString());
            } else {
                String placesStr = request.execute().parseAsString();
                JSONObject jObject = new JSONObject(placesStr);
                Log.e(TAG, "Parsed " + jObject.toString());
                solution = parseResult(jObject);
            }
        } catch (Exception e) {
            for (StackTraceElement elem : e.getStackTrace()) {
                Log.e(TAG, elem.toString());
            }
            Log.e(TAG, "Failed > " + e);
        }
        return solution;
    }

    private List<Restaurant> parseResult(JSONObject jObject) throws JSONException {
        List<Restaurant> solution = new ArrayList<>();
        if (jObject.has("results")) {
            JSONArray jArray = jObject.getJSONArray("results");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject r = (JSONObject) jArray.get(i);
                Restaurant rObj = new Restaurant();
                if (r.has("name"))
                    rObj.setName(JSONObject.quote(r.getString("name")));
                if (r.has("price_level"))
                    rObj.setPriceLevel(JSONObject.quote(r.getString("price_level") + "*"));
                if (r.has("rating"))
                    rObj.setRating(JSONObject.quote(r.getString("rating") + "*"));
                if (r.has("vicinity"))
                    rObj.setVicinity(JSONObject.quote(r.getString("vicinity")));
                if (r.has("place_id"))
                    rObj.setPlaceId(JSONObject.quote(r.getString("place_id")));
                solution.add(rObj);
            }
        }
        return solution;
    }

    public interface AsyncResponse {
        void processFinish(List<Restaurant> output);
    }

    public AsyncResponse delegate = null;

    @Override
    protected void onPostExecute(List<Restaurant> places) {
        delegate.processFinish(places);
    }


    public static void getNearbyRestaurants(Location location) {

    }
}
