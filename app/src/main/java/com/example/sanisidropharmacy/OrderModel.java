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
    // SNAKE_CASE GETTERS (JSON-backed)
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
    // CAMELCASE GETTERS (used by adapters/activities)
    // ------------------------------
    public int getUserId() { return user_id; }
    public String getCreatedAt() { return created_at; }
    public String getPaymentMethod() { return payment_method; }
    public String getDeliveryAddress() { return delivery_address; }

    // ------------------------------
    // SETTERS (used by conversion code)
    // ------------------------------
    public void setId(int id) { this.id = id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setTotal(double total) { this.total = total; }
    public void setStatus(String status) { this.status = status; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public void setShipping_address(String shipping_address) { this.shipping_address = shipping_address; }
    public void setDelivery_address(String delivery_address) { this.delivery_address = delivery_address; }
    public void setPayment_method(String payment_method) { this.payment_method = payment_method; }
    public void setReference(String reference) { this.reference = reference; }
    public void setItems(List<OrderHistoryResponse.OrderLine> items) { this.items = items; }
}
