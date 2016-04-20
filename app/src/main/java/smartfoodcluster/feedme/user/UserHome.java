package smartfoodcluster.feedme.user;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.dao.RestaurantGui;
import smartfoodcluster.feedme.handlers.LocationHandler;
import smartfoodcluster.feedme.util.Constants;

public class UserHome extends BaseActivity {

    protected static final String TAG = "UserHome";

    /**
     * Represents a geographical location.
     */

    protected Location mLastLocation;

    protected Double mLatitudeText;
    protected Double mLongitudeText;
    List<RestaurantGui> restaurants = new ArrayList<RestaurantGui>();
    List<Restaurant> res = new ArrayList<Restaurant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_home);

        double[] loc = getLastBestLocation();
        Log.e(TAG, "Location found " + loc[0] + " " + loc[1]);

        LocationHandler locationHandler = (LocationHandler) new LocationHandler(new LocationHandler.AsyncResponse() {
            @Override
            public void processFinish(List<Restaurant> output) {
                Log.e(TAG, "Search performed");
                for (Restaurant p : output) {
                    res.add(p);
                }
                findViewById(R.id.progressBarRestaurantSearch).setVisibility(View.GONE);
                findViewById(R.id.restaurantList).setVisibility(View.VISIBLE);
                ListAdapter restaurantAdapter = new RestaurantAdapter();
                ListView restaurantListView = (ListView) findViewById(R.id.restaurantList);
                restaurantListView.setAdapter(restaurantAdapter);
                restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Restaurant r = res.get(position);
                        String selectedRestaurant = r.getName();
                        Toast.makeText(UserHome.this, selectedRestaurant, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(), UserViewMenu.class);
                        i.putExtra(Constants.name, selectedRestaurant);
                        try {
                            i.putExtra(Constants.resObject, r.toPrettyString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        i.putExtras(getIntent());
                        startActivity(i);
                        setContentView(R.layout.activity_user_view_menu);
                    }
                });
            }
        }).execute(loc);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private double[] getLastBestLocation() {
        double[] loc = new double[2];
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location l = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Enable location permission!!!!");
            l = null;
        } else {
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
                l = locationGPS;
            } else {
                l = locationNet;
            }
        }

        if (l == null) {
            // Default to MSL @UFL
            loc[0] = 29.6481041;
            loc[1] = -82.3462533;
        } else {
            loc[0] = l.getLatitude();
            loc[1] = l.getLongitude();
        }
        return loc;
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {

        public RestaurantAdapter() {
            super(UserHome.this, R.layout.restaurant_list_view, res);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
            }
            Restaurant r = res.get(position);
            TextView resName = ((TextView) thisView.findViewById(R.id.restaurantName));
            resName.setText(r.getName().replace("\"", ""));
            String rating = "NA";
            String priceLevel = "NA";
            if (r.getRating() != null && !r.getRating().isEmpty())
                rating = r.getRating();
            if (r.getPriceLevel() != null && !r.getPriceLevel().isEmpty())
                priceLevel = r.getPriceLevel();


            ((TextView) thisView.findViewById(R.id.restaurantPriceLevel)).setText(("PriceLevel : " + priceLevel).replace("\"", ""));
            ((TextView) thisView.findViewById(R.id.restaurantRating)).setText(("Rating : " + rating).replace("\"", ""));
            ((TextView) thisView.findViewById(R.id.restaurantVicinity)).setText((r.getVicinity()).replace("\"", ""));
            return thisView;
        }
    }
}
