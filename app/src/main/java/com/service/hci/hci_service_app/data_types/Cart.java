package com.service.hci.hci_service_app.data_types;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cart {

    private static final ArrayList<Item_amount> cart = new ArrayList<>();
    private static Cart instance;
    private  Cart() { }
    public static void initInstance()
    {
        if (instance == null)
        {
            instance = new Cart();
        }
    }

    public static Cart getInstance()
    {
        return instance;
    }

    public ArrayList<Item_amount> getCart() {
        return cart;
    }

    public boolean add(Item item, int amount)
    {
        Log.i("Log in cart add", item.toString() + String.valueOf(amount));
        Item_amount item_amount= new Item_amount(item,amount);
        for (Item_amount ia:cart) {
            if (ia.getItem().getName().equals(item.getName())) {
                if (ia.amount < 5) {
                    ia.amount = ia.amount + 1;
                    return true;
                }
                else {
                    return false;
                }

            }
        }
        cart.add(item_amount);
        return true;
    }

    public JSONArray getOrders(int userID){
        JSONArray orderArray = new JSONArray();
        Log.i("in sendOrder ID: ", String.valueOf(userID));
        if(!cart.isEmpty() || cart != null){
            // convert to JSON and send, return true
            for(Item_amount entry : cart){
                JSONObject orderInList = new JSONObject();
                Item item = entry.item;
                int amount = entry.amount;
                try {
                    orderInList.put("amount",amount);
                    orderInList.put("item_id",item.getId());
                    orderInList.put("seat_id",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderArray.put(orderInList);
            }

            cart.clear();
        }
        return orderArray;
    }

    public void delete(Item item)
    {
        cart.remove(item);
    }

}
