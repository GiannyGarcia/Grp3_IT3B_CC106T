package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class ProductStorage {

    private static final List<MedicineModel> productList = new ArrayList<>();

    public static void addProduct(MedicineModel product) {
        productList.add(product);
    }

    public static List<MedicineModel> getAllProducts() {
        return productList;
    }

    public static List<MedicineModel> getMedicinesByCategory(String category) {
        List<MedicineModel> filtered = new ArrayList<>();
        for (MedicineModel m : productList) {
            if (m.getCategory().equalsIgnoreCase(category)) {
                filtered.add(m);
            }
        }
        return filtered;
    }
}
