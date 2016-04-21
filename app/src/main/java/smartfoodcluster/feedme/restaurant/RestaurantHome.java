package smartfoodcluster.feedme.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.dao.OrderView;
import smartfoodcluster.feedme.fragments.NewOrdersFragment;
import smartfoodcluster.feedme.fragments.TabFragment;

public class RestaurantHome extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    List<OrderView> orders = new ArrayList<OrderView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.menuDrawer);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();


        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_orders) {
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new NewOrdersFragment()).commit();

                }

                if (menuItem.getItemId() == R.id.nav_edit_details) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                return false;
            }

        });

        List<OrderView> orderList = new ArrayList<OrderView>();

        orderList.add(new OrderView(123L, "123"));
        orderList.add(new OrderView(122L, "113"));

        ListAdapter orderAdapter = new OrderAdapter();
        ListView orderListView = (ListView) findViewById(R.id.neworderlist);
        orderListView.setAdapter(orderAdapter);

        Log.e("After Adapter", "After adapter");

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderView selectedOrderView = orders.get(position);
                Long selectedOrder = selectedOrderView.getOrderId();
                Log.e("Order Id", selectedOrder.toString());
                Log.e("Order Time", selectedOrderView.getOrderTime().toString());
                Toast.makeText(RestaurantHome.this, selectedOrder.toString(), Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), OrderView.class);
                i.putExtra("orderId", selectedOrder);
                i.putExtra("orderTime", selectedOrderView.getOrderTime());
                startActivity(i);
                setContentView(R.layout.activity_order_view);
            }
        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    private class OrderAdapter extends ArrayAdapter<OrderView> {

        public OrderAdapter() {
            super(RestaurantHome.this, R.layout.order_list_view, orders);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.order_list_view, parent, false);
            }

            OrderView selectedOrderView = orders.get(position);

            TextView orderIdView = (TextView) thisView.findViewById((int) R.id.orderId);
            orderIdView.setText(selectedOrderView.getOrderId().toString());

            TextView orderTimeView = (TextView) thisView.findViewById(R.id.orderTime);
            orderTimeView.setText(selectedOrderView.getOrderTime());
            return thisView;
        }

    }
}
