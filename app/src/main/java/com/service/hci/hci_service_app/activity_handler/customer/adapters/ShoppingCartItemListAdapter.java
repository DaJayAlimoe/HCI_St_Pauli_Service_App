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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
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
            holder.btnPlus = (Button) convertView.findViewById(R.id.btn_customer_plus_cart);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_customer_delete_cartItem);
            holder.btnMinus = (Button) convertView.findViewById(R.id.btn_customer_minus_cart);
            CounterFab counterFab = CustomerMain.getFloatingActionButton();
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getItem(position).getAmount() < 5) {
                        getItem(position).setAmount(getItem(position).getAmount() + 1);
                        holder.amount.setText(String.valueOf(getItem(position).getAmount()));

                        counterFab.increase();
                        notifyDataSetChanged();
                    }
                    else {
                        holder.amount.setText(String.valueOf(getItem(position).getAmount()));
                        Toast.makeText(getContext(), "Nur maximal 5 pro Sorte möglich!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getItem(position).getAmount() > 1) {
                        getItem(position).setAmount(getItem(position).getAmount() - 1);
                        holder.amount.setText(String.valueOf(getItem(position).getAmount()));
                        counterFab.decrease();
                        notifyDataSetChanged();
                    }
                    else
                        holder.amount.setText(String.valueOf(getItem(position).getAmount()));
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    counterFab.setCount(counterFab.getCount()-getItem(position).getAmount());
                    remove(getItem(position));
                    Toast.makeText(getContext(),name + " aus dem Warenkorb entfernt " ,Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });

            convertView.setTag(holder);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        // sieht unschön aus bei notifyDataSetChanged()
//        Animation animation = AnimationUtils.loadAnimation(
//                context, (position > lastPosition) ? R.anim.anim_down_loader : R.anim.anim_up_loader);
//
//        result.startAnimation(animation);
//        lastPosition = position;


        holder.name.setText(name);
        holder.amount.setText(String.valueOf(getItem(position).getAmount()));
        holder.description.setText(description);
        holder.picture.setImageResource(picture);

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        EditText amount;
        TextView description;
        ImageView picture;
        Button btnMinus;
        Button btnDelete;
        Button btnPlus;
    }
}
