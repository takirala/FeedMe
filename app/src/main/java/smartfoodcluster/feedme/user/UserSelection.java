package smartfoodcluster.feedme.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;
import smartfoodcluster.feedme.UserViewMenuScreen;
import smartfoodcluster.feedme.dao.RestaurantGui;
import smartfoodcluster.feedme.util.Constants;

import smartfoodcluster.feedme.R;

import smartfoodcluster.feedme.handlers.LocationHandler;
import smartfoodcluster.feedme.dao.RestaurantGui;
import smartfoodcluster.feedme.util.Constants;

public class UserSelection extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    protected static final String TAG = "UserSelection";

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    protected Double mLatitudeText;
    protected Double mLongitudeText;
    List<RestaurantGui> restaurants = new ArrayList<RestaurantGui>();
    List<Place> res = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_selection);

        Location location = getLastBestLocation();
        if (res.size() == 0) {
            Log.e(TAG, "Performing search ");
            double[] loc = new double[2];

            if (location == null) {
                // Default to MSL @UFL
                loc[0] = 29.6481041;
                loc[1] = -82.3462533;
            } else {
                loc[0] = location.getLatitude();
                loc[1] = location.getLongitude();
            }
            Log.e(TAG, "Location found " + loc[0] + " " + loc[1]);

            LocationHandler locationHandler = (LocationHandler) new LocationHandler(new LocationHandler.AsyncResponse() {
                @Override
                public void processFinish(List<Restaurant> output) {
                    for (Restaurant p : output) {
                        Log.e(TAG, p.toString());
                    }
                    Log.e(TAG, "Search performed");
                }
            }).execute(loc);

        } else {
            Log.e(TAG, "Location not found");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        List restaurantLista = populateList();

        //ListAdapter restaurantAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,restaurantList);
        ListAdapter restaurantAdapter = new RestaurantAdapter();
        ListView restaurantListGui = (ListView) findViewById(R.id.restaurantList);
        restaurantListGui.setAdapter(restaurantAdapter);

        restaurantListGui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestaurantGui selectedRestaurantGui = restaurants.get(position);
                String selectedRestaurant = selectedRestaurantGui.getRestaurantName();
                Toast.makeText(UserSelection.this, selectedRestaurant, Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), UserViewMenuScreen.class);
                i.putExtra("restarantName", selectedRestaurant);
                i.putExtra("RestaurantIcon", selectedRestaurantGui.getRestaurantIconId());
                startActivity(i);
                setContentView(R.layout.activity_user_view_menu_screen);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private Location getLastBestLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Enable location permission!!!!");
            return null;
        }
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private List<RestaurantGui> populateList() {
        restaurants.add(new RestaurantGui("BoneFish", R.drawable.bonefish));
        restaurants.add(new RestaurantGui("BigBurger", R.drawable.bigburger));
        restaurants.add(new RestaurantGui("Chipotle", R.drawable.chipotle));
        restaurants.add(new RestaurantGui("McDonalds", R.drawable.mcdonalds));
        restaurants.add(new RestaurantGui("Publix", R.drawable.publix));
        restaurants.add(new RestaurantGui("Subway", R.drawable.subway));
        return restaurants;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e(TAG, "Yayy location connected");
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(UserSelection.this, Constants.requestLocation, Toast.LENGTH_LONG).show();
            return;
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //  public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        if (mLastLocation != null) {
            mLatitudeText = mLastLocation.getLatitude();
            mLongitudeText = mLastLocation.getLongitude();
            Toast.makeText(this, "Hurray !! " + mLatitudeText + " , " + mLongitudeText, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    private class RestaurantAdapter extends ArrayAdapter<RestaurantGui> {

        public RestaurantAdapter() {
            super(UserSelection.this, R.layout.restaurant_list_view, restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
            }

            RestaurantGui selectedRestaurant = restaurants.get(position);

            ImageView restaurantImageView = (ImageView) thisView.findViewById(R.id.restaurantIcon);
            restaurantImageView.setImageResource(selectedRestaurant.getRestaurantIconId());

            TextView restaurantNameTextView = (TextView) thisView.findViewById(R.id.restaurantName);
            restaurantNameTextView.setText(selectedRestaurant.getRestaurantName());



            return thisView;
        }

    }


}
