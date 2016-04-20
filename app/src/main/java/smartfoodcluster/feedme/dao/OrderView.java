package smartfoodcluster.feedme.dao;

public class OrderView {
    Long orderId;
    String orderTime;

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
