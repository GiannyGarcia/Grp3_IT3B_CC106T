package com.example.sanisidropharmacy;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    private static final List<MedicineModel> medicineList = new ArrayList<>();

    static {
        // -------------------------
        // SAMPLE DATA (edit as needed)
        // -------------------------

        medicineList.add(new MedicineModel(
                "Biogesic",
                "Paracetamol for fever and pain relief",
                "5.00",
                "OTC",
                "https://i.imgur.com/hQ5ZxSm.png",
                "500mg every 4-6 hours",
                120,
                false
        ));

        medicineList.add(new MedicineModel(
                "Amoxicillin",
                "Antibiotic for bacterial infections",
                "12.00",
                "Prescription",
                "https://i.imgur.com/vB7qmXW.png",
                "250–500mg every 8 hours",
                40,
                true
        ));

        medicineList.add(new MedicineModel(
                "Neozep",
                "Cold and flu medicine",
                "7.00",
                "OTC",
                "https://i.imgur.com/oKZrT0U.png",
                "1 tablet every 6 hours",
                90,
                false
        ));

        medicineList.add(new MedicineModel(
                "Ventolin",
                "Asthma reliever inhaler",
                "250.00",
                "Prescription",
                "https://i.imgur.com/cedKzcX.png",
                "2 puffs every 4-6 hours",
                25,
                true
        ));

        medicineList.add(new MedicineModel(
                "Vitamin C",
                "Ascorbic acid supplement",
                "3.00",
                "Vitamins",
                "https://i.imgur.com/ak6fX34.png",
                "500mg daily",
                200,
                false
        ));
    }

    // -----------------------------
    // RETURN ALL MEDICINES
    // -----------------------------
    public static List<MedicineModel> getAllMedicines() {
        return medicineList;
    }

    // -----------------------------
    // FILTER BY CATEGORY
    // -----------------------------
    public static List<MedicineModel> getMedicinesByCategory(String category) {

        List<MedicineModel> filtered = new ArrayList<>();

        for (MedicineModel m : medicineList) {
            if (m.getCategory().equalsIgnoreCase(category)) {
                filtered.add(m);
            }
        }

        return filtered;
    }
}
