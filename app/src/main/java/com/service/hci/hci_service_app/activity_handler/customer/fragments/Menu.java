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
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ItemListAdapter;

import java.util.ArrayList;

public class Menu extends Fragment{

    public Menu() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_menu, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView); // get the child text view

        Item item1 = new Item("Cola","Cola","Cola");
        Item item2 = new Item("Fanta","Fanta","Fanta");
        Item item3 = new Item("Sprite","Sprite","Sprite");
        Item item4 = new Item("Bier","Bier","Bier");
        Item item5 = new Item("Wasser","Wasser","Wasser");
        Item item6 = new Item("Snack1","Snack1","Snack1");
        Item item7 = new Item("Snack2","Snack2","Snack2");
        Item item8 = new Item("Snack3","Snack3","Snack3");
        Item item9 = new Item("Snack4","Snack4","Snack4");
        Item item10 = new Item("Snack5","Snack5","Snack5");
        Item item11 = new Item("Snack6","Snack6","Snack6");
        Item item12 = new Item("Snack7","Snack7","Snack7");
        Item item13 = new Item("Snack8","Snack8","Snack8");
        ArrayList<Item> itemArrayList = new ArrayList<>();
        itemArrayList.add(item1);
        itemArrayList.add(item2);
        itemArrayList.add(item3);
        itemArrayList.add(item4);
        itemArrayList.add(item5);
        itemArrayList.add(item6);
        itemArrayList.add(item7);
        itemArrayList.add(item8);
        itemArrayList.add(item9);
        itemArrayList.add(item10);
        itemArrayList.add(item11);
        itemArrayList.add(item12);
        itemArrayList.add(item13);

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
