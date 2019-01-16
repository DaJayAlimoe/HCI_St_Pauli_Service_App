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

public class MenuFragment extends Fragment{

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_menu, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView); // get the chil d text view

        Item item1 = new Item("Ingredients","Cola", R.drawable.cola);
        Item item2 = new Item("Ingredients","Fanta", R.drawable.fanta);
        Item item3 = new Item("Ingredients","Sprite", R.drawable.sprite);
        Item item4 = new Item("Ingredients","Bier", R.drawable.beer);
        Item item5 = new Item("Ingredients","Wasser", R.drawable.water);
        Item item6 = new Item("Ingredients","Brezel", R.drawable.brezel);
        Item item7 = new Item("Ingredients","Bratwurst", R.drawable.bratwurst);
        Item item8 = new Item("Ingredients","Snack3", R.drawable.beer);
        Item item9 = new Item("Ingredients","Snack4", R.drawable.beer);
        Item item10 = new Item("Ingredients","Snack5", R.drawable.beer);
        Item item11 = new Item("Ingredients","Snack6", R.drawable.beer);
        Item item12 = new Item("Ingredients","Snack7", R.drawable.beer);
        Item item13 = new Item("Ingredients","Snack8", R.drawable.beer);
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

        //listView.addHeaderView(textView);

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
