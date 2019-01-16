package com.service.hci.hci_service_app.activity_handler.customer.adapters;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_types.*;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private Context context;
    private int ressource;
    private int lastPosition = -1;

    public OrderListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Order getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Order order) {
        return super.getPosition(order);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String orderNR = getItem(position).getOrderNR();
        Item description = getItem(position).getItem();
        Integer orderStatus = getItem(position).getAmount();

        // create item object with information
        Order order = new Order(orderNR,description,orderStatus);

        // create the view result for showing the aniomation
        final View result;

        // viewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(ressource, parent, false);

            // to show animation
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.textView_Item_Description);
            holder.amount = (TextView) convertView.findViewById(R.id.textView_Amount);
            holder.picture = (ImageView) convertView.findViewById(R.id.imgView_picture);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(
                context, (position > lastPosition) ? R.anim.anim_down_loader : R.anim.anim_up_loader);

        result.startAnimation(animation);
        lastPosition = position;

        holder.amount.setText(order.getAmount().toString());
        holder.description.setText("ONR : "+" "+ order.getOrderNR().toString()+" "+order.getItem().getDescription().toString());
        holder.picture.setImageResource(order.getItem().getPicture());

        return convertView;
    }

    static class ViewHolder {
        TextView amount;
        TextView description;
        ImageView picture;
    }

}
