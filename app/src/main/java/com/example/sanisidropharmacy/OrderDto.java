package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.List;

public class OrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // -------------------------
    // ORDER FIELDS FROM SERVER
    // -------------------------
    public int id;
    public int user_id;
    public double total;
    public String status;
    public String created_at;
    public String shipping_address;
    public String delivery_address;
    public String payment_method;
    public String reference;

    // The list of item lines inside this order
    public List<OrderItemDto> items;


    // -------------------------
    // INNER CLASS: ORDER ITEM
    // -------------------------
    public static class OrderItemDto implements Serializable {

        private static final long serialVersionUID = 1L;

        public int product_id;
        public String name;
        public int qty;
        public double price;
    }
}
