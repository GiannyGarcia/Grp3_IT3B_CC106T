package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse implements Serializable {

    private boolean success;
    private List<OrderDto> orders;

    public boolean isSuccess() { return success; }
    public List<OrderDto> getOrders() { return orders; }

    public static class OrderItem implements Serializable {
        public int id;
        public int user_id;
        public double total;
        public String status;
        public String created_at;
        public String shipping_address;
        public String delivery_address;
        public String payment_method;
        public String reference;

        public List<OrderLine> items;
    }

    public static class OrderLine implements Serializable {
        public int product_id;
        public String name;
        public int qty;
        public double price;
    }
}
