package smartfoodcluster.feedme.entities;

import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

/**
 * Created by Srinivas on 4/17/2016.
 */
@Entity
public class Order {
    @Id
    Long id;
    String orderUUID;
    int status;
    Float totalAmount;
    String restaurantPlaceId;
    String orderDate;
    String orderDetails;
    // Will be in the format index#count. For example 1#3

    public String getOrderUUID() {
        return orderUUID;
    }

    public String getRestaurantPlaceId() {
        return restaurantPlaceId;
    }

    public void setRestaurantPlaceId(String restaurantPlaceId) {
        this.restaurantPlaceId = restaurantPlaceId;
    }

    public void setOrderUUID(String orderUUID) {
        this.orderUUID = orderUUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }
}
