package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private Button addProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MedicineAdapter(this, DataStorage.medicineList);
        recyclerView.setAdapter(adapter);

        addProductButton = findViewById(R.id.addProductButton);
        addProductButton.setOnClickListener(v -> {
            startActivity(new Intent(CatalogActivity.this, AddProductActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Refresh list when returning
    }
}
