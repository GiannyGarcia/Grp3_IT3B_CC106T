package com.example.sanisidropharmacy;

public class Medicine {
    private int id;
    private String name;
    private String category;
    private double price;
    private boolean requiresPrescription;
    private int imageResId;

    public Medicine(int id, String name, String category, double price, boolean requiresPrescription, int imageResId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.requiresPrescription = requiresPrescription;
        this.imageResId = imageResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }


    public boolean isPrescriptionRequired() {
        return requiresPrescription;
    }

    public int getImageResId() { return imageResId; }
}
