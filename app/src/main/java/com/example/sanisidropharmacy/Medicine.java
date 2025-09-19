package com.example.sanisidropharmacy;

public class Medicine {
    private String name;
    private String price;
    private int imageResId;
    private String description;
    private String dosage;

    public Medicine(String name, String price, int imageResId, String description, String dosage) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.description = description;
        this.dosage = dosage;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
    public String getDosage() { return dosage; }
}
