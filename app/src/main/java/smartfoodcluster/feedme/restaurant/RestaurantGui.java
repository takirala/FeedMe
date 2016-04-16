package smartfoodcluster.feedme.restaurant;

/**
 * Created by hemanth.pinaka on 4/6/2016.
 */
public class RestaurantGui {
    String restaurantName=new String();
    int restaurantIconId;

    public RestaurantGui(String restaurantName, int restaurantIconId) {
        this.restaurantName = restaurantName;
        this.restaurantIconId = restaurantIconId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getRestaurantIconId() {
        return restaurantIconId;
    }

    public void setRestaurantIconId(int restaurantIconId) {
        this.restaurantIconId = restaurantIconId;
    }
}
