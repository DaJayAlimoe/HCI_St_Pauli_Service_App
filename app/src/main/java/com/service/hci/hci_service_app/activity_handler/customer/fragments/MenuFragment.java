package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;


import com.service.hci.hci_service_app.R;

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
        ListView listViewItems = (ListView) view.findViewById(R.id.listView_customer_menu); // get the chil d text view
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton__customer_cart_menu);
        Cart.initInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(view.getContext());
                Button btnOrder;
                Button btnCancel;
                Button btnMinus;
                Button btnDelete;
                Button btnPlus;


                ShoppingCartItemListAdapter itemListAdapter = new ShoppingCartItemListAdapter(dialog.getContext(), R.layout.customer_shopping_list_view, Cart.getInstance().getCart());
                listViewItems.setAdapter(itemListAdapter);

                //dialog.setContentView(R.layout.customer_shoppingcart);
//                ListView listViewCart = (ListView) dialog.findViewById(R.id.shoppingCartList);
//
//                listViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    }
//                });



//                btnPlus = dialog.findViewById(R.id.btn_plus_Cart);
//                btnPlus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ArrayList<Item_amount> list = Cart.getInstance().getCart();
//                    }
//                });
//
//                btnMinus = dialog.findViewById(R.id.btn_plus_Cart);
//                btnMinus.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                btnDelete = dialog.findViewById(R.id.btn_plus_Cart);
//                btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        itemListAdapter.remove();
//                    }
//                });




                btnCancel =  dialog.findViewById(R.id.btn_customer_cart_back);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnOrder = (Button) dialog.findViewById(R.id.btn_customer_order);
                btnOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cart.getInstance().sendOrders();
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
        }});


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
        listViewItems.setAdapter(itemListAdapter);


        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("In den Warenkorb");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hinzufügen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Cart.getInstance().add(itemListAdapter.getItem(i), 1);
                                Toast.makeText(view.getContext(),itemListAdapter.getItem(i) + " den Warenkorb hinzugefügt", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
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
}
