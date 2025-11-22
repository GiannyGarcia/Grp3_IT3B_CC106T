package com.example.sanisidropharmacy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model representing an item in the cart.
 * Fields kept identical to your original design.
 */
public class CartModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String price;
    private String description;
    private String dosage;
    private String category;
    private int stock;
    private boolean prescription;
    private String image;
    private int qty;

    public CartModel(String name, String price, String description, String dosage,
                     String category, int stock, boolean prescription,
                     String image, int qty) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.dosage = dosage;
        this.category = category;
        this.stock = stock;
        this.prescription = prescription;
        this.image = image;
        this.qty = Math.max(0, qty);
    }

    // --- Getters ---
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getDosage() { return dosage; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public boolean isPrescription() { return prescription; }
    public String getImage() { return image; }
    public int getQty() { return qty; }

    // --- Setters where sensible ---
    public void setQty(int qty) { this.qty = Math.max(0, qty); }

    // Convenience helpers
    public void incrementQty() {
        if (qty < stock) this.qty++;
    }

    public void decrementQty() {
        if (qty > 0) this.qty--;
    }

    // Useful for merging items in cart: define identity (adjust if needed)
    // Currently uses name + category + image as identity
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartModel)) return false;
        CartModel that = (CartModel) o;
        return Objects.equals(name, that.name)
                && Objects.equals(category, that.category)
                && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, image);
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", qty=" + qty +
                ", category='" + category + '\'' +
                ", stock=" + stock +
                ", prescription=" + prescription +
                '}';
    }
}
