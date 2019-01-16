package com.service.hci.hci_service_app.activity_handler.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.service.hci.hci_service_app.R;

public class ItemConfirmView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_item_confirm_view);

////        listdata = findViewById(R.id.listdata);
////        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        String name =  intent.getStringExtra("name");
        String picture = intent.getStringExtra("picture");
        String description = intent.getStringExtra("description");

        Toast.makeText(ItemConfirmView.this,"Daten erhalten: " + name +" "+ picture+" " + description+" ",Toast.LENGTH_SHORT).show();
//        listdata.setText(receivedName);
//        imageView.setImageResource(receivedImage);
        //enable back Button
        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
