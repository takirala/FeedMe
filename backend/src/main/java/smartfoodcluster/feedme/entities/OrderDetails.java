package smartfoodcluster.feedme.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Srinivas on 4/17/2016.
 */
@Entity
public class OrderDetails {
    @Id
    Long orderDetailsId;
    Long itemId;
    Integer itemQty;
    Float itemTotal;
    Long orderId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Integer getItemQty() {
        return itemQty;
    }

    public void setItemQty(Integer itemQty) {
        this.itemQty = itemQty;
    }

    public Float getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Float itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
