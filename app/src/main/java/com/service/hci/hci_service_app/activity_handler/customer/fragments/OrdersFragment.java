package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.OrderListAdapter;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.data_types.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrdersFragment extends Fragment{

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

        ListView listView = (ListView) view.findViewById(R.id.customer_AllOrdersView); // get the child text view

        // to test shopping cart
        int userID = Session.getUserId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount",2);
            jsonObject.put("item_id",2);
            jsonObject.put("seat_id",userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("orderInCart",jsonObject.toString());

        Order.addOrder(jsonObject);
        Order.sendOrders();


        // synchronize order data
        ArrayList<Order> itemArrayList = new ArrayList<>();
        Api stApi = new Api();

        JSONObject myOrders = stApi.getMyOrders();

//        Timer timer = new Timer();
//        //Then you extend the timer task
//
//        class SyncData extends TimerTask {
//
//
//            public void run() {
//                //call after every 30min
//            }
//        }
////        And then add the new task to the Timer with some update interval
//
//        TimerTask syncData = new SyncData();
//        timer.scheduleAtFixedRate(syncData,1000*10);//this will run after every 10 sec

        try {

            JSONArray keys = myOrders.getJSONArray("bookings");

            for (int i = 0; i < keys.length (); ++i) {
                JSONObject value = keys.getJSONObject(i); // get single entry from array
                JSONObject itemObj = value.getJSONObject("item"); // items in response
                Item myItem = new Item(itemObj);


                LocalDateTime actTime = LocalDateTime.parse(value.getString("activeAt"));
                LocalDateTime createTime = LocalDateTime.parse(value.getString("createdOn"));
                LocalDateTime updateTime = LocalDateTime.parse(value.getString("lastUpdatedOn"));

                Order order = new Order(myItem, value.getInt("amount"),value.getInt("id"),value.getInt("eta"),actTime,createTime,updateTime, Order.OrderStatus.valueOf(value.getString("status")));
                itemArrayList.add(order);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("itemArrayList",itemArrayList.toString());
        Log.i("MyOrders",myOrders.toString());

        OrderListAdapter itemListAdapter = new OrderListAdapter(view.getContext(), R.layout.customer_order_list_view, itemArrayList);
        listView.setAdapter(itemListAdapter);

        // Inflate the layout for this fragment
        return view;
    }

}
