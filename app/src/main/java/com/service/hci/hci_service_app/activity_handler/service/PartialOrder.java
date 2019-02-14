package com.service.hci.hci_service_app.activity_handler.service;

public class PartialOrder {

    private int seat;
    private int amount;
    private String itemName;

    public PartialOrder(int seat, int amount, String itemName) {
        this.seat = seat;
        this.amount = amount;
        this.itemName = itemName;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}

