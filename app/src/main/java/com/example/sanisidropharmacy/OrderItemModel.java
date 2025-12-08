package com.example.sanisidropharmacy;

import java.io.Serializable;

public class OrderItemModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int product_id;
    private String name;
    private int qty;
    private double price;

    public int getProductId() { return product_id; }
    public String getName() { return name; }
    public int getQty() { return qty; }
    public double getPrice() { return price; }
}
