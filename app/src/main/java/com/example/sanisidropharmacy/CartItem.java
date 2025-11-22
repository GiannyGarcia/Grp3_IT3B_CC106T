package com.example.sanisidropharmacy;

public class CartItem {

    private String name;
    private String price;
    private int quantity;
    private String imageUri;
    private String category;

    public CartItem(String name, String price, int quantity, String imageUri, String category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUri = imageUri;
        this.category = category;
    }

    public String getName() { return name; }

    public String getPrice() { return price; }

    public int getQuantity() { return quantity; }

    public String getImageUri() { return imageUri; }

    public String getCategory() { return category; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
