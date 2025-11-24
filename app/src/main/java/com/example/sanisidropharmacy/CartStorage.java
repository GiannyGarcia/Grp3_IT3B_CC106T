package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class CartStorage {

    private static final List<CartModel> cartList = new ArrayList<>();

    /** Add item to cart */
    public static void addToCart(CartModel newItem) {

        for (CartModel item : cartList) {
            if (item.getName().equals(newItem.getName())) {

                int newQty = item.getQty() + newItem.getQty();

                if (newQty > item.getStock()) newQty = item.getStock();

                item.setQty(newQty);
                return;
            }
        }

        cartList.add(newItem);
    }

    /** Get all cart items */
    public static List<CartModel> getCart() {
        return cartList;
    }

    /** Remove item */
    public static void removeItem(CartModel item) {
        cartList.remove(item);
    }

    /** Update quantity (+/-) */
    public static void updateQty(CartModel item, int newQty) {
        if (newQty < 1) newQty = 1;  // prevent zero or negative quantity
        if (newQty > item.getStock()) newQty = item.getStock();
        item.setQty(newQty);
    }

    /** Calculate total */
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
