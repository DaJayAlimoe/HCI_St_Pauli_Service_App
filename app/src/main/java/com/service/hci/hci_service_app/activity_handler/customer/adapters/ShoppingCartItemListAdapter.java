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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_types.Cart;
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.data_types.Item_amount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShoppingCartItemListAdapter extends ArrayAdapter<Item_amount> {

    private Context context;
    private int ressource;
    private int lastPosition = -1;
    private ArrayList<Item_amount> cartList;

    public ShoppingCartItemListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Item_amount> objects) {
        super(context, resource, objects);
        this.context = context;
        this.ressource = resource;
    }

    public void refreshCart(ArrayList<Item_amount> objects) {
        this.cartList.clear();
        this.cartList.addAll(objects);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Item_amount getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Item_amount item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getItem().getName();
        String description = getItem(position).getItem().getDescription();
        int picture = getItem(position).getItem().getPicture();
        int amount = getItem(position).getAmount();

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
            holder.name = (TextView) convertView.findViewById(R.id.textView_customer_name_cart);
            holder.description = (TextView) convertView.findViewById(R.id.textView_customer_description_cart);
            holder.amount = (EditText) convertView.findViewById(R.id.numberView_customer_amount_cart);
            holder.picture = (ImageView) convertView.findViewById(R.id.imgView_customer_picture_cart);

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
        holder.amount.setText(String.valueOf(amount));
        holder.description.setText(item.getDescription());
        holder.picture.setImageResource(item.getPicture());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        EditText amount;
        TextView description;
        ImageView picture;
    }
}
