package smartfoodcluster.feedme.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.util.Constants;

public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Intent intent = getIntent();
        if (intent.getExtras().get(Constants.showSuccess) != null) {
            Toast.makeText(UserHome.this, "Authenticated!!", Toast.LENGTH_SHORT).show();
            intent.removeExtra(Constants.showSuccess);
        }
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        String[] restaurantList = {"BoneFish", "McDonalds", "Chipotle", "Publix", "I'm Tasty! Eat Here!"};
        ListAdapter restaurantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantList);
        ListView restaurantListGui = (ListView) findViewById(R.id.restaurantList);
        restaurantListGui.setAdapter(restaurantAdapter);

        restaurantListGui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRestaurant = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(UserHome.this, selectedRestaurant, Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), UserViewMenu.class);
                i.putExtra("restarantName", selectedRestaurant);
                startActivity(i);
                setContentView(R.layout.activity_user_view_menu);
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/


    }

}
