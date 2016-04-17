package smartfoodcluster.feedme.user;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.myapplicationid.restaurantEndpoint.model.Restaurant;
import com.google.android.gms.drive.Contents;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.handlers.LocationHandler;
import smartfoodcluster.feedme.handlers.PaymentQRHandler;
import smartfoodcluster.feedme.qrcode.QRCodeEncoder;
import smartfoodcluster.feedme.qrcode.QRContents;


public class UserPayment extends AppCompatActivity {

    protected static final String TAG = "UserPayment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarPayment);
        final TextView progressBarText = (TextView) findViewById(R.id.progressBarPaymentText);
        final ImageView qrCode = (ImageView) findViewById(R.id.qrCode);

        Log.e(TAG, "Received background sleeping");

        PaymentQRHandler locationHandler = (PaymentQRHandler) new PaymentQRHandler(new PaymentQRHandler.AsyncResponse() {
            @Override
            public void processFinish(String uuid) {
                Log.e(TAG, "Payment performed");
                progressBar.setVisibility(View.GONE);
                progressBarText.setText("Payment successful!!");
                qrCode.setVisibility(View.VISIBLE);
                onClick(uuid);
            }
        }).execute();

        Log.e(TAG, "After Received background slept");


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

    public void onClick(String qrInputText) {
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
            ImageView myImage = (ImageView) findViewById(R.id.qrCode);
            myImage.setImageBitmap(bitmap);
            myImage.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
