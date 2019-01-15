package com.service.hci.hci_service_app.data_types;

public class Order {
    Item item ;
    Integer amount;
    String OrderNR;

    public String getOrderNR() {
        return OrderNR;
    }

    public void setOrderNR(String orderNR) {
        OrderNR = orderNR;
    }

    public Order(String orderNR ,Item item, Integer amount) {
        this.item = item;
        this.amount = amount;
        this.OrderNR = orderNR;
    }

    public Item getItem() {

        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
