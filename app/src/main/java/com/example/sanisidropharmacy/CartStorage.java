package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class CartStorage {

    // Our temporary in-memory cart
    private static final List<CartItem> cartList = new ArrayList<>();

    // Add an item to cart
    public static void addToCart(String name, String price, String quantity, String imageUri, String category) {

        // Check if item already exists --> increase qty instead
        for (CartItem item : cartList) {
            if (item.getName().equals(name)) {
                int newQty = item.getQuantity() + Integer.parseInt(quantity);
                item.setQuantity(newQty);
                return;
            }
        }

        // Add new item
        cartList.add(new CartItem(
                name,
                price,
                Integer.parseInt(quantity),
                imageUri,
                category
        ));
    }

    // Return all cart items
    public static List<CartItem> getCartItems() {
        return cartList;
    }

    // Remove single item
    public static void removeItem(String name) {
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getName().equals(name)) {
                cartList.remove(i);
                break;
            }
        }
    }

    // Update quantity
    public static void updateQuantity(String name, int qty) {
        for (CartItem item : cartList) {
            if (item.getName().equals(name)) {
                item.setQuantity(qty);
                break;
            }
        }
    }

    // Get total price of all items
    public static double getTotalPrice() {
        double total = 0;

        for (CartItem item : cartList) {
            double price = Double.parseDouble(item.getPrice());
            total += price * item.getQuantity();
        }
        return total;
    }

    // Clear everything
    public static void clearCart() {
        cartList.clear();
    }
}
