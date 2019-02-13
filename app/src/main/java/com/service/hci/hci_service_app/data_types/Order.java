package com.service.hci.hci_service_app.data_types;

import com.service.hci.hci_service_app.data_layer.Api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {

    public enum OrderStatus {
        PREORDERED(0), ONTHEWAY(1), CANCELED(2), CLOSED(3);

        private final int status;
        private final int button_icon;

        OrderStatus(int status) {
            this.status = status;
            switch (this.status) {
                case 0:
                    this.button_icon = android.R.drawable.btn_dialog;
                    break;
                case 1:
                    this.button_icon = android.R.drawable.ic_menu_set_as;
                    break;
                case 2:
                    this.button_icon = android.R.drawable.checkbox_on_background;
                    break;
                case 3:
                    this.button_icon = android.R.drawable.btn_dialog;
                    break;
                default:
                    this.button_icon = 0;
                    break;
            }
        }

        public int getStatus() {return status;}
        public int getStatusButtonIcon() {return button_icon;}
    }

    public OrderStatus getStatus() {
        return status;
    }
//
//    public void setStatus(OrderStatus status) {
//        this.status = status;
//    }

    private Item item ;
    private int amount;
    private int orderNR;
    private int eta;
    private Timestamp activeAt;
    private Timestamp createdOn;
    private Timestamp lastUpdatedOn;
    private OrderStatus status;
    private static ArrayList<JSONObject> orderList = new ArrayList<>();


    public Order(int orderNR ,Item item, int amount) {
        this.item = item;
        this.amount = amount;
        this.orderNR = orderNR;
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

    public Order(Item item, int amount, int orderNR, int eta, OrderStatus status) {
        this.item = item;
        this.amount = amount;
        this.orderNR = orderNR;
        this.eta = eta;
        this.status = status;
    }

    public int getStatusButtonIcon() {
        return this.status.getStatusButtonIcon();
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

    public static boolean sendOrders(){
        // send orderList to server and clear the List
        if(orderListIsEmpty() || orderList == null){
            return false;
        }else{
            JSONArray orderArray = new JSONArray();
            // convert to JSON and send, return true
            for(JSONObject orderInList : orderList){

                orderArray.put(orderInList);
            }
            Api stApi = Api.getInstance();
            boolean send = stApi.placeOrder(orderArray);

            if(!send){
                return false;
            }

            orderList.clear();
            return true;
        }
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
