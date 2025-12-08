package com.example.sanisidropharmacy;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("order_id")
    private int orderId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getOrderId() {
        return orderId;
    }
}
