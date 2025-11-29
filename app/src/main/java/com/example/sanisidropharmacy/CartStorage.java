package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class CartStorage {

    // The static list holds the cart data across the application session.
    private static List<CartModel> cartItems = new ArrayList<>();

    // --- Core Methods needed by CartActivity/CartAdapter ---

    /**
     * Retrieves the current list of items in the cart.
     * @return The list of CartModel objects.
     */
    public static List<CartModel> getCart() {
        return cartItems;
    }

    /**
     * Calculates the total cost of all items in the cart.
     */
    public static double getTotalCost() {
        double total = 0;
        for (CartModel item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    // ⭐ METHOD 4: COMPLETED implementation for updating quantity ⭐
    /**
     * Finds a CartModel item and updates its quantity.
     * @param itemToUpdate The CartModel instance to update (from the adapter).
     * @param newQty The new quantity for the item.
     */
    public static void updateQuantity(CartModel itemToUpdate, int newQty) {
        if (newQty < 1) {
            // If the quantity drops to zero, we should remove the item.
            removeItem(itemToUpdate);
            return;
        }

        for (CartModel item : cartItems) {
            // Find the item by reference/equality
            if (item.equals(itemToUpdate)) {
                item.setQuantity(newQty);
                return;
            }
        }
    }

    // ⭐ METHOD 5: COMPLETED implementation for removing item ⭐
    /**
     * Removes a specific CartModel object from the cart list.
     * @param itemToRemove The CartModel instance to remove (from the adapter).
     */
    public static void removeItem(CartModel itemToRemove) {
        cartItems.remove(itemToRemove);
    }

    // ⭐ METHOD 6: REQUIRED for adding items from MedicineDetailActivity ⭐
    /**
     * Adds a product to the cart or increases the quantity if it already exists.
     * @param product The Product object to add.
     * @param quantity The amount to add (usually 1).
     */
    public static void addItem(Product product, int quantity) {
        if (product == null || quantity < 1) {
            return;
        }

        // Check if the product is already in the cart (compare by ID)
        for (CartModel item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                // Product exists: Update the quantity
                int newQty = item.getQuantity() + quantity;
                item.setQuantity(newQty);
                return;
            }
        }

        // Product is new: Create a new CartModel and add it
        CartModel newItem = new CartModel(product, quantity);
        cartItems.add(newItem);
    }

    // ⭐ NEW METHOD: FIX for CheckoutActivity compilation error (Line 135) ⭐
    /**
     * Clears all items from the shopping cart. Called when an order is placed.
     */
    public static void clearCart() {
        cartItems.clear();
    }
}