package com.service.hci.hci_service_app.activity_handler.service.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_types.Order;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<AllOrdersAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    List<Order> orders;


    public AllOrdersAdapter(Context context,ArrayList<Order> orders) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(R.layout.service_all_orders_adapter_view, parent, false);

        MyViewHolder holder = new MyViewHolder(convertView);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    System.out.println("CLICKED ON!!!!!!!!!!!!!!!!!!!!!: " + position);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Order order = orders.get(position);
        holder.textViewSeat.setText(Integer.toString(order.getSeatNR()));
        holder.textViewCount.setText(Integer.toString(order.getAmount()));
        holder.textViewItemName.setText(order.getItem().getName());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewSeat;
        TextView textViewCount;
        TextView textViewItemName;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewSeat = (TextView)itemView.findViewById(R.id.textView_service_seat);
            textViewCount = (TextView)itemView.findViewById(R.id.textView_service_count);
            textViewItemName = (TextView)itemView.findViewById(R.id.textView_service_item);
        }
    }

}

