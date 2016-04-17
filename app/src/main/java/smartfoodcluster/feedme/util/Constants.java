package smartfoodcluster.feedme.util;

/**
 * Created by Lavenger on 4/14/2016.
 */
public class Constants {

    public static final boolean authEnabled = false;

    public static final String gUserInfo = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String emailId = "email_id";
    public static String showSuccess = "showSuccess";
    public static String userRole = "userRole";
    public static String userAuthenticated = "userAuthenticated";
    public static String requestLocation = "Please enable your location";

    public static final String PREFS_NAME = "MyPrefsFile";

    public static class UserRole {
        public static final boolean USER = true;
        public static final boolean OWNER = false;
    }
}
