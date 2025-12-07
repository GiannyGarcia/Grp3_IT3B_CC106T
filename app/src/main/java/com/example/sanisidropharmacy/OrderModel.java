package com.example.sanisidropharmacy;

import java.util.List;

public class OrderModel {
    private int id;
    private int user_id;
    private double total;
    private String status;
    private String created_at;
    private String payment_method;
    private String reference;
    private String delivery_address;

    private List<OrderItem> items;

    public int getId() { return id; }
    public int getUserId() { return user_id; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return created_at; }
    public String getPaymentMethod() { return payment_method; }
    public String getReference() { return reference; }
    public String getDeliveryAddress() { return delivery_address; }

    public List<OrderItem> getItems() { return items; }
}
