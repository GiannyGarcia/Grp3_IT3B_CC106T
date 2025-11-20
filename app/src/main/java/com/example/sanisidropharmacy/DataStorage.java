package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    // ✅ Static lists that hold products per category
    private static final List<Product> prescriptionList = new ArrayList<>();
    private static final List<Product> nonPrescriptionList = new ArrayList<>();
    private static final List<Product> nonIntakeList = new ArrayList<>();
    private static final List<Product> deviceList = new ArrayList<>();
    private static final List<Product> allProducts = new ArrayList<>();

    // ✅ Getter methods used across activities
    public static List<Product> getPrescriptionList() { return prescriptionList; }
    public static List<Product> getNonPrescriptionList() { return nonPrescriptionList; }
    public static List<Product> getNonIntakeList() { return nonIntakeList; }
    public static List<Product> getDeviceList() { return deviceList; }
    public static List<Product> getAllProducts() { return allProducts; }

    // ✅ Optional helper: automatically sync product to correct category
    public static void addProduct(Product product) {
        if (product == null) return;

        switch (product.getCategory()) {
            case "Prescription Medicines":
                prescriptionList.add(product);
                break;
            case "Non-Prescription Medicines":
                nonPrescriptionList.add(product);
                break;
            case "Non-Intake Products":
                nonIntakeList.add(product);
                break;
            case "Device or Monitoring Products":
                deviceList.add(product);
                break;
            default:
                allProducts.add(product);
                break;
        }

        // keep master list synced
        if (!allProducts.contains(product)) {
            allProducts.add(product);
        }
    }
}
