package smartfoodcluster.feedme.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;

import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.handlers.LocationHandler;
import smartfoodcluster.feedme.handlers.PaymentQRHandler;


public class UserPayment extends AppCompatActivity {

    protected static final String TAG = "UserPayment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarPayment);

        PaymentQRHandler locationHandler = (PaymentQRHandler) new PaymentQRHandler(new PaymentQRHandler.AsyncResponse() {
            @Override
            public void processFinish() {
                Log.e(TAG, "Payment performed");


            }
        }).execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
