package com.example.sanisidropharmacy;

public class CartModel {

    private String name, price, description, dosage, category, imageUrl;
    private int stock;
    private boolean prescription;
    private int quantity;

    public CartModel(String name, String price, String description, String dosage,
                     String category, int stock, boolean prescription,
                     String imageUrl, int quantity) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.dosage = dosage;
        this.category = category;
        this.stock = stock;
        this.prescription = prescription;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // getters only
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getDosage() { return dosage; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public boolean isPrescription() { return prescription; }
    public String getImageUrl() { return imageUrl; }
    public int getQuantity() { return quantity; }
}
