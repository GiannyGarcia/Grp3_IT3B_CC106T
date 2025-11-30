package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartStorage {

    private static final String PREFS_NAME = "CartPrefs";
    private static final String KEY_CART_LIST = "cartListJson";

    // Load cart
    public static List<CartModel> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CART_LIST, null);

        if (json == null) return new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<List<CartModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveCart(Context context, List<CartModel> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CART_LIST, new Gson().toJson(list)).apply();
    }

    // Add item
    public static void addItem(Context context, Product product, int qty) {
        List<CartModel> cart = getCart(context);

        for (CartModel item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + qty);
                saveCart(context, cart);
                return;
            }
        }

        cart.add(new CartModel(product, qty));
        saveCart(context, cart);
    }

    // Update qty
    public static void updateQuantity(Context context, CartModel updated) {
        List<CartModel> cart = getCart(context);

        for (CartModel item : cart) {
            if (item.getProduct().getId() == updated.getProduct().getId()) {
                item.setQuantity(updated.getQuantity());
                break;
            }
        }
        saveCart(context, cart);
    }

    // Remove item
    public static void removeItem(Context context, CartModel itemToRemove) {
        List<CartModel> cart = getCart(context);

        CartModel target = null;
        for (CartModel i : cart) {
            if (i.getProduct().getId() == itemToRemove.getProduct().getId()) {
                target = i;
                break;
            }
        }

        if (target != null) {
            cart.remove(target);
            saveCart(context, cart);
        }
    }

    // Clear cart
    public static void clearCart(Context context) {
        saveCart(context, new ArrayList<>());
    }
}
