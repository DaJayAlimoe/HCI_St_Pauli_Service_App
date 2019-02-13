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
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ShoppingCartItemListAdapter;
import com.service.hci.hci_service_app.data_types.Cart;

public class ShoppingCart extends AppCompatActivity implements View.OnClickListener {
    //Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        final Dialog dialog = new Dialog(view.getContext());
        TextView txtclose;
        Button btnOrder;
//        OrderInCart shoppingCart = new OrderInCart();
        dialog.setContentView(R.layout.customer_shoppingcart);
        ListView listView = (ListView) dialog.findViewById(R.id.shoppingCartList);
        ShoppingCartItemListAdapter itemListAdapter = new ShoppingCartItemListAdapter(dialog.getContext(), R.layout.customer_shopping_list_view, Cart.getInstance().getCart());
        listView.setAdapter(itemListAdapter);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        txtclose.setText("M");
        btnOrder = (Button) dialog.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.getInstance().sendOrders();
            } //TODO Logic was dann passieren soll


        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
