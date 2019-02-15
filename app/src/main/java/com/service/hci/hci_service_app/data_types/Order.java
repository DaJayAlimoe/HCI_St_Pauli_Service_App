package com.service.hci.hci_service_app.data_types;

import android.graphics.Color;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {

    public enum OrderStatus {
        PREORDERED(0), ONTHEWAY(1), CANCELED(2), CLOSED(3);

        private final int status;
        private final String button_text;

        OrderStatus(int status) {
            this.status = status;
            switch (this.status) {
                case 0:
                    this.button_text = "Bestellt";
                    break;
                case 1:
                    this.button_text = "Auf dem Weg";
                    break;
                case 2:
                    this.button_text = "Storniert";
                    break;
                case 3:
                    this.button_text = "Zugestellt";
                    break;
                default:
                    this.button_text = "NONE";
                    break;
            }
        }

        public int getStatus() {return status;}
        public String getButtonText() {return button_text;}
    }

    public OrderStatus getStatus() {
        return status;
    }
//
//    public void setStatus(OrderStatus status) {
//        this.status = status;
//    }

    private Item item ;

    public int getSeatNR() {
        return seatNR;
    }

    private int amount;
    private int orderNR;
    private int eta;
    private Timestamp activeAt;
    private Timestamp createdOn;
    private Timestamp lastUpdatedOn;
    private OrderStatus status;
    private int seatNR;
    private static ArrayList<JSONObject> orderList = new ArrayList<>();

    public Order(int seatNR ,Item item, int amount) {
        this.item = item;
        this.amount = amount;
        this.seatNR = seatNR;
    }

    public Order(Item item, int amount, int orderNR, int eta, Timestamp activeAt, Timestamp createdOn, Timestamp lastUpdatedOn, OrderStatus status) {
        this.item = item;
        this.amount = amount;
        this.orderNR = orderNR;
        this.eta = eta;
        this.activeAt = activeAt;
        this.createdOn = createdOn;
        this.lastUpdatedOn = lastUpdatedOn;
        this.status = status;
    }

    public void setStatus(OrderStatus ordStatus) {
        this.status = ordStatus;
    }

    public String getButtonText() {
        return this.status.getButtonText();
    }

    public Timestamp getActiveAt() {
        return activeAt;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public static ArrayList<JSONObject> getOrderList() {
        return orderList;
    }

    public static void addOrder(JSONObject order) {
        orderList.add(order);
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public int getOrderNR() {
        return orderNR;
    }

    public void setOrderNR(String orderNR) {
        orderNR = orderNR;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public static boolean orderListIsEmpty(){
        return orderList.isEmpty();
    }

    @Override
    public String toString() {
        return "Order{" +
                "item=" + item +
                ", amount=" + amount +
                ", orderNR=" + orderNR +
                ", eta=" + eta +
                ", activeAt=" + activeAt +
                ", createdOn=" + createdOn +
                ", lastUpdatedOn=" + lastUpdatedOn +
                ", status=" + status +
                '}';
    }
}
