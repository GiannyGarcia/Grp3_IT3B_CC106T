package com.example.sanisidropharmacy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.ArrayList;

public class InventoryTrackerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInventory;
    private InventoryAdapter inventoryAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_tracker_activity);

        recyclerViewInventory = findViewById(R.id.recyclerViewInventory);
        recyclerViewInventory.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        inventoryAdapter = new InventoryAdapter(this, productList);
        recyclerViewInventory.setAdapter(inventoryAdapter);

        // Load initial data
        loadAndRefreshInventory();

        // ⭐ ADD FAB LOGIC BELOW ⭐
        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryTrackerActivity.this, ProductPostActivity.class);
            startActivity(intent);
        });
    }

    // Refresh inventory
    private void loadAndRefreshInventory() {
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        List<Product> freshProducts = ProductManager.getInstance().getProducts();

        if (inventoryAdapter != null) {
            productList.clear();
            productList.addAll(freshProducts);
            inventoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndRefreshInventory();
    }
}
