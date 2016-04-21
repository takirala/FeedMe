package smartfoodcluster.feedme.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.g3smartfoodcluster.orderEndpoint.model.Order;

import java.util.List;

import smartfoodcluster.feedme.R;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<Order> listStorage;
    private Context context;

    public CustomAdapter(Context context, List<Order> customizedListView) {
        this.context = context;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.order_list_view, parent, false);
            listViewHolder.orderAmount = (TextView) convertView.findViewById(R.id.orderAmount);
            listViewHolder.orderName = (TextView) convertView.findViewById(R.id.orderId);
            listViewHolder.orderTime = (TextView) convertView.findViewById(R.id.orderDate);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.orderName.setText(listStorage.get(position).getOrderUUID());
        listViewHolder.orderTime.setText(listStorage.get(position).getOrderDate());
        listViewHolder.orderAmount.setText(listStorage.get(position).getTotalAmount().toString());

        return convertView;
    }

    static class ViewHolder {
        TextView orderAmount;
        TextView orderName;
        TextView orderTime;
    }

}

