package smartfoodcluster.feedme.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import smartfoodcluster.feedme.dao.RestaurantGui;
import smartfoodcluster.feedme.util.Constants;

public class UserViewMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<RestaurantGui> restaurants = new ArrayList<RestaurantGui>();
    HashMap<String, Integer> orderedItemMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_menu_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String restaurantName = extras.getString("restaurantName");
            ((TextView) findViewById(R.id.RestaurantDescriptionGui)).setText(restaurantName);
            ((ImageView) findViewById(R.id.restaurantImage)).setImageResource(extras.getInt("RestaurantIcon"));

            List restaurantList = populateList();

            //ListAdapter restaurantAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,restaurantList);
            ListAdapter restaurantAdapter = new RestaurantAdapter();
            ListView restaurantListGui = (ListView) findViewById(R.id.menuItemsGui);
            restaurantListGui.setAdapter(restaurantAdapter);

          /*  String[] menu = {restaurantName+" Sushi 65",restaurantName+" Tandoori Tandeloin",restaurantName+" Eat and Die",restaurantName+" Wings",
                    restaurantName+" Dosa",restaurantName+" Idli"};
            ListAdapter menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);
            ((ListView) findViewById(R.id.menuListGui)).setAdapter(menuAdapter);*/
        }

        Button checkoutButton = (Button) findViewById(R.id.checkoutButtonGui);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShoppingCart.class);
                i.putExtra("orderedItemMap", orderedItemMap);
                i.putExtra(Constants.placeId, "abc123");
                i.putExtra(Constants.emailId, "tarugupta.92@gmail.com");
                startActivity(i);
                setContentView(R.layout.activity_shopping_cart_screen);
               /* ArrayList<String> orderedList=new ArrayList<String>();
                ListView orderedItemView = (ListView)findViewById(R.id.menuItemsGui);
                ListAdapter menuItemsAdapter = orderedItemView.getAdapter();
                for(int i=0;i<menuItemsAdapter.getCount();i++){
                      View item = (View)menuItemsAdapter.getItem(i);
                   // ViewParent parent = item.getParent();
                    //thisView=getLayoutInflater().inflate(R.layout.restaurant_menu_list_view, parent, false);
                    View thisView=getLayoutInflater().inflate(R.layout.restaurant_menu_list_view,null);
                    CharSequence count = ((TextView)thisView.findViewById(R.id.menuItemCount)).getText();

                }*/

            }
        });
    }

    private List<RestaurantGui> populateList() {
        restaurants.add(new RestaurantGui("Chicken Nuggets", R.drawable.bonefish));
        restaurants.add(new RestaurantGui("Chicken Burger", R.drawable.bigburger));
        restaurants.add(new RestaurantGui("Chicken Sandwitch", R.drawable.chipotle));
        restaurants.add(new RestaurantGui("Chicken Pizza", R.drawable.mcdonalds));
        restaurants.add(new RestaurantGui("Chicken Noodles", R.drawable.publix));
        restaurants.add(new RestaurantGui("Chicken Ramoon", R.drawable.subway));
        return restaurants;
    }


    private class RestaurantAdapter extends ArrayAdapter<RestaurantGui> {

        public RestaurantAdapter() {
            super(UserViewMenu.this, R.layout.restaurant_list_view, restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.restaurant_menu_list_view, parent, false);
            }

            final RestaurantGui selectedRestaurant = restaurants.get(position);


            TextView restaurantMenuItemTextView = (TextView) thisView.findViewById(R.id.menuItemText);
            restaurantMenuItemTextView.setText(selectedRestaurant.getRestaurantName());
            final TextView countTextView = (TextView) thisView.findViewById(R.id.menuItemCount);

            Button addItemButtonView = (Button) thisView.findViewById(R.id.addItemButton);
            addItemButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String presentCount = (String) countTextView.getText();

                    if (presentCount.equals("")) {
                        System.out.println(presentCount);
                        countTextView.setText(new String("1"));
                    } else {
                        Integer count = Integer.valueOf(presentCount) + 1;
                        countTextView.setText(count.toString());
                    }
                    orderedItemMap.put(selectedRestaurant.getRestaurantName(), Integer.valueOf(countTextView.getText().toString()));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_view_menu_screen, menu);
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
