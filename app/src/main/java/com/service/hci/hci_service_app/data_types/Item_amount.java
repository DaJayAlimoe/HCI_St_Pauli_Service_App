package com.service.hci.hci_service_app.data_types;

public class Item_amount {
    Item item;
    int amount;

    public Item_amount(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
