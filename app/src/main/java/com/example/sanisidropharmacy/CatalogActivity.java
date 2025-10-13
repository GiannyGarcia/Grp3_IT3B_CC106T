package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private TextView emptyText;
    private EditText searchEditText;
    private FloatingActionButton addProductFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog); // new integrated XML

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MedicineAdapter(this, DataStorage.medicineList);
        recyclerView.setAdapter(adapter);

        // Empty message handling
        emptyText = findViewById(R.id.emptyText);
        toggleEmptyMessage();

        // Search (optional: simple filter)
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(String text) {
                adapter.filter(text);
                toggleEmptyMessage();
            }
        });

        // FloatingActionButton for adding new product
        addProductFab = findViewById(R.id.addProductFab);
        addProductFab.setOnClickListener(v -> {
            startActivity(new Intent(CatalogActivity.this, AddProductActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Refresh list when returning
        toggleEmptyMessage();
    }

    // Show/hide "No products available" text
    private void toggleEmptyMessage() {
        if (DataStorage.medicineList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
