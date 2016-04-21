package smartfoodcluster.feedme.qrcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import appcloud.controller.ListOrdersTask;
import appcloud.controller.WeavePingerTask;
import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.user.BaseActivity;
import smartfoodcluster.feedme.util.Constants;

public class QRCodeScanner extends BaseActivity {

    public static final String TAG = "QRCodeScanner";
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

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

    //product qr code mode
    public void scanQR(View v) {

        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(QRCodeScanner.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //listOrders(contents);
                boolean isValidOrder = verifyOrderOnCloud(contents);
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }

    private boolean verifyOrderOnCloud(final String orderId) {

        ListOrdersTask locationHandler = (ListOrdersTask) new ListOrdersTask(new ListOrdersTask.AsyncResponse() {
            @Override
            public void processFinish(List<Order> output) {
                Log.e(TAG, "Looking for orderId " + orderId + " Result size : " + output.size());
                for (Order o : output) {
                    Log.e(TAG, "Order uuid : " + o.getOrderUUID());
                    if (o.getOrderUUID().contains(orderId)) {
                        try {
                            Log.e(TAG, "Order matched \n >> " + o.toPrettyString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        displayOrder(o);
                        try {
                            Toast.makeText(QRCodeScanner.this, o.toPrettyString(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        blinkLED();
                        break;
                    } else continue;
                }
            }
        }).execute();
        return false;
    }

    private void blinkLED() {
        WeavePingerTask task = new WeavePingerTask();
        Toast.makeText(QRCodeScanner.this, "Sending Request", Toast.LENGTH_SHORT).show();
        task.execute(true);
    }


    private void displayOrder(Order o) {

        findViewById(R.id.scannedDetails).setVisibility(View.VISIBLE);

        if (o.getOrderUUID() != null) {
            ((TextView) findViewById(R.id.orderIdText)).setText("OrderId : " + o.getOrderUUID().split("-")[0].replace("\"", ""));
        }

        if (o.getOrderDate() != null) {
            ((TextView) findViewById(R.id.orderDate)).setText("Date : " + o.getOrderDate());
        }

        if (o.getTotalAmount() != null) {
            ((TextView) findViewById(R.id.orderAmount)).setText("Amount : " + o.getTotalAmount());
        }

        if (o.getOrderDetails() != null) {

            String[] items = new String[1];
            if (o.getOrderDetails().contains("|")) {
                items = o.getOrderDetails().split("|");
            } else {
                items[0] = o.getOrderDetails();
            }
            StringBuffer sb = new StringBuffer();
            for (String item : items) {
                String[] sp = item.split("#");
                Log.e(TAG, "Item " + item + " \tsize" + sp.length);
                sb.append("\n" + sp[0] + " \t\t\t -  " + sp[1]);
            }
            ((TextView) findViewById(R.id.orderDetails)).setText(sb.toString());
        }

    }


    private void listOrders(String orderId) {
        ListOrdersTask task = new ListOrdersTask(getApplicationContext());
        AsyncTask<Void, Void, List<Order>> result = task.execute();
        try {
            List<Order> output = result.get();
            Log.e(TAG, "Total order > " + output.size());
            for (Order o : output) {
                Log.e(TAG, "Order uuid : " + o.getOrderUUID());
                if (o.getOrderUUID().equals(orderId)) {
                    //Call http and break
                    Toast.makeText(QRCodeScanner.this, o.toString(), Toast.LENGTH_LONG).show();
                    break;
                } else continue;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
