package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.ItemConfirmView;
import com.service.hci.hci_service_app.activity_handler.customer.ShoppingCart;
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
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton_Cart);
        ShoppingCart shoppingCart = new ShoppingCart();
        fab.setOnClickListener(shoppingCart);
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
                itemArrayList.add(new Item(item.getLong("id"), item.getString("description"), item.getString("name"), item.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemListAdapter itemListAdapter = new ItemListAdapter(view.getContext(), R.layout.customer_item_view, itemArrayList);
        listView.setAdapter(itemListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), ItemConfirmView.class);
                intent.putExtra("name", itemListAdapter.getItem(i).getName());
                intent.putExtra("description", itemListAdapter.getItem(i).getDescription());
                intent.putExtra("picture", itemListAdapter.getItem(i).getPicture());
                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}
