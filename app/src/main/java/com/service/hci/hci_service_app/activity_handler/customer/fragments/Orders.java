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
import com.service.hci.hci_service_app.activity_handler.customer.adapters.OrderListAdapter;
import com.service.hci.hci_service_app.data_types.CartList;
import com.service.hci.hci_service_app.data_types.Item;
import com.service.hci.hci_service_app.data_types.Order;

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
        Order p1 = new Order("1060", item1, 3);
        Order p2 = new Order("2060", item4, 3);
        Order p3 = new Order("3060", item5, 3);
        Order p4 = new Order("6060", item6, 3);
        Order p5 = new Order("4060", item7, 3);
        Order p6 = new Order("8060", item9, 3);
        Order p7 = new Order("9060", item2, 3);
        Order p8 = new Order("10060", item12, 3);
        Order p9 = new Order("091", item13, 3);
        Order p10 = new Order("1312", item4, 3);
        Order p11 = new Order("7323", item4, 3);
        Order p12 = new Order("8008", item4, 3);
        Order p13 = new Order("1337", item4, 3);
        Order p14 = new Order("666", item4, 3);
        ArrayList<Order> itemArrayList = new ArrayList<>();
        itemArrayList.add(p1);
        itemArrayList.add(p2);
        itemArrayList.add(p3);
        itemArrayList.add(p4);
        itemArrayList.add(p5);
        itemArrayList.add(p6);
        itemArrayList.add(p7);
        itemArrayList.add(p8);
        itemArrayList.add(p9);
        itemArrayList.add(p10);
        itemArrayList.add(p11);
        itemArrayList.add(p12);
        itemArrayList.add(p13);
        itemArrayList.add(p14);


        CartList cartList;

        OrderListAdapter itemListAdapter = new OrderListAdapter(view.getContext() ,R.layout.customer_order_list_view, itemArrayList);
        listView.setAdapter(itemListAdapter);

//        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(),R.layout.customer_item_view,itemArrayList);
//        listView.setAdapter(arrayAdapter);

     /*   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(),ItemConfirmView.class);
                intent.putExtra("name", itemListAdapter.getItem(i).getName());
                intent.putExtra("description", itemListAdapter.getItem(i).getDescription());
                intent.putExtra("picture", itemListAdapter.getItem(i).getPicture());
                startActivity(intent);
            }
        });*/


        // Inflate the layout for this fragment
        return view;
    }
}
