package com.example.sanisidropharmacy;

import org.json.JSONException;
import org.json.JSONObject;

public class Medicine {

    private String name;
    private String category;
    private String price;
    private String description;
    private String dosage;
    private String imageUrl; // URI string from gallery
    private int stock;
    private boolean prescription;

    // Full constructor
    public Medicine(String name, String category, String price,
                    String description, String dosage,
                    String imageUrl, int stock, boolean prescription) {

        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.dosage = dosage;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.prescription = prescription;
    }

    // ✅ Constructor from JSON object
    public Medicine(JSONObject obj) {
        this.name = obj.optString("name", "");
        this.category = obj.optString("category", "");
        this.price = obj.optString("price", "0");
        this.description = obj.optString("description", "");
        this.dosage = obj.optString("dosage", "");
        this.imageUrl = obj.optString("imageUri", "");
        this.stock = obj.optInt("stock", 0);
        this.prescription = obj.optBoolean("prescription", false);
    }

    // ✅ Convert product to JSON (for saving updates later)
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("category", category);
            obj.put("price", price);
            obj.put("description", description);
            obj.put("dosage", dosage);
            obj.put("imageUri", imageUrl);
            obj.put("stock", stock);
            obj.put("prescription", prescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // ✅ Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getDosage() { return dosage; }
    public String getImageUrl() { return imageUrl; }
    public int getStock() { return stock; }
    public boolean isPrescription() { return prescription; }
}
