package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class CartStorage {

    private static final List<CartModel> cartList = new ArrayList<>();

    /**
     * Adds an item to the cart. If the item already exists (same name + category + image),
     * merge quantities instead of creating duplicates.
     */
    public static void addToCart(CartModel newItem) {

        for (CartModel item : cartList) {
            if (item.equals(newItem)) {

                // If same product exists → increase qty
                int updatedQty = item.getQty() + newItem.getQty();

                // Cap by stock
                if (updatedQty > item.getStock()) {
                    updatedQty = item.getStock();
                }

                item.setQty(updatedQty);
                return; // Finished
            }
        }

        // If not found → add as new entry
        cartList.add(newItem);
    }

    /** Get complete cart */
    public static List<CartModel> getCart() {
        return cartList;
    }

    /** Clear cart */
    public static void clearCart() {
        cartList.clear();
    }

    /** Remove one item from cart */
    public static void removeItem(CartModel item) {
        cartList.remove(item);
    }

    /** Update quantity manually */
    public static void updateQty(CartModel item, int newQty) {
        if (newQty < 0) newQty = 0;
        if (newQty > item.getStock()) newQty = item.getStock();
        item.setQty(newQty);
    }

    /** Calculate total price of cart */
    public static double getTotalCost() {
        double total = 0;
        for (CartModel item : cartList) {
            try {
                total += Double.parseDouble(item.getPrice()) * item.getQty();
            } catch (Exception ignored) {}
        }
        return total;
    }
}
