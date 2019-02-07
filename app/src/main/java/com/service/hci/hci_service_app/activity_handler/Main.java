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

        boolean auth = stApi.authenticate(this,"seat:6ed63a5c91810f0c");

        Log.i("Authenticate- flag: ", Boolean.toString(auth));
        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));

//        Log.i("Authenticate1- flag: ", Boolean.toString(auth1));
//        Log.i("isSeat: ", Boolean.toString(Session.isSeat()));
//        Log.i("isEmployee: ", Boolean.toString(Session.isEmployee()));

        JSONObject items = stApi.getItems();
        Log.i("items: ", items.toString());



//        // service test
//        boolean auth1 = stApi.authenticate(this,"empl:1fe8598f0430f512");
//
//        JSONObject myOrders = stApi.getMyOrders();
//        Log.i("myOrders: ", myOrders.toString());
//
//        JSONObject orders = stApi.getOrders();
//        Log.i("orders: ", orders.toString());

        JSONArray orderList = new JSONArray();
        try {
            int userID = Session.getUserId();
            JSONObject j1 = new JSONObject();
            j1.put("amount",5);
            j1.put("item_id",2);
            j1.put("seat_id", userID);
            JSONObject j2 = new JSONObject();
            j1.put("amount",5);
            j1.put("item_id",1);
            j1.put("seat_id", userID);
            JSONObject j3 = new JSONObject();
            j1.put("amount",5);
            j1.put("item_id",3);
            j1.put("seat_id", userID);
            orderList.put(j1);
            orderList.put(j2);
            orderList.put(j3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean placedOrders = stApi.placeOrder(orderList);
        Log.i("placed Orders: ", Boolean.toString(placedOrders));

//        items = null;
//        orders = null;
//        myOrders = null;
//        orderList = null;
//
//        // User : Employee
//        stApi.authenticate("empl:62dd274c2cd77eac");
//
//        items = stApi.getItems();
//        System.out.println(items.toString());
//
//        orders = stApi.getOrders();
//        System.out.println(orders.toString());
//
//        myOrders = stApi.getMyOrders();
//        System.out.println(myOrders);
//
//        orderList = null;
//        try {
//            orderList = new JSONObject("{" +
//                    "{\"booking_id\": 61}," +
//                    "{\"booking_id\": 62}," +
//                    "{\"booking_id\": 63}," +
//                    "}");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject takenOrders = stApi.takeOrder(orderList);
//        System.out.println(takenOrders);
        return "text complete";
    }
}

