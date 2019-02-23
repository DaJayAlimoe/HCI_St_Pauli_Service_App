package com.service.hci.hci_service_app.activity_handler.service.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;
import com.service.hci.hci_service_app.activity_handler.service.ServiceMain;
import com.service.hci.hci_service_app.activity_handler.service.adapters.AllOrdersAdapter;
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
    private AllOrdersAdapter allOrdersAdapter;
    private final Handler autoUpdateHandler = new Handler();
    private ServiceMain serviceMain;

    public AllOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        serviceMain = (ServiceMain) getActivity();

        View view = inflater.inflate(R.layout.service_orders, container, false);

        ListView listView = view.findViewById(R.id.listView_service_all_orders);

        TextView header1 = view.findViewById(R.id.textView_service_orders_header1);
        TextView header2 = view.findViewById(R.id.textView_service_orders_header2);
        TextView header3 = view.findViewById(R.id.textView_service_orders_header3);

        header1.setText("Sitz-Nr");
        header2.setText("Typ");
        header3.setText("Menge");

        ArrayList<PartialOrder> partialOrders = new ArrayList<>();

        Api stApi = Api.getInstance(getContext());

        ArrayList<Order> itemArrayList = this.getData();


        allOrdersAdapter = new AllOrdersAdapter(this.getContext(),R.layout.service_all_orders_adapter_view, itemArrayList);
        listView.setAdapter(allOrdersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("Bestellung übernehmen");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Bestellung übernehmen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (allOrdersAdapter.getItem(i).getStatus() != Order.OrderStatus.CANCELED||allOrdersAdapter.getItem(i).getStatus() != Order.OrderStatus.ONTHEWAY ) {
                                    try {
                                        JSONObject booking = new JSONObject();
                                        booking.put("booking_id", allOrdersAdapter.getItem(i).getOrderNR());
                                        JSONArray jsonArray = new JSONArray();
                                        jsonArray.put(booking);
                                        stApi.takeOrder(jsonArray);
                                        SelectedOrdersFragment selectedOrdersFragment =(SelectedOrdersFragment)serviceMain.adapter.getItem(1);

                                        if(selectedOrdersFragment.selectedOrdersAdapter != null) {
                                            selectedOrdersFragment.selectedOrdersAdapter.add(allOrdersAdapter.getItem(i));
                                        }

                                        allOrdersAdapter.remove(allOrdersAdapter.getItem(i));
                                        allOrdersAdapter.notifyDataSetChanged();


                                        Toast.makeText(view.getContext(), "Bestellung erfolgreich übernommen", Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } finally {
                                        dialog.dismiss();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(view.getContext(), "Bestellung nicht mehr Verfügbar", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

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
        });

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startTimer(20000, 20000);
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
                        allOrdersAdapter.clear();
                        allOrdersAdapter.addAll(data);
                        allOrdersAdapter.notifyDataSetChanged();
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

        JSONObject myOrders = stApi.getOrders();

        if (!myOrders.isNull("bookings")) {
            try {
                JSONArray keys = myOrders.getJSONArray("bookings");

                for (int i = 0; i < keys.length(); ++i) {
                    JSONObject value = keys.getJSONObject(i); // get single entry from array
                    JSONObject itemObj = value.getJSONObject("item"); // items in response
                    JSONObject seat = value.getJSONObject("seat");
                    int orderNr = value.getInt("id");
                    int amount = value.getInt("amount");
                    Item myItem = new Item(itemObj);
                    Timestamp actTime = Util.parseTimestamp(value.getString("activeAt"));
                    Timestamp createTime = Util.parseTimestamp(value.getString("createdOn"));
                    Timestamp updateTime = Util.parseTimestamp(value.getString("lastUpdatedOn"));
                    String status = value.getString("status");
                    Order order = new Order(seat.getInt("seatNr"),myItem,amount,orderNr,Order.OrderStatus.valueOf(status));
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