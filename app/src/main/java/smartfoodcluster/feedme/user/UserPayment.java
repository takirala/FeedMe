package smartfoodcluster.feedme.user;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.json.JSONObject;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.handlers.PaymentQRHandler;
import smartfoodcluster.feedme.qrcode.QRCodeEncoder;
import smartfoodcluster.feedme.qrcode.QRContents;
import smartfoodcluster.feedme.util.Constants;


public class UserPayment extends BaseActivity {

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
        final Bundle extras = getIntent().getExtras();
        String resStr = (String) extras.get(Constants.orderObject);

        try {
            JsonFactory factory = new AndroidJsonFactory();
            JSONObject jsonObject = new JSONObject(resStr.trim());
            final Order r = factory.fromString(jsonObject.toString(), Order.class);
            Log.e(TAG, "" + r);
            PaymentQRHandler locationHandler = (PaymentQRHandler) new PaymentQRHandler(new PaymentQRHandler.AsyncResponse() {
                @Override
                public void processFinish() {
                    Log.e(TAG, "Payment performed");
                    progressBar.setVisibility(View.GONE);
                    progressBarText.setText("Payment successful!!");
                    qrCode.setVisibility(View.VISIBLE);
                    onClick(r.getOrderUUID());
                }
            }).execute();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
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
