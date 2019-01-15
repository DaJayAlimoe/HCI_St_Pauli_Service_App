package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.ItemConfirmView;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ItemListAdapter;
import com.service.hci.hci_service_app.data_types.CartList;

import java.util.ArrayList;

public class Orders extends Fragment {

    public Orders() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_orders, container, false);

        ListView listView = (ListView) view.findViewById(R.id.customer_AllOrdersView); // get the child text view

        CartList cartList;

        ItemListAdapter itemListAdapter = new ItemListAdapter(view.getContext() ,R.layout.customer_item_view, itemArrayList);
        listView.setAdapter(itemListAdapter);

//        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(),R.layout.customer_item_view,itemArrayList);
//        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(),ItemConfirmView.class);
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
