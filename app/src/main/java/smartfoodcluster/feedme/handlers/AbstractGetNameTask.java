package smartfoodcluster.feedme.handlers;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import smartfoodcluster.feedme.auth.LoginActivity;
import smartfoodcluster.feedme.user.UserSelection;
import smartfoodcluster.feedme.util.Constants;

/**
 * Created by Lavenger on 4/14/2016.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TokenInfoTask";
    protected LoginActivity mActivity;
    public static String GOOGLE_USER_DATA = "No_data";
    protected String mScope;
    protected String mEmail;
    protected int mRequestCode;

    AbstractGetNameTask(LoginActivity activity, String email, String scope) {
        this.mActivity = activity;
        this.mEmail = email;
        this.mScope = scope;
    }


    @Override
    protected Void doInBackground(Void... params) {
        try {
            fetchNameFromProfileServer();
        } catch (Exception ex) {
            onError(ex.getMessage(), ex);
        }
        return null;

    }

    /**
     * Get a authentication token if one is not available. If the error is not
     * recoverable then it displays the error message on parent activity.
     */

    protected abstract String fetchToken() throws IOException;

    private void fetchNameFromProfileServer() throws IOException, JSONException {
        String token = fetchToken();
        URL url = new URL(Constants.gUserInfo + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if (sc == HttpURLConnection.HTTP_OK) {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();
            Intent intent = new Intent(mActivity, UserSelection.class);
            intent.putExtra(Constants.emailId, mEmail);
            mActivity.startActivity(intent);
            mActivity.finish();
        } else if (sc == HttpURLConnection.HTTP_UNAUTHORIZED) {
            GoogleAuthUtil.invalidateToken(mActivity, token);
            onError("Server auth error, please try again.", null);
        } else onError("Server returned the following error code: " + sc, null);
        return;
    }

    private static String readResponse(InputStream is)throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0 , len);
        }
        return new String(bos.toByteArray(), "UTF-8");

    }

    protected void onError(String msg, Exception e) {
        if (e != null) {
            Log.e(TAG, "Ex : ", e);
        }
    }

}
