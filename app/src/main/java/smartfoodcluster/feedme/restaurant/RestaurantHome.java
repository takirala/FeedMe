package smartfoodcluster.feedme.restaurant;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import appcloud.controller.ListOrdersTask;
import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.qrcode.QRCodeEncoder;
import smartfoodcluster.feedme.qrcode.QRContents;
import smartfoodcluster.feedme.user.BaseActivity;
import smartfoodcluster.feedme.util.Constants;

public class RestaurantHome extends BaseActivity {

    protected static final String TAG = "RestaurantHome";

    /**
     * Represents a geographical location.
     */

    List<Order> res = new ArrayList<Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);

        ListOrdersTask ordersHandle = (ListOrdersTask) new ListOrdersTask(new ListOrdersTask.AsyncResponse() {
            @Override
            public void processFinish(List<Order> output) {
                Log.e(TAG, "Fetch Performed");
                if (output == null || output.size() == 0) {
                    Toast.makeText(RestaurantHome.this, "No orders yet!! Come back again.", Toast.LENGTH_LONG);
                    return;
                }
                for (Order p : output) {
                    res.add(p);
                }
                findViewById(R.id.progressBarOrderSearch).setVisibility(View.GONE);
                findViewById(R.id.restaurantOrdersList).setVisibility(View.VISIBLE);
                ListAdapter orderAdapter = new OrderAdapter();
                ListView restaurantListView = (ListView) findViewById(R.id.restaurantOrdersList);
                restaurantListView.setAdapter(orderAdapter);
//                restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Order r = res.get(position);
//
//                        Intent i = new Intent(getApplicationContext(), UserViewMenu.class);
//                        i.putExtra(Constants.name, selectedRestaurant);
//                        try {
//                            i.putExtra(Constants.resObject, r.toPrettyString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        i.putExtras(getIntent());
//                        startActivity(i);
//                        setContentView(R.layout.activity_user_view_menu);
//                    }
//                });
            }
        }).execute();

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

    private class OrderAdapter extends ArrayAdapter<Order> {

        public OrderAdapter() {
            super(RestaurantHome.this, R.layout.restaurant_list_view, res);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView = convertView;
            if (thisView == null) {
                thisView = getLayoutInflater().inflate(R.layout.order_list_view, parent, false);
            }
            Order r = res.get(position);
            ((TextView) thisView.findViewById(R.id.orderIdString)).setText("#ID    :      \t\t\t\t" + r.getOrderUUID().split("-")[0].replace("\"", ""));
            ((TextView) thisView.findViewById(R.id.orderAmountString)).setText("Cost   :      \t\t\t\t$" + r.getTotalAmount());
            ((TextView) thisView.findViewById(R.id.orderDateString)).setText("Date   :      \t\t\t\t" + r.getOrderDate());
            if (r.getOrderDetails() != null)
                ((TextView) thisView.findViewById(R.id.orderDetailsString)).setText(r.getOrderDetails());
            ((TextView) thisView.findViewById(R.id.orderStatusString)).setText("Status:      \t\t\t\t" + Constants.getStatus(r.getStatus()));
            ImageView qrCodeView = (ImageView) thisView.findViewById(R.id.orderQRCodeView);
            setView(r.getOrderUUID(), qrCodeView);
            return thisView;
        }
    }

    public void setView(String qrInputText, ImageView myImage) {
        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInputText,
                null,
                QRContents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            myImage.setImageBitmap(bitmap);
            myImage.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
