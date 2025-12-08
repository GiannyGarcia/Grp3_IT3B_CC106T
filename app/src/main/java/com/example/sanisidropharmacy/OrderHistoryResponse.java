package com.example.sanisidropharmacy;

import java.util.List;

public class OrderHistoryResponse {

    private boolean success;
    private List<OrderItem> orders;

    public boolean isSuccess() { return success; }
    public List<OrderItem> getOrders() { return orders; }

    // ----- Order Item (represents each order) -----
    public static class OrderItem {
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

    // ----- Order Line (represents each product inside an order) -----
    public static class OrderLine {
        public int product_id;
        public String name;
        public int qty;
        public double price;
    }
}
