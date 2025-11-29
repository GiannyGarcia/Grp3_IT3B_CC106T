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
    private String description;

    // 1. Default no-argument constructor
    public Product() { }

    // 2. Full constructor (10 arguments)
    public Product(int id, String name, double price, int stock, String expiryDate,
                   String category, String brand, String prescriptionType, String imageUrl,
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

    // 3. Constructor (7 arguments)
    public Product(String name, int stock, String expiryDate, String description,
                   double price, String imageUriStr, String category) {
        this.id = 0;
        this.brand = "";
        this.prescriptionType = "";
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.category = category;
        this.imageUrl = imageUriStr;
        this.description = description;
    }

    // 4. ⭐ CORRECTED CONSTRUCTOR (9 arguments) to match MedicineDetailActivity call ⭐
    // Signature: (String ID, String name, String brand, String category, String description, double price, int stock, String imageUrl, boolean prescriptionRequired)
    public Product(String idStr,
                   String name,
                   String brand,
                   String category,
                   String description,
                   double price,
                   int stock,
                   String imageUrl,
                   boolean prescriptionRequired) {

        // Convert String ID to int field
        try {
            this.id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            this.id = 0;
        }

        // Map fields
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;

        // Convert boolean to String prescriptionType field
        this.prescriptionType = prescriptionRequired ? "Prescription Required" : "OTC";

        // Set expiryDate to null or default since it's not passed
        this.expiryDate = null;
    }


    // --- Getters and setters ---
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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // --- Convenience methods ---
    public boolean isPrescriptionRequired() {
        return prescriptionType != null && prescriptionType.equalsIgnoreCase("Prescription Required");
    }

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.isEmpty();
    }

    // --- Optional: a quick display string ---
    @Override
    public String toString() {
        return name + " (" + category + ") - ₱" + price;
    }
}