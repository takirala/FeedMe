package smartfoodcluster.feedme.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lavenger on 4/20/2016.
 */
public class Test {
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat();
        String now = sdf.format(new Date()).toString();
        System.out.println(now);
    }
}
