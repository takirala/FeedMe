package smartfoodcluster.feedme.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import smartfoodcluster.feedme.R;
import smartfoodcluster.feedme.dao.RestaurantGui;
import smartfoodcluster.feedme.dao.ShoppingCartDao;

public class ShoppingCart extends AppCompatActivity {
    HashMap<String, Integer> orderedItemsMap = new HashMap<String, Integer>();
    ArrayList<ShoppingCartDao> finalOrderListArray = new ArrayList<ShoppingCartDao>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            calculateSum(extras);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }


    public void calculateSum(Bundle extras) {
        if (extras != null) {
            orderedItemsMap = (HashMap<String, Integer>) extras.getSerializable("orderedItemMap");
            ListView orderSummaryListView = (ListView) findViewById(R.id.itemsOrderedListGui);
            finalOrderListArray = new ArrayList<ShoppingCartDao>();
            Integer totalBill = 0;

            for (String menuItem : orderedItemsMap.keySet()) {
                totalBill += orderedItemsMap.get(menuItem) * 45;
                finalOrderListArray.add(new ShoppingCartDao(menuItem, new Integer(45), orderedItemsMap.get(menuItem)));
            }
            ArrayAdapter<ShoppingCartDao> adapter = new ShoppingCartAdapter();
            orderSummaryListView.setAdapter(adapter);
            ((TextView) findViewById(R.id.totalAmountGui)).setText(totalBill.toString());

        }

    }

    private class ShoppingCartAdapter extends ArrayAdapter<ShoppingCartDao> {

        public ShoppingCartAdapter() {
            super(ShoppingCart.this, R.layout.shopping_cart_list_view, finalOrderListArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View thisView=convertView;
            if(thisView==null){
                thisView= getLayoutInflater().inflate(R.layout.shopping_cart_list_view,parent,false);
            }

            ShoppingCartDao itemOnFocus = finalOrderListArray.get(position);
            TextView restaurantNameGui = (TextView)thisView.findViewById(R.id.menuItemGui);
            restaurantNameGui.setText(itemOnFocus.getMenuItem());
            TextView countGui = (TextView)thisView.findViewById(R.id.itemCountGui);
            countGui.setText(itemOnFocus.getCountForItem().toString());
            TextView amountPerItem =(TextView)thisView.findViewById(R.id.totalPerItemGui);
            amountPerItem.setText(new Integer(itemOnFocus.getCountForItem()*itemOnFocus.getCostForItem()).toString());

            return thisView;
        }


    }

}


