package com.example.sanisidropharmacy;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String name;
    private double price;
    private int stock;

    private String expiryDate;
    private String category;
    private String brand;
    private String prescriptionType;  // "Prescription Required" or "OTC"
    private String imageUrl;
    private String description;

    // --- Empty constructor for Gson ---
    public Product() {}

    // --- Main constructor used by ProductManager and AddProductActivity ---
    public Product(int id,
                   String name,
                   double price,
                   int stock,
                   String expiryDate,
                   String category,
                   String brand,
                   String prescriptionType,
                   String imageUrl,
                   String description) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.category = category;
        this.brand = brand;
        this.prescriptionType = prescriptionType;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // ---------------------------------------------------------
    // GETTERS & SETTERS
    // ---------------------------------------------------------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getPrescriptionType() { return prescriptionType; }
    public void setPrescriptionType(String prescriptionType) { this.prescriptionType = prescriptionType; }

    public boolean isPrescriptionRequired() {
        return "Prescription Required".equalsIgnoreCase(prescriptionType);
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // For debugging or display
    @Override
    public String toString() {
        return name + " - ₱" + price + " (" + category + ")";
    }
}
