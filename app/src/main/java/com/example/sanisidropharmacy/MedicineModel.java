package com.example.sanisidropharmacy;

public class MedicineModel {

    private String name;
    private String description;
    private String price;
    private String category;
    private String image; // renamed from imageUrl to match adapter & cart
    private String dosage;
    private int stock;
    private boolean prescription;

    // Basic constructor
    public MedicineModel(String name, String description, String price, String category, String image) {
        this(name, description, price, category, image, "N/A", 0, false);
    }

    // Full constructor
    public MedicineModel(String name, String description, String price, String category,
                         String image, String dosage, int stock, boolean prescription) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.dosage = dosage;
        this.stock = stock;
        this.prescription = prescription;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImage() { return image; } // updated
    public String getDosage() { return dosage; }
    public int getStock() { return stock; }
    public boolean isPrescription() { return prescription; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(String price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImage(String image) { this.image = image; } // updated
    public void setDosage(String dosage) { this.dosage = dosage; }
    public void setStock(int stock) { this.stock = stock; }
    public void setPrescription(boolean prescription) { this.prescription = prescription; }
}
