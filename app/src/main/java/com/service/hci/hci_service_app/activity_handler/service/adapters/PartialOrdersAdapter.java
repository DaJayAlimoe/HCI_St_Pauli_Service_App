package com.service.hci.hci_service_app.activity_handler.service.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;
import com.service.hci.hci_service_app.R;

import java.util.ArrayList;

public class PartialOrdersAdapter extends ArrayAdapter<PartialOrder> {

    private static final String TAG = "PartialOrdersAdapter";

    private Context context;

    int resource;

    public PartialOrdersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PartialOrder> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int seat = getItem(position).getSeat();
        int count = getItem(position).getCount();
        String itemName = getItem(position).getItemName();

        PartialOrder partialOrder = new PartialOrder(seat, count, itemName);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textViewSeat = convertView.findViewById(R.id.textView_service_seat);
        TextView textViewCount = convertView.findViewById(R.id.textView_service_count);
        TextView textViewItemName = convertView.findViewById(R.id.textView_service_item);

        textViewSeat.setText(String.valueOf(seat));
        textViewCount.setText(String.valueOf(count));
        textViewItemName.setText(itemName);


        return convertView;

    }
}

