package com.service.hci.hci_service_app.data_types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {
    HashMap< Item,Integer> cart;
    private static Cart instance;
    private Cart() {
        this.cart = new HashMap<>();
    }
    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new Cart();
        }
    }
    public static Cart getInstance()
    {
        // Return the instance
        return instance;
    }

    public HashMap<Item, Integer> getCart() {
        return cart;
    }

    public void add(Item item, int amount)
    {
        cart.put(item,amount);
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
