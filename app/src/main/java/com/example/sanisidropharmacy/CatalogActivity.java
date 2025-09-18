package com.example.sanisidropharmacy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list
        medicineList = new ArrayList<>();
        loadMedicines();

        // Pass context + list
        medicineAdapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(medicineAdapter);
    }

    private void loadMedicines() {
        // Add sample medicines with drawable references
        medicineList.add(new Medicine("Biogesic", "₱5.00", R.drawable.biogesic));
        medicineList.add(new Medicine("Neozep", "₱7.00", R.drawable.neozep));
        medicineList.add(new Medicine("Kremil-S", "₱8.00", R.drawable.kremils));
        medicineList.add(new Medicine("Bioflu", "₱10.00", R.drawable.bioflu));
        medicineList.add(new Medicine("Alaxan FR", "₱12.00", R.drawable.alaxan));
    }
}
