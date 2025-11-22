package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final List<MedicineModel> prescriptionList = new ArrayList<>();
    private static final List<MedicineModel> nonPrescriptionList = new ArrayList<>();
    private static final List<MedicineModel> nonIntakeList = new ArrayList<>();
    private static final List<MedicineModel> deviceList = new ArrayList<>();

    // Return lists (live)
    public static List<MedicineModel> getPrescriptionList() { return prescriptionList; }
    public static List<MedicineModel> getNonPrescriptionList() { return nonPrescriptionList; }
    public static List<MedicineModel> getNonIntakeList() { return nonIntakeList; }
    public static List<MedicineModel> getDeviceList() { return deviceList; }

    // Helper to add product by category name
    public static void addProduct(String category, MedicineModel item) {
        if (category == null || item == null) return;
        switch (category.toLowerCase()) {
            case "prescription medicines":
            case "prescription":
                prescriptionList.add(item);
                break;
            case "non-prescription medicines":
            case "non-prescription":
                nonPrescriptionList.add(item);
                break;
            case "non-intake products":
            case "non-intake":
                nonIntakeList.add(item);
                break;
            case "device or monitoring products":
            case "device":
            case "monitoring":
                deviceList.add(item);
                break;
            default:
                // fallback: put into nonPrescriptionList
                nonPrescriptionList.add(item);
                break;
        }
    }

    // Convenience: get by category string
    public static List<MedicineModel> getProductsForCategory(String category) {
        if (category == null) return new ArrayList<>();
        switch (category.toLowerCase()) {
            case "prescription medicines":
            case "prescription":
                return prescriptionList;
            case "non-prescription medicines":
            case "non-prescription":
                return nonPrescriptionList;
            case "non-intake products":
            case "non-intake":
                return nonIntakeList;
            case "device or monitoring products":
            case "device":
            case "monitoring":
                return deviceList;
            default:
                return new ArrayList<>();
        }
    }
}
