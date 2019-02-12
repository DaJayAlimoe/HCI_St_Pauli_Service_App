package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.os.Handler;
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
import com.service.hci.hci_service_app.data_types.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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
//
//        Timer timer;
//        TimerTask timerTask;
//        final Handler handler = new Handler();
//
//
//        public void startTimer(int delay, int interval) {
//            timer = new Timer();
//            //initialize the TimerTask's job
//            initializeTimerTask();
//            //schedule the timer, after the first delay ms the TimerTask will run every interval ms
//            timer.schedule(timerTask, delay, interval); //
//        }
//
//        public void stoptimertask() {
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//        }
//
//        public void initializeTimerTask() {
//
//            timerTask = new TimerTask() {
//                public void run() {
//                    handler.post(new Runnable() {
//                        public void run() {
//
//                        }
//                    });
//                }
//            };
//        }

        try {

            JSONArray keys = myOrders.getJSONArray("bookings");

            for (int i = 0; i < keys.length (); ++i) {
                JSONObject value = keys.getJSONObject(i); // get single entry from array
                JSONObject itemObj = value.getJSONObject("item"); // items in response
                Item myItem = new Item(itemObj);
                Timestamp actTime = null;
                Timestamp createTime = null;
                Timestamp updateTime = null;
                try {
                    actTime = Util.parseTimestamp(value.getString("activeAt"));
                    createTime = Util.parseTimestamp(value.getString("createdOn"));
                    updateTime = Util.parseTimestamp(value.getString("lastUpdatedOn"));
                } catch (ParseException e) {
                    Log.i("parsing DateTime in OrdersFragment", e.getStackTrace().toString());
                }

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
