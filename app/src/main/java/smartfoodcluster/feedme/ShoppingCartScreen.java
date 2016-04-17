package smartfoodcluster.feedme;

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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import smartfoodcluster.feedme.dao.ShoppingCartDao;

public class ShoppingCartScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HashMap<String, Integer> orderedItemsMap = new HashMap<String, Integer>();
    ArrayList<ShoppingCartDao> finalOrderListArray = new ArrayList<ShoppingCartDao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_screen);
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
            calculateSum(extras);
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
        getMenuInflater().inflate(R.menu.shopping_cart_screen, menu);
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

    public void calculateSum(Bundle extras) {
        if (extras != null) {
            orderedItemsMap = (HashMap<String, Integer>) extras.getSerializable("orderedItemMap");
            ListView orderSummaryListView = (ListView) findViewById(R.id.itemsOrderedListGui);
            finalOrderListArray = new ArrayList<ShoppingCartDao>();
            Integer totalBill = 0;

            for (String menuItem : orderedItemsMap.keySet()) {
                totalBill += orderedItemsMap.get(menuItem) * 45;
                finalOrderListArray.add(new ShoppingCartDao(menuItem, new Integer(45), orderedItemsMap.get(menuItem)));
            }
            ArrayAdapter<ShoppingCartDao> adapter = new ShoppingCartAdapter();
            orderSummaryListView.setAdapter(adapter);
            ((TextView) findViewById(R.id.totalAmountGui)).setText(totalBill.toString());

        }

    }


    private class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartDao> {

        public ShoppingCartAdapter() {
            super(ShoppingCartScreen.this, R.layout.shopping_cart_list_view, finalOrderListArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView=convertView;
            if(thisView==null){
                thisView= getLayoutInflater().inflate(R.layout.shopping_cart_list_view,parent,false);
            }

            ShoppingCartDao itemOnFocus = finalOrderListArray.get(position);
            TextView restaurantNameGui = (TextView)thisView.findViewById(R.id.menuItemGui);
            restaurantNameGui.setText(itemOnFocus.getMenuItem());
            TextView countGui = (TextView)thisView.findViewById(R.id.itemCountGui);
            countGui.setText(itemOnFocus.getCountForItem().toString());
            TextView amountPerItem =(TextView)thisView.findViewById(R.id.totalPerItemGui);
            amountPerItem.setText(new Integer(itemOnFocus.getCountForItem()*itemOnFocus.getCostForItem()).toString());

            return thisView;
        }


    }
}
