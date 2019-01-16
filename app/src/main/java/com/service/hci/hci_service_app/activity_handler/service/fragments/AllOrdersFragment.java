package com.service.hci.hci_service_app.activity_handler.service.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;
import com.service.hci.hci_service_app.activity_handler.service.adapters.PartialOrdersAdapter;

import com.service.hci.hci_service_app.R;

import java.util.ArrayList;

public class AllOrdersFragment extends Fragment {

    public AllOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_orders, container, false);

        ListView listView = view.findViewById(R.id.service_all_orders_listView);

        TextView header1 = view.findViewById(R.id.textView_ser_orders_header1);
        TextView header2 = view.findViewById(R.id.textView_ser_orders_header2);
        TextView header3 = view.findViewById(R.id.textView_ser_orders_header3);
        TextView header4 = view.findViewById(R.id.textView_ser_orders_header4);

        header1.setText("Sitz-Nr:");
        header2.setText("Menge:");
        header3.setText("Typ:");
        header4.setText("In Auftrag nehmen:");

        //Dummy Liste
        PartialOrder p1 = new PartialOrder(5060, 2, "Bier");
        PartialOrder p2 = new PartialOrder(210, 5, "Limonade");
        PartialOrder p3 = new PartialOrder(378, 7, "Cola");
        PartialOrder p4 = new PartialOrder(3, 10, "Sprite");
        PartialOrder p5 = new PartialOrder(6780, 15, "Bratwurst");
        PartialOrder p6 = new PartialOrder(5500, 4, "Hot Dog");
        PartialOrder p7 = new PartialOrder(444, 1, "Brezel");
        PartialOrder p8 = new PartialOrder(1234, 20, "Bier");
        PartialOrder p9 = new PartialOrder(567, 8, "Bratwurst");
        PartialOrder p10 = new PartialOrder(35, 9, "Cola");
        PartialOrder p11 = new PartialOrder(68, 1, "Fanta");

        ArrayList<PartialOrder> partialOrders = new ArrayList<>();
        partialOrders.add(p1);
        partialOrders.add(p2);
        partialOrders.add(p3);
        partialOrders.add(p4);
        partialOrders.add(p5);
        partialOrders.add(p6);
        partialOrders.add(p7);
        partialOrders.add(p8);
        partialOrders.add(p9);
        partialOrders.add(p10);
        partialOrders.add(p11);
        partialOrders.add(p4);
        partialOrders.add(p6);
        partialOrders.add(p2);
        partialOrders.add(p5);
        partialOrders.add(p5);
        partialOrders.add(p5);
        partialOrders.add(p5);

        PartialOrdersAdapter adapter = new PartialOrdersAdapter(this.getContext(),
                R.layout.service_all_orders_adapter_view, partialOrders);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;

    }

}