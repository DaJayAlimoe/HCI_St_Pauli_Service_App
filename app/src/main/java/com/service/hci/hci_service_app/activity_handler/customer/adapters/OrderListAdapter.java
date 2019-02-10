package com.service.hci.hci_service_app.activity_handler.customer.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.Main;
import com.service.hci.hci_service_app.data_types.*;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends ArrayAdapter<Order> implements View.OnClickListener {
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
        // variables to show
        int orderNR = getItem(position).getOrderNR();
        Item item = getItem(position).getItem();
        int amount = getItem(position).getAmount();
        int eta = getItem(position).getEta();
        Order.OrderStatus status = getItem(position).getStatus();

        // create item object with information
//        Order(Item item, int amount, int orderNR, int eta, OrderStatus status) {
        Order order = new Order(item, amount, orderNR, eta, status);

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
            holder.imageButton = convertView.findViewById(R.id.btn_status);

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

        holder.amount.setText(order.getItem().getDescription().toString());
        holder.description.setText("Best.Nr: "+ order.getOrderNR()+" Menge: "+order.getAmount());
        holder.picture.setImageResource(order.getItem().getPicture());

        if(order.getStatus().getStatus() == 0 && (order.getEta() > 0)){
            // to cancel the order
            holder.imageButton.setImageResource(android.R.drawable.btn_dialog);
            holder.imageButton.setBackgroundColor(Color.GREEN);
            holder.imageButton.setOnClickListener(this);
        }
        else if(order.getStatus().getStatus() == 1){
            holder.imageButton.setImageResource(android.R.drawable.ic_menu_set_as);
            holder.imageButton.setClickable(false);
        }
        else if (order.getStatus().getStatus() == 3){
            holder.imageButton.setImageResource(android.R.drawable.checkbox_on_background);
            holder.imageButton.setClickable(false);
        }else{
            holder.imageButton.setImageResource(android.R.drawable.btn_dialog);
            holder.imageButton.setClickable(false);
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int actView = v.getId();

        if (actView == R.id.btn_status) {
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
            alertDialog.setTitle("Bestellung wirklich abbrechen?");
            alertDialog.setMessage("blabla");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "bestätigen",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Abbrechen",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    static class ViewHolder {
        TextView amount;
        TextView description;
        ImageView picture;
        ImageButton imageButton;
    }

}
