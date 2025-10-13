package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private FloatingActionButton fabAddProduct;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category); // XML for the category details UI

        categoryName = getIntent().getStringExtra("category_name");
        setTitle(categoryName);

        recyclerView = findViewById(R.id.recyclerView);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicineList = new ArrayList<>();

        // Example data for testing
        medicineList.add(new Medicine("Paracetamol", 10.0, 50, "2025-12-12", categoryName, "Biogesic", ""));
        medicineList.add(new Medicine("Ibuprofen", 15.0, 40, "2026-03-10", categoryName, "Advil", ""));

        medicineAdapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(medicineAdapter);

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, AddProductActivity.class);
            intent.putExtra("category_name", categoryName);
            startActivity(intent);
        });
    }
}
