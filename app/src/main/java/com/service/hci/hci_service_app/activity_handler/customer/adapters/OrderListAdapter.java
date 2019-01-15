package com.service.hci.hci_service_app.activity_handler.customer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_types.Item;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends ArrayAdapter<Item> {
    private Context context;
    private int ressource;
    private int lastPosition = -1;

    public OrderListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Item> objects) {
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
    public Item getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Item item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String orderNR = getItem(position).getName();
        String description = getItem(position).getDescription();
        String orderStatus = getItem(position).getPicture();

        // create item object with information
        Item item = new Item(description, name, picture);

        // create the view result for showing the aniomation
        final View result;

        // viewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(ressource, parent, false);

            // to show animation
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.description = (TextView) convertView.findViewById(R.id.textView_description);
            holder.picture = (TextView) convertView.findViewById(R.id.textView_picture);

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

        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.picture.setText(item.getPicture());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView description;
        TextView picture;
    }

}
