package com.example.sanisidropharmacy;

// Represents one item in the cart with its quantity
public class CartModel {
    private Product product;
    private int quantity;

    public CartModel(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // You'll also need Product.java to be defined, but that's standard for your app.
}