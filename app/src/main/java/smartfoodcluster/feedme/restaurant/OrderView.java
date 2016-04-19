package smartfoodcluster.feedme.restaurant;

/**
 * Created by Srinivas on 4/18/2016.
 */
public class OrderView {

    Long orderId;
    String orderTime = new String();

    public OrderView(Long orderId, String orderTime) {
        this.orderId = orderId;
        this.orderTime = orderTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
