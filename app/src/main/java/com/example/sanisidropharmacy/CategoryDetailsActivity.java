package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private FloatingActionButton fabAdd;
    private String categoryName;
    private ArrayList<Medicine> currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAddProduct);

        // Get category name from intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        setTitle(categoryName);

        // Load the correct list from DataStorage
        switch (categoryName) {
            case "Prescription Medicines":
                currentList = DataStorage.prescriptionList;
                break;
            case "Non-Prescription Medicines":
                currentList = DataStorage.nonPrescriptionList;
                break;
            case "Non-Intake Products":
                currentList = DataStorage.nonIntakeList;
                break;
            case "Device or Monitoring Products":
                currentList = DataStorage.deviceList;
                break;
            default:
                currentList = new ArrayList<>();
        }

        // Initialize RecyclerView
        adapter = new MedicineAdapter(this, currentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // FAB - Add new product
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryDetailsActivity.this, AddProductActivity.class);
            intent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when returning from AddProductActivity
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
