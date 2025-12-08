package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse implements Serializable {

    private boolean success;
    private List<OrderDto> orders;

    public boolean isSuccess() {
        return success;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }
}
