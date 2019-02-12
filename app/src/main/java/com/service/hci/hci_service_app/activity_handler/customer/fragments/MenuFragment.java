package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.service.hci.hci_service_app.R;

import com.service.hci.hci_service_app.activity_handler.customer.ShoppingCart;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ShoppingCartItemListAdapter;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;
import com.service.hci.hci_service_app.data_types.Cart;
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ItemListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_menu, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listView); // get the chil d text view
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton_Cart);
        ShoppingCart shoppingCart = new ShoppingCart();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        Cart.initInstance();
        ShoppingCart x = new ShoppingCart();
        fab.setOnClickListener(x);

        Api stApi = new Api();
        JSONObject response = stApi.getItems();
        Session.setItems(response.toString());
        ArrayList<Item> itemArrayList = new ArrayList<>();
        try {
            JSONArray items = response.getJSONArray("items");
            for (int index = 0; index < items.length(); index++) {
                JSONObject item = items.getJSONObject(index);
                Item itemObj = new Item(item.getLong("id"), item.getString("description"), item.getString("name"), item.getString("name"));
                itemArrayList.add(itemObj);
                Log.i("Menu Item "+index, itemObj.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemListAdapter itemListAdapter = new ItemListAdapter(view.getContext(), R.layout.customer_item_view, itemArrayList);
        listView.setAdapter(itemListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("In den Warenkorb");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "hinzufügen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Cart.getInstance().add(itemListAdapter.getItem(i), 1);

                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "AbbrechenL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
//                Intent intent = new Intent(view.getContext(), ItemConfirmView.class);
//                intent.putExtra("name", itemListAdapter.getItem(i).getName());
//                intent.putExtra("description", itemListAdapter.getItem(i).getDescription());
//                intent.putExtra("picture", itemListAdapter.getItem(i).getPicture());
//                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}
