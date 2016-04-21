package smartfoodcluster.feedme.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;
import com.appspot.g3smartfoodcluster.restaurantEndpoint.model.Restaurant;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import appcloud.controller.AddOrderAsyncTask;
import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.dao.ShoppingCartDao;
import smartfoodcluster.feedme.util.Constants;

public class ShoppingCart extends BaseActivity {

    public static final String TAG = "ShoppingCart";
    HashMap<String, Integer> orderedItemsMap = new HashMap<String, Integer>();
    ArrayList<ShoppingCartDao> finalOrderListArray = new ArrayList<ShoppingCartDao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, Constants.writeToUs, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            final Float totalBill = calculateSum(extras);
            Button makePaymentButton = (Button) findViewById(R.id.makePaymentButton);
            makePaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String resStr = (String) extras.get(Constants.resObject);
                    Log.e(TAG, "###" + resStr);

                    Restaurant r = null;
                    try {
                        JsonFactory factory = new AndroidJsonFactory();
                        //JSONObject jsonObject = new JSONObject(resStr.trim());
                        r = factory.fromString(resStr.trim(), Restaurant.class);
                        Log.e(TAG, "" + r);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Order order = new Order();
                    order.setOrderUUID(JSONObject.quote(UUID.randomUUID().toString()));
                    order.setRestaurantPlaceId(JSONObject.quote(r.getPlaceId()));
                    SimpleDateFormat sdf = new SimpleDateFormat("E_HH_mm_a");
                    String now = sdf.format(new Date()).toString();
                    order.setOrderDate(JSONObject.quote(now));
                    order.setStatus(Constants.NEW);
                    order.setTotalAmount(totalBill);
                    orderedItemsMap = (HashMap<String, Integer>) extras.getSerializable(Constants.orderedItemMap);

                    StringBuffer sb = new StringBuffer();
                    for (String key : orderedItemsMap.keySet()) {
                        if (sb.length() == 0)
                            sb.append(key + "#" + orderedItemsMap.get(key));
                        else
                            sb.append("|" + key + "#" + orderedItemsMap.get(key));
                    }
                    order.setOrderDetails(JSONObject.quote(sb.toString()));
                    saveToCloud(r, order);
                    Log.e(TAG, "Saved on cloud");
                    Intent i = new Intent(getApplicationContext(), UserPayment.class);
                    try {
                        i.putExtra(Constants.resObject, r.toPrettyString());
                        i.putExtra(Constants.orderObject, order.toPrettyString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i.putExtras(getIntent());
                    startActivity(i);
                    setContentView(R.layout.activity_user_payment);
                }
            });
        }
    }

    private void saveToCloud(Restaurant restaurant, Order order) {
        AddOrderAsyncTask task = new AddOrderAsyncTask(getApplicationContext());
        task.execute(order);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        finish();
        /*if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/
    }

    public Float calculateSum(Bundle extras) {
        Float totalBill = 0f;
        if (extras != null) {
            orderedItemsMap = (HashMap<String, Integer>) extras.getSerializable("orderedItemMap");
            ListView orderSummaryListView = (ListView) findViewById(R.id.itemsOrderedListGui);
            finalOrderListArray = new ArrayList<ShoppingCartDao>();

            for (String menuItem : orderedItemsMap.keySet()) {
                totalBill += orderedItemsMap.get(menuItem) * 45;
                finalOrderListArray.add(new ShoppingCartDao(menuItem, new Integer(45), orderedItemsMap.get(menuItem)));
            }
            ArrayAdapter<ShoppingCartDao> adapter = new ShoppingCartAdapter();
            orderSummaryListView.setAdapter(adapter);
            ((TextView) findViewById(R.id.totalAmountGui)).setText(totalBill.toString());
        }
        return totalBill;
    }

    public void makePayment(View view) {

    }

    private class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartDao> {

        public ShoppingCartAdapter() {
            super(ShoppingCart.this, R.layout.shopping_cart_list_view, finalOrderListArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.shopping_cart_list_view, parent, false);
            }

            ShoppingCartDao itemOnFocus = finalOrderListArray.get(position);
            TextView restaurantNameGui = (TextView) thisView.findViewById(R.id.menuItemGui);
            restaurantNameGui.setText(itemOnFocus.getMenuItem());
            TextView countGui = (TextView) thisView.findViewById(R.id.itemCountGui);
            countGui.setText(itemOnFocus.getCountForItem().toString());
            TextView amountPerItem = (TextView) thisView.findViewById(R.id.totalPerItemGui);
            amountPerItem.setText(new Integer(itemOnFocus.getCountForItem() * itemOnFocus.getCostForItem()).toString());

            return thisView;
        }
    }
}
