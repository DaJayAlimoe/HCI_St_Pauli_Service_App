package com.service.hci.hci_service_app.activity_handler.service.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.service.hci.hci_service_app.activity_handler.customer.adapters.OrderListAdapter;
import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;
import com.service.hci.hci_service_app.activity_handler.service.adapters.PartialOrdersAdapter;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.data_types.Order;
import com.service.hci.hci_service_app.data_types.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AllOrdersFragment extends Fragment {

    private Timer autoUpdateTimer;
    private OrderListAdapter orderListAdapter;
    private final Handler autoUpdateHandler = new Handler();

    public AllOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_orders, container, false);

        ListView listView = view.findViewById(R.id.listView_service_all_orders);

        TextView header1 = view.findViewById(R.id.textView_service_orders_header1);
        TextView header2 = view.findViewById(R.id.textView_service_orders_header2);
        TextView header3 = view.findViewById(R.id.textView_service_orders_header3);
        TextView header4 = view.findViewById(R.id.textView_service_orders_header4);

        header1.setText("Sitz-Nr");
        header2.setText("Typ");
        header3.setText("Menge");
        header4.setText("Annehmen");

        //Dummy Liste
      //  PartialOrder p1 = new PartialOrder(5060, 2, "Bier");
        //PartialOrder p2 = new PartialOrder(210, 5, "Limonade");
        //PartialOrder p3 = new PartialOrder(378, 7, "Cola");
        //PartialOrder p4 = new PartialOrder(3, 10, "Sprite");
        //PartialOrder p5 = new PartialOrder(6780, 15, "Bratwurst");
        //PartialOrder p6 = new PartialOrder(5500, 4, "Hot Dog");
        //PartialOrder p7 = new PartialOrder(444, 1, "Brezel");
        //PartialOrder p8 = new PartialOrder(1234, 20, "Bier");
        // PartialOrder p9 = new PartialOrder(567, 8, "Bratwurst");
        //PartialOrder p10 = new PartialOrder(35, 9, "Cola");
        //PartialOrder p11 = new PartialOrder(68, 1, "Fanta");

        ArrayList<PartialOrder> partialOrders = new ArrayList<>();
        //  partialOrders.add(p1);
        //partialOrders.add(p2);
        //partialOrders.add(p3);
        //partialOrders.add(p4);
        //partialOrders.add(p5);
        //partialOrders.add(p6);
        //partialOrders.add(p7);
        //partialOrders.add(p8);
        //partialOrders.add(p9);
        //partialOrders.add(p10);
        //partialOrders.add(p11);
        //partialOrders.add(p4);
        //partialOrders.add(p6);
        //partialOrders.add(p2);
        //partialOrders.add(p5);
        //partialOrders.add(p5);
        //partialOrders.add(p5);
        //partialOrders.add(p5);
        Api stApi = Api.getInstance();



        ArrayList<Order> itemArrayList = this.getData();
        //OrderListAdapter itemListAdapter = new OrderListAdapter(view.getContext(), R.layout.customer_order_list_view, itemArrayList);
        //listView.setAdapter(itemListAdapter);

        PartialOrdersAdapter adapter = new PartialOrdersAdapter(this.getContext(),R.layout.service_all_orders_adapter_view, itemArrayList);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoUpdateTimer = new Timer();
        autoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoUpdateHandler.post(new Runnable() {
                    public void run() {
                        ArrayList<Order> data = getData();
                        orderListAdapter.refreshData(data);
                    }
                });
            }
        }, 30000, 30000); // updates each 30 secs
    }

    @Override
    public void onPause() {
        if (autoUpdateTimer != null)
            autoUpdateTimer.cancel();
        super.onPause();
    }

    private ArrayList<Order> getData() {
        ArrayList<Order> itemArrayList = new ArrayList<>();
        Api stApi = Api.getInstance();

        JSONObject myOrders = stApi.getOrders();

        if (!myOrders.isNull("bookings")) {
            try {
                JSONArray keys = myOrders.getJSONArray("bookings");

                for (int i = 0; i < keys.length(); ++i) {
                    JSONObject value = keys.getJSONObject(i); // get single entry from array
                    JSONObject itemObj = value.getJSONObject("item"); // items in response
                    JSONObject seat = value.getJSONObject("seat");
                    int amount = value.getInt("amount");                    Item myItem = new Item(itemObj);
                    Timestamp actTime = Util.parseTimestamp(value.getString("activeAt"));
                    Timestamp createTime = Util.parseTimestamp(value.getString("createdOn"));
                    Timestamp updateTime = Util.parseTimestamp(value.getString("lastUpdatedOn"));

                    Order order = new Order(seat.getInt("seatNr"),myItem,amount);
                    itemArrayList.add(order);
                    Log.i("Order " + i, order.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("itemArrayList", itemArrayList.toString());
        Log.i("MyOrders", myOrders.toString());

        return itemArrayList;
    }

}