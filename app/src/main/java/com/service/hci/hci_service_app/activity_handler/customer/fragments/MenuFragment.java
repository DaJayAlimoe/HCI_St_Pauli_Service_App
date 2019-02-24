package com.service.hci.hci_service_app.activity_handler.customer.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.service.hci.hci_service_app.R;
import com.service.hci.hci_service_app.activity_handler.customer.CustomerMain;
import com.service.hci.hci_service_app.activity_handler.customer.OnScrollObserver;
import com.service.hci.hci_service_app.activity_handler.customer.adapters.ItemListAdapter;
import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;
import com.service.hci.hci_service_app.data_types.Cart;
import com.service.hci.hci_service_app.data_types.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    public MenuFragment() {
    }

    private CustomerMain customerMain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.customer_menu, container, false);
        ListView listViewItems = (ListView) view.findViewById(R.id.listView_customer_menu); // get the chil d text view
        customerMain = (CustomerMain) getActivity();
        Api stApi = Api.getInstance(getContext());
        JSONObject response = stApi.getItems();
        Session session = Session.getInstance(getContext());
        session.setItems(response.toString());
        ArrayList<Item> itemArrayList = new ArrayList<>();

        TextView textViewSeatNr = customerMain.getTextViewSeatNr();

        try {
            JSONArray items = response.getJSONArray("items");
            for (int index = 0; index < items.length(); index++) {
                JSONObject item = items.getJSONObject(index);
                Item itemObj = new Item(item.getLong("id"), item.getString("description"), item.getString("name"), item.getString("name"));
                itemArrayList.add(itemObj);
                Log.i("Menu Item " + index, itemObj.toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemListAdapter itemListAdapter = new ItemListAdapter(view.getContext(), R.layout.customer_item_view, itemArrayList);
        listViewItems.setAdapter(itemListAdapter);

        CounterFab counterFab= (CounterFab) customerMain.getFloatingActionButton();

        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.setTitle("In den Warenkorb");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Hinzufügen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                             if (  Cart.getInstance().add(itemListAdapter.getItem(i), 1)) {
                                 Toast.makeText(view.getContext(), itemListAdapter.getItem(i).getName() + " dem Warenkorb hinzugefügt", Toast.LENGTH_SHORT).show();
                                 counterFab.increase();
                                 dialog.dismiss();

                             }
                             else{
                                 Toast.makeText(view.getContext()," Sie können nicht mehr als 5 Elemente von einem Artikel im Warenkorb haben", Toast.LENGTH_SHORT).show();
                             }
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

        listViewItems.setOnScrollListener(new OnScrollObserver() {
            @Override
            public void onScrollUp() {
                counterFab.setActivated(true);
                counterFab.setVisibility(View.VISIBLE);
                textViewSeatNr.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollDown() {
                counterFab.setActivated(false);
                counterFab.setVisibility(View.INVISIBLE);
                textViewSeatNr.setVisibility(View.INVISIBLE);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
