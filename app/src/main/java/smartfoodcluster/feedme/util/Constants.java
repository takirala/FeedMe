package smartfoodcluster.feedme.util;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Lavenger on 4/14/2016.
 */
public class Constants {

    public static final boolean authEnabled = false;

    public static final boolean adminMode = false;

    public static final String gUserInfo = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String emailId = "email_id";
    public static String placeId = "placeId";
    public static String vicinity = "vicinity";
    public static String name = "name";
    public static String resObject = "resObject";
    public static String showSuccess = "showSuccess";
    public static String userRole = "userRole";
    public static String userAuthenticated = "userAuthenticated";
    public static String requestLocation = "Please enable your location";
    public static String writeToUs = "Write to us at mcprojectsp16@googlegroups.com";

    public static final String PREFS_NAME = "MyPrefsFile";

    public static final int NEW = 0;
    public static final int PROCESSING = -1;
    public static final int COMPLETED = 1;

    public static class UserRole {
        public static final boolean USER = true;
        public static final boolean OWNER = false;
    }
}
