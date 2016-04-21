package smartfoodcluster.feedme.dao;

/**
 * Created by hemanth.pinaka on 4/16/2016.
 */
public class ShoppingCartDao {
    String menuItem;
    Integer costForItem;
    Integer countForItem;

    public ShoppingCartDao(String menuItem, Integer costForItem, Integer countForItem) {
        this.menuItem = menuItem;
        this.costForItem = costForItem;
        this.countForItem = countForItem;
    }

    public Integer getCostForItem() {
        return costForItem;
    }

    public void setCostForItem(Integer costForItem) {
        this.costForItem = costForItem;
    }


    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getCountForItem() {
        return countForItem;
    }

    public void setCountForItem(Integer countForItem) {
        this.countForItem = countForItem;
    }


}
