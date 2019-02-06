package com.service.hci.hci_service_app.activity_handler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
import com.service.hci.hci_service_app.activity_handler.customer.ItemConfirmView;
import com.service.hci.hci_service_app.activity_handler.service.ServiceMain;
import com.service.hci.hci_service_app.data_layer.Api;

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
    }

    @Override
    public void onClick(View v) {
        int actView = v.getId();

        if(actView == R.id.btn_to_customer){
            Intent intent = new Intent(Main.this, CustomerMain.class);
            startActivity(intent);
        }else if(actView == R.id.btn_to_service){
            Intent intent = new Intent(this, ServiceMain.class);
            startActivity(intent);
        }

    }

    public String test() {
        Api stApi = new Api();

        // User : SEAT
        stApi.authenticate("seat:199c14c81cce07eb");

        JSONObject items = stApi.getItems();
        return items.toString();
//        System.out.println(items.toString());
//
//        JSONObject orders = stApi.getOrders();
//        System.out.println(orders.toString());
//
//        JSONObject myOrders = stApi.getMyOrders();
//        System.out.println(myOrders);
//
//        JSONObject orderList = null;
//        try {
//            orderList = new JSONObject("{" +
//                    "{\"amount\": 5, \"item_id\": 2, \"seat_id\": 28}," +
//                    "{\"amount\": 5, \"item_id\": 1, \"seat_id\": 28}," +
//                    "{\"amount\": 5, \"item_id\": 3, \"seat_id\": 28} " +
//                    "}");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JSONObject placedOrders = stApi.placeOrder(orderList);
//        System.out.println(placedOrders);
//
//        items = null;
//        orders = null;
//        myOrders = null;
//        orderList = null;
//
//        // User : Employee
//        stApi.authenticate("empl:61260b2407acf125");
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
    }
}

