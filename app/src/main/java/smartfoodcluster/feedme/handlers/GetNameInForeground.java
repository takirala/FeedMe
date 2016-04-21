package smartfoodcluster.feedme.handlers;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import smartfoodcluster.feedme.auth.LoginActivity;

/**
 * Created by Lavenger on 4/14/2016.
 */
public class GetNameInForeground extends AbstractGetNameTask {
    public GetNameInForeground(LoginActivity activity, String email, String scope) {
        super(activity, email, scope);
    }

    @Override
    protected String fetchToken() throws IOException {
        try {
            String token = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException e) {
            mActivity.startActivityForResult(e.getIntent(), mRequestCode);
        } catch (GoogleAuthException e) {
            onError("Unrecoverable error " + e.getMessage(), e);
        }
        return null;
    }
}
