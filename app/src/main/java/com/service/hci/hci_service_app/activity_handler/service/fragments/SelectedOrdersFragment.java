package com.service.hci.hci_service_app.activity_handler.service.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.service.hci.hci_service_app.activity_handler.service.PartialOrder;

import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.service.adapters.SelectedPartialOrdersAdapter;

import java.util.ArrayList;

public class SelectedOrdersFragment extends Fragment {

    public SelectedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_selected_orders, container, false);

        ListView listView = view.findViewById(R.id.listView_service_selected_orders);

        TextView header1 = view.findViewById(R.id.textView_service_selected_orders_header1);
        TextView header2 = view.findViewById(R.id.textView_service_selected_orders_header2);
        TextView header3 = view.findViewById(R.id.textView_service_selected_orders_header3);
        TextView header4 = view.findViewById(R.id.textView_service_selected_orders_header4);

        header1.setText("Sitz-Nr:");
        header2.setText("Menge:");
        header3.setText("Typ:");
        header4.setText("Erledigt:");

        //Dummy Liste
        PartialOrder p1 = new PartialOrder(5060, 2, "Bier");
        PartialOrder p2 = new PartialOrder(210, 5, "Limonade");
        PartialOrder p3 = new PartialOrder(378, 7, "Cola");
        PartialOrder p4 = new PartialOrder(3, 10, "Sprite");
        PartialOrder p5 = new PartialOrder(6780, 15, "Bratwurst");
        PartialOrder p6 = new PartialOrder(5500, 4, "Hot Dog");


        ArrayList<PartialOrder> partialOrders = new ArrayList<>();
        partialOrders.add(p1);
        partialOrders.add(p2);
        partialOrders.add(p3);
        partialOrders.add(p4);
        partialOrders.add(p5);
        partialOrders.add(p6);

        SelectedPartialOrdersAdapter adapter = new SelectedPartialOrdersAdapter(this.getContext(),
                R.layout.service_selected_orders_adapter_view, partialOrders);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;

    }

}