package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.OrderListAdapter;
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

public class OrdersFragment extends Fragment {

    private Timer autoUpdateTimer;
    private OrderListAdapter orderListAdapter;
    private final Handler autoUpdateHandler = new Handler();

    public OrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_orders, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView_customer_AllOrdersView); // get the child text view
        ArrayList<Order> itemArrayList = this.getData();
        OrderListAdapter itemListAdapter = new OrderListAdapter(view.getContext(), R.layout.customer_order_list_view, itemArrayList);
        listView.setAdapter(itemListAdapter);

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
                        orderListAdapter.clear();
                        orderListAdapter.addAll(data);
                        orderListAdapter.notifyDataSetChanged();
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
        Api stApi = Api.getInstance(getContext());

        JSONObject myOrders = stApi.getMyOrders();


        if (!myOrders.isNull("bookings")) {
            try {
                JSONArray keys = myOrders.getJSONArray("bookings");

                for (int i = 0; i < keys.length(); ++i) {
                    JSONObject value = keys.getJSONObject(i); // get single entry from array
                    JSONObject itemObj = value.getJSONObject("item"); // items in response
                    Item myItem = new Item(itemObj);
                    Timestamp actTime = Util.parseTimestamp(value.getString("activeAt"));
                    Timestamp createTime = Util.parseTimestamp(value.getString("createdOn"));
                    Timestamp updateTime = Util.parseTimestamp(value.getString("lastUpdatedOn"));

                    Order order = new Order(myItem, value.getInt("amount"), value.getInt("id"), value.getInt("eta"), actTime, createTime, updateTime, Order.OrderStatus.valueOf(value.getString("status")));
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
