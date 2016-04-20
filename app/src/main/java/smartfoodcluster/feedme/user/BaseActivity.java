package smartfoodcluster.feedme.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.auth.LoginActivity;
import smartfoodcluster.feedme.util.Constants;

/**
 * Created by Lavenger on 4/19/2016.
 */
public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "BaseActivity";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e(TAG, "" + id);
        if (id == R.id.nav_orders) {
            // Handle the camera action
        } else if (id == R.id.nav_logout) {
            Log.e(TAG, "Logging out the user ");
            try {
                clearApplicationData();
                clearSharedPreferences(getApplicationContext());
            } catch (Exception e) {
            }
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            setContentView(R.layout.activity_login);
            Toast.makeText(BaseActivity.this, Constants.logout, Toast.LENGTH_LONG);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i(TAG, "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void clearSharedPreferences(Context ctx) {

        String prefFile = ctx.getFilesDir().getParent() + "/shared_prefs/" + Constants.PREFS_NAME;
        String prefFileBak = ctx.getFilesDir().getParent() + "/shared_prefs/" + Constants.PREFS_NAME + ".bak";

        SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
        settings.edit().clear().commit();

        // Make sure it has enough time to save all the commited changes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        try {
            new File(prefFile).delete();
        } catch (Exception e) {
        }
        try {
            new File(prefFileBak).delete();
        } catch (Exception e) {
        }

    }

}
