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
    private String prescriptionType;
    private String imageUrl;

    // ✅ 1. Default no-argument constructor (needed for AddProductActivity)
    public Product() { }

    // ✅ 2. Full constructor (your original one — retained for compatibility)
    public Product(int id, String name, double price, int stock, String expiryDate,
                   String category, String brand, String prescriptionType, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.category = category;
        this.brand = brand;
        this.prescriptionType = prescriptionType;
        this.imageUrl = imageUrl;
    }

    // ✅ 3. Getters and setters (unchanged)
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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // ✅ 4. Convenience methods for easier data handling in adapters
    public boolean isPrescriptionRequired() {
        return prescriptionType != null && prescriptionType.equalsIgnoreCase("Prescription");
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.isEmpty();
    }

    // ✅ 5. Optional: a quick display string (for adapters or debugging)
    @Override
    public String toString() {
        return name + " (" + category + ") - ₱" + price;
    }
}
