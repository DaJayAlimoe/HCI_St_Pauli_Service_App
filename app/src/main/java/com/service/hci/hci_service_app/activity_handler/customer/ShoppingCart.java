package com.service.hci.hci_service_app.activity_handler.customer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.service.hci.hci_service_app.R;

public class ShoppingCart extends AppCompatActivity implements View.OnClickListener {
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnOrder;
//        OrderInCart shoppingCart = new OrderInCart();
        myDialog.setContentView(R.layout.customer_shoppingcart);
        ListView listView = (ListView) myDialog.findViewById(R.id.customer_AllOrdersView);
//        ShoppingCartItemListAdapter itemListAdapter = new ShoppingCartItemListAdapter(v.getContext() ,R.layout.customer_order_list_view,OrderInCart.getItemArrayList());
//        listView.setAdapter(itemListAdapter);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        btnOrder = (Button) myDialog.findViewById(R.id.btnAddCart);
        btnOrder.setOnClickListener(this);
        //TODO Logic was dan passieren soll
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onClick(View view) {
ShowPopup(view);
    }
}
