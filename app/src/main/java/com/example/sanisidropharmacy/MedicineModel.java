package com.example.sanisidropharmacy;

public class MedicineModel {

    private String name;
    private String description;
    private String price;
    private String category;

    public MedicineModel() {
        // Default constructor (required for Firebase, if you use it later)
    }

    public MedicineModel(String name, String description, String price, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getPrice() { return price; }

    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setPrice(String price) { this.price = price; }

    public void setCategory(String category) { this.category = category; }
}
