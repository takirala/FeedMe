package smartfoodcluster.feedme.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.util.Constants;

public class UserViewMenu extends BaseActivity {

    private static final String TAG = "UserViewMenu";
    static List<String> menu = new ArrayList<String>();
    HashMap<String, Integer> orderedItemMap = new HashMap<String, Integer>();

    static {
        menu.add("Fancy Item 1");
        menu.add("Spicy Item 2");
        menu.add("Cool Item 3");
        menu.add("Cruncy Item 4");
        menu.add("Sizzling Item 5");
        menu.add("Fiery Item 6");
        menu.add("Exotic Dish 7");
        menu.add("Cruncy Item 8");
        menu.add("Sizzling Item 9");
        menu.add("Fiery Item 10");
        menu.add("Exotic Dish 11");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.get("resObject") != null) {
                Log.e(TAG, extras.getSerializable("resObject").toString());
            }
            String restaurantName = extras.getString(Constants.name);
            ((TextView) findViewById(R.id.RestaurantDescriptionGui)).setText(restaurantName);
            ((ImageView) findViewById(R.id.restaurantImage)).setImageResource(0);

            ListAdapter menuListAdapter = new RestaurantAdapter();
            ListView menuList = (ListView) findViewById(R.id.menuItemsGui);
            menuList.setAdapter(menuListAdapter);
        }
        Button checkoutButton = (Button) findViewById(R.id.checkoutButtonGui);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShoppingCart.class);
                i.putExtra(Constants.orderedItemMap, orderedItemMap);
                //resobject and ordereditemmap included.
                i.putExtras(getIntent());
                startActivity(i);
                setContentView(R.layout.activity_user_shopping_cart);
            }
        });
    }

    private class RestaurantAdapter extends ArrayAdapter<String> {

        public RestaurantAdapter() {
            super(UserViewMenu.this, R.layout.restaurant_list_view, menu);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.restaurant_menu_list_view, parent, false);
            }

            final String selectedRestaurant = menu.get(position);

            TextView restaurantMenuItemTextView = (TextView) thisView.findViewById(R.id.menuItemText);
            restaurantMenuItemTextView.setText(selectedRestaurant);
            final TextView countTextView = (TextView) thisView.findViewById(R.id.menuItemCount);

            Button addItemButtonView = (Button) thisView.findViewById(R.id.addItemButton);
            addItemButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String presentCount = (String) countTextView.getText();
                    Integer count = Integer.valueOf(presentCount) + 1;
                    countTextView.setText(count.toString());
                    orderedItemMap.put(selectedRestaurant, count);
                }
            });

            Button removeItemButtonView = (Button) thisView.findViewById(R.id.removeItemButton);
            removeItemButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String presentCount = (String) countTextView.getText();
                    Integer presentCountInt = Integer.valueOf(presentCount);
                    if (presentCountInt > 0) {
                        Integer count = presentCountInt - 1;
                        countTextView.setText(count.toString());
                        if (count > 0)
                            orderedItemMap.put(selectedRestaurant, count);
                        else
                            orderedItemMap.remove(selectedRestaurant);
                    }
                }
            });

            return thisView;
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
}
