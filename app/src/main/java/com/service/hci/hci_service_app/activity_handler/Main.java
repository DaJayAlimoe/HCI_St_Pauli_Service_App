package com.service.hci.hci_service_app.activity_handler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
import com.service.hci.hci_service_app.activity_handler.service.ServiceMain;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Main extends AppCompatActivity implements View.OnClickListener {

    private Button btn_service;
    private Button btn_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // test comment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // findViewById for booth buttons and add a onClickListener object to it
        this.btn_customer = findViewById(R.id.btn_to_customer);
        btn_customer.setOnClickListener(this);
        this.btn_service = findViewById(R.id.btn_to_service);
        btn_service.setOnClickListener(this);

        TextView tv = findViewById(R.id.tv_qrcode);
        tv.setText(this.test());

        if (getIntent().getBooleanExtra("EXIT", false)) {
            Intent intent = new Intent(getApplicationContext(), Main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        // check, if session already available
        if(Session.isSeat()){
            Intent intent = new Intent(Main.this, CustomerMain.class);

            // the items should be saved here.

            startActivity(intent);
            finish();
        }else if(Session.isEmployee()){
            Intent intent = new Intent(Main.this, ServiceMain.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int actView = v.getId();

        if(actView == R.id.btn_to_customer){
            Intent intent = new Intent(Main.this, CustomerMain.class);
            startActivity(intent);
            finish();
        }else if(actView == R.id.btn_to_service){
            Intent intent = new Intent(Main.this, ServiceMain.class);
            startActivity(intent);
            finish();
        }

    }

    public String test() {
        Api stApi = new Api();

        //employee test
        boolean auth = stApi.authenticate(this,"seat:8ea732f038d22e7");

        Log.i("Authenticate- flag: ", Boolean.toString(auth));
        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));


        JSONObject items = stApi.getItems();
        Log.i("items customer: ", items.toString());


        JSONArray orderList = new JSONArray();
        try {
            int userID = Session.getUserId();
            JSONObject j1 = new JSONObject();
            j1.put("amount",5);
            j1.put("item_id",2);
            j1.put("seat_id", userID);
            orderList.put(j1);

            JSONObject j2 = new JSONObject();
            j2.put("amount",5);
            j2.put("item_id",1);
            j2.put("seat_id", userID);
            orderList.put(j2);


            JSONObject j3 = new JSONObject();
            j3.put("amount",5);
            j3.put("item_id",3);
            j3.put("seat_id", userID);
            orderList.put(j3);

            JSONObject j4 = new JSONObject();
            j4.put("amount",5);
            j4.put("item_id",3);
            j4.put("seat_id", userID);
            orderList.put(j4);

            JSONObject j5 = new JSONObject();
            j5.put("amount",5);
            j5.put("item_id",3);
            j5.put("seat_id", userID);
            orderList.put(j5);

            JSONObject j6 = new JSONObject();
            j6.put("amount",5);
            j6.put("item_id",3);
            j6.put("seat_id", userID);
            orderList.put(j6);

            JSONObject j7 = new JSONObject();
            j7.put("amount",5);
            j7.put("item_id",3);
            j7.put("seat_id", userID);
            orderList.put(j7);

            Log.i("orderList: ", orderList.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean placedOrders = stApi.placeOrder(orderList);
        Log.i("placed Orders: ", Boolean.toString(placedOrders));


        JSONObject myOrders = stApi.getMyOrders();
        Log.i("myOrders: ", myOrders.toString());


        // service test
        boolean auth1 = stApi.authenticate(this,"empl:1f9eced18e9d6145");
        Log.i("Authenticate1- flag: ", Boolean.toString(auth1));
        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));

        JSONObject orders = stApi.getOrders();
        Log.i("orders: ", orders.toString());

        JSONObject myOrdersEmpl = stApi.getMyOrders();
        Log.i("myOrders: ", myOrdersEmpl.toString());

        items = stApi.getItems();
        Log.i("items empl: ", items.toString());


        JSONArray orderList1 = new JSONArray();

        try {
            JSONObject order1 = new JSONObject();
            order1.put("booking_id",63);
            JSONObject order2 = new JSONObject();
            order2.put("booking_id",64);

            orderList1.put(order1);
            orderList1.put(order2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("takeOrders empl: ", orderList1.toString());
        JSONObject takenOrders = stApi.takeOrder(orderList1);
        Log.i("takeOrders empl: ", takenOrders.toString());

        return "text complete";
    }
}

