package com.service.hci.hci_service_app.activity_handler.customer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.fragments.OrdersFragment;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_types.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        // create item object with information
        Order order = getItem(position);

        // create the view result for showing the aniomation
        final View result;

        // viewHolder object
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(ressource, parent, false);

            // to show animation
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.textView_customer_item_description_order);
            holder.amount = (TextView) convertView.findViewById(R.id.textView_customer_amount_order);
            holder.picture = (ImageView) convertView.findViewById(R.id.imgView_customer_picture_order);
            holder.statusButton = convertView.findViewById(R.id.btn_customer_status_order);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(
//                context, (position > lastPosition) ? R.anim.anim_down_loader : R.anim.anim_up_loader);
//
//        result.startAnimation(animation);
//        lastPosition = position;

        holder.amount.setText(order.getAmount() + "x " + order.getItem().getName());
        holder.description.setText("Wartezeit : " + order.getEta() / 60000 + " min");
//        holder.description.setText(order.getItem().getDescription());
        holder.picture.setImageResource(order.getItem().getPicture());

        if (order.getActiveAt().after(new Timestamp(System.currentTimeMillis())) && order.getStatus() != Order.OrderStatus.CANCELED) {
            // to cancel the order
            holder.statusButton.setText("Stornieren");
            holder.statusButton.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.statusButton.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_st_pauli));
            holder.statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Bestellung wirklich stornieren?");
                    alertDialog.setMessage(String.valueOf(getItem(position).getAmount()) + "x " + String.valueOf(getItem(position).getItem().getName()));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Stornieren",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Api stApi = Api.getInstance(getContext());
                                    boolean cancleStatus = stApi.cancelOrder(getItem(position).getOrderNR());
                                    if (cancleStatus) {
                                        Log.i("cancelStatus", Boolean.toString(cancleStatus));
                                        getItem(position).setStatus(Order.OrderStatus.CANCELED);
                                        notifyDataSetChanged();
                                        Toast.makeText(v.getContext(), "Bestellung erfolgreich storniert", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(v.getContext(), "Bestellung bereits in arbeit", Toast.LENGTH_LONG).show();
                                        Log.i("cancelStatus", Boolean.toString(cancleStatus));
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Doch nicht stornieren",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackground(ContextCompat.getDrawable(context, R.drawable.btn_st_pauli));
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackground(ContextCompat.getDrawable(context, R.drawable.btn_st_pauli));
                        }
                    });
                    alertDialog.show();
                }
            });
        } else {
            SpannableString spanString = new SpannableString(order.getButtonText());
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
            holder.statusButton.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.statusButton.setText(spanString);
            holder.statusButton.setBackgroundColor(Color.TRANSPARENT);
            holder.statusButton.setClickable(false);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView amount;
        TextView description;
        ImageView picture;
        Button statusButton;
    }

}
