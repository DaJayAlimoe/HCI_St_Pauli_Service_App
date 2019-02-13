//package com.service.hci.hci_service_app.activity_handler.customer;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.service.hci.hci_service_app.R;
//import com.service.hci.hci_service_app.data_layer.Api;
//import com.service.hci.hci_service_app.data_types.Order;
//
//public class ItemConfirmView extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
//        alertDialog.setTitle("In den Warenkorb");
//        //alertDialog.setMessage(holder.amount.toString());
//
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "hinzufügen",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = getIntent();
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "doch nicht stornieren",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
////        setContentView(R.layout.customer_item_confirm_view);
////
////////        listdata = findViewById(R.id.listdata);
////////        imageView = findViewById(R.id.imageView);
////        Intent intent = getIntent();
////        String name =  intent.getStringExtra("name");
////        int picture = intent.getIntExtra("picture",0);
////        String description = intent.getStringExtra("description");
////        Button fab = findViewById(R.id.btnOrder);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v){
////
////            }
////        });
////        Toast.makeText(ItemConfirmView.this,"Daten erhalten: " + name +" "+ picture+" " + description+" ",Toast.LENGTH_SHORT).show();
//////        listdata.setText(receivedName);
//////        imageView.setImageResource(receivedImage);
////        //enable back Button
////        //this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//}
