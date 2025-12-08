package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.List;

public class OrderHistoryResponse implements Serializable {

    private boolean success;
    private List<OrderItem> orders;

    public boolean isSuccess() { return success; }
    public List<OrderItem> getOrders() { return orders; }

    // -----------------------------------------
    // ORDER ITEM (One Order)
    // -----------------------------------------
    public static class OrderItem implements Serializable {
        private static final long serialVersionUID = 1L;

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

    // -----------------------------------------
    // ORDER LINE (Item inside an order)
    // -----------------------------------------
    public static class OrderLine implements Serializable {
        private static final long serialVersionUID = 1L;

        public int product_id;
        public String name;
        public int qty;
        public double price;
    }
}
