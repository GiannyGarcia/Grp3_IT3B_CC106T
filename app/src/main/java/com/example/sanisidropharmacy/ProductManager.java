package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    // -------------------------------------------------------------
    // INITIALIZE WITH SAMPLE DATA ONLY ON FIRST RUN
    // -------------------------------------------------------------
    public void initialize(Context context) {
        if (!productList.isEmpty()) return;

        loadProductsFromSharedPreferences(context);

        if (productList.isEmpty()) {
            // Add default sample products only once
            addProduct(new Product(
                    0, "Paracetamol 500mg", 5.50, 100,
                    "12/31/2026", "Prescription Medicines", "Generic", "Prescription Required",
                    "", "Pain reliever for fever and mild to moderate pain."
            ));

            addProduct(new Product(
                    0, "Multivitamins Tab", 12.00, 50,
                    "08/01/2027", "Non-Prescription Products", "Brand X", "OTC",
                    "", "Essential vitamins and minerals for daily health."
            ));

            saveProductsToSharedPreferences(context);
        }
    }

    // -------------------------------------------------------------
    // LOAD & SAVE
    // -------------------------------------------------------------
    public void loadProductsFromSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PRODUCTS_LIST, null);

        if (json != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Product>>() {}.getType();
            productList = gson.fromJson(json, listType);
        } else {
            productList = new ArrayList<>();
        }

        // Determine next id
        int maxId = 0;
        for (Product p : productList) {
            if (p.getId() > maxId) maxId = p.getId();
        }
        nextId.set(maxId);
    }

    public void saveProductsToSharedPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(KEY_PRODUCTS_LIST, gson.toJson(productList));

        editor.apply();
    }

    // -------------------------------------------------------------
    // DATA MANIPULATION
    // -------------------------------------------------------------
    public void addProduct(Product product) {
        int newId = nextId.incrementAndGet();
        product.setId(newId);
        productList.add(product);
    }

    public List<Product> getProducts() {
        return productList;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : productList) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                filtered.add(p);
            }
        }
        return filtered;
    }
}
