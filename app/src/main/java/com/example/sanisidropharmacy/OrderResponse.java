package com.example.sanisidropharmacy;

public class OrderResponse {
    private boolean success;
    private int order_id;
    private String message;

    public boolean isSuccess() { return success; }
    public int getOrderId() { return order_id; }
    public String getMessage() { return message; }
}
