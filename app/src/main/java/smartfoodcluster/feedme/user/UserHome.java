package smartfoodcluster.feedme.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.restaurant.RestaurantGui;

public class UserHome extends AppCompatActivity {
    List<RestaurantGui> restaurants=new ArrayList<RestaurantGui>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        List restaurantList  = populateList();

        //ListAdapter restaurantAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,restaurantList);
        ListAdapter restaurantAdapter = new RestaurantAdapter();
        ListView restaurantListGui = (ListView)findViewById(R.id.restaurantList);
        restaurantListGui.setAdapter(restaurantAdapter);

        restaurantListGui.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestaurantGui selectedRestaurantGui = restaurants.get(position);
                String selectedRestaurant = selectedRestaurantGui.getRestaurantName();
                Toast.makeText(UserHome.this, selectedRestaurant, Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), UserViewMenu.class);
                i.putExtra("restarantName", selectedRestaurant);
                i.putExtra("RestaurantIcon",selectedRestaurantGui.getRestaurantIconId());
                startActivity(i);
                setContentView(R.layout.activity_restaurant_home);
            }
        });
    }

    private List<RestaurantGui> populateList(){


        restaurants.add(new RestaurantGui("BoneFish", R.drawable.bonefish));
        restaurants.add(new RestaurantGui("BigBurger", R.drawable.bigburger));
        restaurants.add(new RestaurantGui("Chipotle", R.drawable.chipotle));
        restaurants.add(new RestaurantGui("McDonalds",R.drawable.mcdonalds));
        restaurants.add(new RestaurantGui("Publix", R.drawable.publix));
        restaurants.add(new RestaurantGui("Subway", R.drawable.subway));
        return restaurants;
    }

    private class RestaurantAdapter extends ArrayAdapter<RestaurantGui>{

        public RestaurantAdapter(){
            super(UserHome.this,R.layout.restaurant_list_view,restaurants);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            View thisView = convertView;
            if(thisView==null){
                thisView=getLayoutInflater().inflate(R.layout.restaurant_list_view,parent,false);
            }

            RestaurantGui selectedRestaurant = restaurants.get(position);

            ImageView restaurantImageView = (ImageView)thisView.findViewById(R.id.restaurantIcon);
            restaurantImageView.setImageResource(selectedRestaurant.getRestaurantIconId());

            TextView restaurantNameTextView=(TextView)thisView.findViewById(R.id.restaurantName);
            restaurantNameTextView.setText(selectedRestaurant.getRestaurantName());

            return thisView;
        }

    }

}