package com.service.hci.hci_service_app.activity_handler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
import com.service.hci.hci_service_app.activity_handler.customer.ItemConfirmView;
import com.service.hci.hci_service_app.activity_handler.service.ServiceOrdersActivity;

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
            Intent intent = new Intent(this, ServiceOrdersActivity.class);
            startActivity(intent);
        }

    }
}

