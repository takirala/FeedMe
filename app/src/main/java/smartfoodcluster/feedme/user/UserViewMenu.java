package smartfoodcluster.feedme.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import smartfoodcluster.feedme.R;

public class UserViewMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_menu);


        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String restaurantName = extras.getString("restarantName");
            ((TextView)findViewById(R.id.restaurantNameTextBox)).setText(restaurantName);

            String[] menu = {restaurantName+" Sushi 65",restaurantName+" Tandoori Tandeloin",restaurantName+" Eat and Die",restaurantName+" Wings",
                    restaurantName+" Dosa",restaurantName+" Idli"};
            ListAdapter menuAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);
            ((ListView) findViewById(R.id.menuListGui)).setAdapter(menuAdapter);
        }

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
