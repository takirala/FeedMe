package smartfoodcluster.feedme.entities;

import com.google.appengine.repackaged.com.google.common.base.Flag;
import com.googlecode.objectify.annotation.Container;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;
import java.util.HashMap;

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

    public String getOrderUUID() {
        return orderUUID;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public HashMap<String, Integer> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(HashMap<String, Integer> orderDetails) {
        this.orderDetails = orderDetails;
    }

    Date orderDate;
    @Container
    HashMap<String, Integer> orderDetails;
}
