package com.example.sanisidropharmacy;

public class Medicine {
    private String name;
    private String description;
    private String manufacturer;
    private double price;
    private String expiryDate;
    private String category;
    private String prescriptionType;
    private int stock;
    private String imageUrl;

    // ✅ Full constructor with all attributes
    public Medicine(String name, String description, String manufacturer, double price,
                    String expiryDate, String category, String prescriptionType, String imageUrl) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.expiryDate = expiryDate;
        this.category = category;
        this.prescriptionType = prescriptionType;
        this.stock = 0; // Default stock
        this.imageUrl = imageUrl;
    }

    // ✅ Optional: simple constructor for testing
    public Medicine(String name, double price, int stock, String expiryDate,
                    String category, String manufacturer, String imageUrl) {
        this.name = name;
        this.description = "";
        this.manufacturer = manufacturer;
        this.price = price;
        this.expiryDate = expiryDate;
        this.category = category;
        this.prescriptionType = "N/A";
        this.stock = stock;
        this.imageUrl = imageUrl;
    }


    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getManufacturer() { return manufacturer; }
    public double getPrice() { return price; }
    public String getExpiryDate() { return expiryDate; }
    public String getCategory() { return category; }
    public String getPrescriptionType() { return prescriptionType; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setPrice(double price) { this.price = price; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setCategory(String category) { this.category = category; }
    public void setPrescriptionType(String prescriptionType) { this.prescriptionType = prescriptionType; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
