package smartfoodcluster.feedme.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.SignInButton;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.handlers.AbstractGetNameTask;
import smartfoodcluster.feedme.handlers.GetNameInForeground;
import smartfoodcluster.feedme.restaurateur.RestaurateurHome;
import smartfoodcluster.feedme.user.UserHome;
import smartfoodcluster.feedme.util.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    Context mContext = LoginActivity.this;
    AccountManager mAccountManager;
    String token;
    int serverCode;
    public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton mEmailSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean authSuccess = true;
                if (Constants.authEnabled) authSuccess = syncGoogleAccount();
                if (authSuccess) {
                    Intent i = new Intent(getApplicationContext(), RestaurateurHome.class);
                    i.putExtra(Constants.showSuccess, true);
                    startActivity(i);
                    setContentView(R.layout.activity_restaurateur_home);
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                    }
                    Toast.makeText(LoginActivity.this, "No google account sync!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private AbstractGetNameTask getTask(LoginActivity activity, String email, String scope) {
        return new GetNameInForeground(activity, email, scope);
    }

    public boolean syncGoogleAccount() {
        if (isNetworkAvailable()) {
            String[] accountarrs = getAccountNames();
            if (accountarrs.length > 0) {
                getTask(LoginActivity.this, accountarrs[0], SCOPE).execute();
                Log.e("Authenticated", "Done");
                return true;
            } else {
                Toast.makeText(LoginActivity.this, "No google account sync!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "No network available!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    boolean isNetworkAvailable() {
        ConnectivityManager cmgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network testing", "##Hurray!! Available");
            Log.e("Network testing", "##Hurray!! Available");
            return true;
        } else {
            Log.e("Network testing", "##Not Available");
            return false;
        }
    }
}

