package com.example.sanisidropharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private EditText searchEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList = new ArrayList<>();

        // Add sample medicines (replace/add more as needed)
        medicineList.add(new Medicine("Biogesic", "₱5.00", R.drawable.ic_biogesic,
                "Pain reliever and fever reducer.", "1 tablet every 4-6 hours"));
        medicineList.add(new Medicine("Amoxicillin", "₱12.00", R.drawable.ic_amoxicillin,
                "Antibiotic for bacterial infections.", "500mg every 8 hours"));
        medicineList.add(new Medicine("Cough Syrup", "₱8.00", R.drawable.ic_cough_syrup,
                "Relieves cough symptoms.", "10ml 3 times a day"));
        medicineList.add(new Medicine("Metformin", "₱15.00", R.drawable.ic_metformin,
                "Used for type 2 diabetes management.", "500mg 2 times a day"));
        medicineList.add(new Medicine("Medicine Placeholder", "₱0.00", R.drawable.ic_medicine_placeholder,
                "Placeholder medicine description.", "Follow instructions"));

        // Add more prominent medicines in the Philippines
        medicineList.add(new Medicine("Neozep", "₱7.00", R.drawable.neozep,
                "Cold and flu relief.", "1 tablet every 4-6 hours"));
        medicineList.add(new Medicine("Kremil-S", "₱8.00", R.drawable.kremils,
                "Antacid for heartburn.", "2 tablets after meals"));
        medicineList.add(new Medicine("Bioflu", "₱10.00", R.drawable.bioflu,
                "Flu symptom relief.", "1 sachet 3 times a day"));
        medicineList.add(new Medicine("Alaxan FR", "₱12.00", R.drawable.alaxan,
                "Pain reliever and fever reducer.", "1 tablet every 6 hours"));
        medicineList.add(new Medicine("Solmux", "₱9.00", R.drawable.solmux,
                "Cough expectorant.", "10ml every 8 hours"));
        medicineList.add(new Medicine("Ascorbic Acid", "₱6.00", R.drawable.ascorbic_acid,
                "Vitamin C supplement.", "1 tablet daily"));
        medicineList.add(new Medicine("Enervon-C", "₱18.00", R.drawable.enervon_c,
                "Energy and vitamin supplement.", "1 tablet daily"));
        medicineList.add(new Medicine("Decolgen", "₱7.50", R.drawable.decolgen,
                "Cold and allergy relief.", "1 tablet every 6 hours"));
        medicineList.add(new Medicine("Ceelin", "₱10.00", R.drawable.ceelin,
                "Vitamin C for children.", "1 tablet daily"));
        medicineList.add(new Medicine("Tuseran", "₱8.50", R.drawable.tuseran,
                "Cough suppressant.", "10ml every 6 hours"));

        // Initialize adapter with click listener
        medicineAdapter = new MedicineAdapter(medicineList, medicine -> {
            Intent intent = new Intent(CatalogActivity.this, MedicineDetailActivity.class);
            intent.putExtra("image", medicine.getImageResId());
            intent.putExtra("name", medicine.getName());
            intent.putExtra("price", medicine.getPrice());
            intent.putExtra("description", medicine.getDescription());
            intent.putExtra("dosage", medicine.getDosage());
            startActivity(intent);
        });

        recyclerView.setAdapter(medicineAdapter);

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                medicineAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}
