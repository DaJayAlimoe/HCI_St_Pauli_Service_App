package com.service.hci.hci_service_app.data_types;

import java.util.ArrayList;

public class CartList {

    private static ArrayList<Item> itemArrayList;

    public CartList() {
        itemArrayList = new ArrayList<>();
    }

    public ArrayList<Item> getItemArrayList() {
        return itemArrayList;
    }

    public static void addItem(Item item) {
        itemArrayList.add(item);
    }

}
