package com.example.sanisidropharmacy;

import java.util.List;

public class OrderHistoryResponse {
    private boolean success;
    private List<OrderModel> orders;

    public boolean isSuccess() { return success; }
    public List<OrderModel> getOrders() { return orders; }
}
