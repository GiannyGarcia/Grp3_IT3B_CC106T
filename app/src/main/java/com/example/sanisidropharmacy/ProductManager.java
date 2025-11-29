package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Singleton class to manage the list of products.
 * Handles in-memory storage and persistence via SharedPreferences using Gson.
 */
public class ProductManager {

    private static ProductManager instance;
    private List<Product> productList;
    private static final String PREFS_NAME = "ProductPrefs";
    private static final String KEY_PRODUCTS_LIST = "productListJson";
    private AtomicInteger nextId;

    private ProductManager() {
        productList = new ArrayList<>();
        nextId = new AtomicInteger(0);
    }

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    // ==========================================================
    // CORE PERSISTENCE LOGIC (Unchanged - Already Correct)
    // ==========================================================

    public void initialize(Context context) {
        if (productList.isEmpty()) {
            loadProductsFromSharedPreferences(context);

            if (productList.isEmpty()) {
                addProduct(new Product(
                        0, "Paracetamol 500mg", 5.50, 100,
                        "12/31/2026", "Prescription Medicines", "Generic",
                        "Non-Prescription", "", // Image URL
                        "Pain reliever for fever and mild to moderate pain."
                ));
                addProduct(new Product(
                        0, "Multivitamins Tab", 12.00, 50,
                        "08/01/2027", "Non-Prescription Products", "Brand X",
                        "Non-Prescription", "", // Image URL
                        "Essential vitamins and minerals for daily health."
                ));
                saveProductsToSharedPreferences(context);
            }
        }
    }

    public void loadProductsFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PRODUCTS_LIST, null);

        productList.clear();

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            productList = gson.fromJson(json, type);

            int maxId = 0;
            for (Product p : productList) {
                if (p.getId() > maxId) {
                    maxId = p.getId();
                }
            }
            nextId.set(maxId);
        } else {
            productList = new ArrayList<>();
            nextId.set(0);
        }
    }

    public void saveProductsToSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(productList);

        editor.putString(KEY_PRODUCTS_LIST, json);
        editor.apply();
    }

    // ==========================================================
    // DATA MANIPULATION (FIXED)
    // ==========================================================

    public void addProduct(Product product) {
        int newId = nextId.incrementAndGet();
        product.setId(newId);
        productList.add(product);
    }

    /**
     * Retrieves the entire list of products.
     * ⭐ FIX: Returns the internal list reference, ensuring all callers work on the same data. ⭐
     */
    public List<Product> getProducts() {
        return productList;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equals(category)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }
}