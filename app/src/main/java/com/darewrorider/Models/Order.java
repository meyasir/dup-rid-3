package com.darewrorider.Models;

import java.util.ArrayList;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class Order {

    int id;

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getDropAddress() {
        return dropAddress;
    }

    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }

    String pickAddress;
    String dropAddress;
    String acknowledge;

    public String getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(String acknowledge) {
        this.acknowledge = acknowledge;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    String pick;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    String deliveryCharges;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    String customerName;

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    String readyTime;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    String detail;
    public long getPrepareTime() {
        return prepareTime;
    }
    public void setPrepareTime(long prepareTime) {
        this.prepareTime = prepareTime;
    }

    long prepareTime;

    public ArrayList<OrderFoodItem> getOrderFoodItems() {
        return orderFoodItems;
    }
    public void setOrderFoodItems(ArrayList<OrderFoodItem> orderFoodItems) {
        this.orderFoodItems = orderFoodItems;
    }

    ArrayList<OrderFoodItem> orderFoodItems;

    public Order() {
    }
}
