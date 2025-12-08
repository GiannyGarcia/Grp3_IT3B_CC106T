package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {

    private int id;
    private int user_id;
    private double total;
    private String status;
    private String created_at;
    private String shipping_address;
    private String delivery_address;
    private String payment_method;
    private String reference;

    private List<OrderHistoryResponse.OrderLine> items;

    // ------------------------------
    // ORIGINAL GETTERS
    // ------------------------------
    public int getId() { return id; }
    public int getUser_id() { return user_id; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public String getCreated_at() { return created_at; }
    public String getShipping_address() { return shipping_address; }
    public String getDelivery_address() { return delivery_address; }
    public String getPayment_method() { return payment_method; }
    public String getReference() { return reference; }
    public List<OrderHistoryResponse.OrderLine> getItems() { return items; }

    // ------------------------------
    // FIXED COMPATIBILITY GETTERS
    // ------------------------------

    public int getUserId() { return user_id; }
    public String getCreatedAt() { return created_at; }
    public String getPaymentMethod() { return payment_method; }
    public String getDeliveryAddress() { return delivery_address; }

    // ------------------------------
    // NEEDED SETTERS
    // ------------------------------

    public void setStatus(String status) { this.status = status; }

    public void setItems(List<OrderHistoryResponse.OrderLine> items) {
        this.items = items;
    }
}
