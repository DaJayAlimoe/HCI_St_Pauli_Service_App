package com.service.hci.hci_service_app.activity_handler.service;

public class PartialOrder {

    private int seat;
    private int count;
    private String itemName;

    public PartialOrder(int seat, int count, String itemName) {
        this.seat = seat;
        this.count = count;
        this.itemName = itemName;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

