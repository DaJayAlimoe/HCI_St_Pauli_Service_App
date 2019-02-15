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

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;
import com.service.hci.hci_service_app.activity_handler.service.adapters.SelectedOrdersAdapter;
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

public class SelectedOrdersFragment extends Fragment {

    private Timer autoUpdateTimer;
    private SelectedOrdersAdapter selectedOrdersAdapter;
    private final Handler autoUpdateHandler = new Handler();


    public SelectedOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_selected_orders, container, false);

        ListView listView = view.findViewById(R.id.listView_service_selected_orders);

        TextView header1 = view.findViewById(R.id.textView_service_selected_orders_header1);
        TextView header2 = view.findViewById(R.id.textView_service_selected_orders_header2);
        TextView header3 = view.findViewById(R.id.textView_service_selected_orders_header3);

        header1.setText("Sitz-Nr");
        header2.setText("Typ");
        header3.setText("Menge");

        //Dummy Liste
        /*PartialOrder p1 = new PartialOrder(5060, 2, "Bier");
        PartialOrder p2 = new PartialOrder(210, 5, "Limonade");
        PartialOrder p3 = new PartialOrder(378, 7, "Cola");
        PartialOrder p4 = new PartialOrder(3, 10, "Sprite");
        PartialOrder p5 = new PartialOrder(6780, 15, "Bratwurst");
        PartialOrder p6 = new PartialOrder(5500, 4, "Hot Dog");*/


        ArrayList<PartialOrder> partialOrders = new ArrayList<>();
        /*partialOrders.add(p1);
        partialOrders.add(p2);
        partialOrders.add(p3);
        partialOrders.add(p4);
        partialOrders.add(p5);
        partialOrders.add(p6);*/

        Api stApi = Api.getInstance(getContext());

        ArrayList<Order> itemArrayList = this.getData();

        selectedOrdersAdapter = new SelectedOrdersAdapter(this.getContext(),
                R.layout.service_selected_orders_adapter_view, itemArrayList);
        listView.setAdapter(selectedOrdersAdapter);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startTimer(30000, 30000);
    }

    public void startTimer(int delay, int period) {
        if (autoUpdateTimer != null)
            autoUpdateTimer.cancel();
        autoUpdateTimer = new Timer();
        autoUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoUpdateHandler.post(new Runnable() {
                    public void run() {
                        ArrayList<Order> data = getData();
                        selectedOrdersAdapter.clear();
                        selectedOrdersAdapter.addAll(data);
                        selectedOrdersAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, delay, period); // updates each 30 secs
    }

    @Override
    public void onPause() {
        if (autoUpdateTimer != null)
            autoUpdateTimer.cancel();
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        startTimer(0, 30000);
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
                    JSONObject seat = value.getJSONObject("seat");
                    int booking = (int) value.get("id");
                    int amount = value.getInt("amount");
                    Item myItem = new Item(itemObj);
                    Timestamp actTime = Util.parseTimestamp(value.getString("activeAt"));
                    Timestamp createTime = Util.parseTimestamp(value.getString("createdOn"));
                    Timestamp updateTime = Util.parseTimestamp(value.getString("lastUpdatedOn"));

//                    Order order = new Order(seat.getInt("seatNr"),myItem,amount,booking);
//                    itemArrayList.add(order);
//                    Log.i("SELECTED Order " + i, order.toString());
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