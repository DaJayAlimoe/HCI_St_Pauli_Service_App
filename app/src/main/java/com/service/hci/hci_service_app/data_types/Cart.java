package com.service.hci.hci_service_app.data_types;

import com.service.hci.hci_service_app.data_layer.Api;
import com.service.hci.hci_service_app.data_layer.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void add(Item item, int amount)
    {
        Item_amount item_amount= new Item_amount(item,amount);
        for (Item_amount ia:cart) {
            if (ia.getItem().getName().equals(item.getName()))
                return;
        }
        cart.add(item_amount);
    }
    public Boolean sendOrders(){
        int userID = Session.getUserId();

        ArrayList<JSONObject> orderList = new ArrayList<>();
        for(Item_amount entry : cart) {
            JSONObject jsonObject = new JSONObject();
            Item item = entry.item;
            int amount = entry.amount;
            try {
                jsonObject.put("amount",amount);
                jsonObject.put("item_id",item.getId());
                jsonObject.put("seat_id",userID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(orderList.isEmpty() || orderList == null){
            return false;
        }else{
            JSONArray orderArray = new JSONArray();
            // convert to JSON and send, return true
            for(JSONObject orderInList : orderList){

                orderArray.put(orderInList);
            }
            Api stApi = new Api();
            boolean send = stApi.placeOrder(orderArray);

            if(!send){
                return false;
            }

            cart.clear();
            return true;
        }
    }
    public void clear()
    {
        cart.clear();
    }
    public void delete(Item item)
    {
        cart.remove(item);
    }

}
