package com.example.sanisidropharmacy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList; // Added import for ArrayList

public class InventoryTrackerActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInventory;
    private InventoryAdapter inventoryAdapter;
    // ⭐ IMPORTANT: Use ArrayList for a mutable list you can clear/add to. ⭐
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⭐ Assuming your layout name is R.layout.activity_inventory_tracker, not inventory_tracker_activity ⭐
        setContentView(R.layout.inventory_tracker_activity);

        recyclerViewInventory = findViewById(R.id.recyclerViewInventory);
        recyclerViewInventory.setLayoutManager(new LinearLayoutManager(this));

        // 1. Initialize the adapter with the empty (mutable) list
        inventoryAdapter = new InventoryAdapter(this, productList);
        recyclerViewInventory.setAdapter(inventoryAdapter);

        // 2. Load and refresh data immediately (will call the refresh logic)
        loadAndRefreshInventory();
    }

    // ⭐ FIX: Centralize the refresh logic into one method ⭐
    private void loadAndRefreshInventory() {
        // Load the latest data from persistence
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        // Get the fresh list from the manager
        List<Product> freshProducts = ProductManager.getInstance().getProducts();

        // Ensure the adapter is not null and the list reference is maintained
        if (inventoryAdapter != null) {
            // ⭐ CRITICAL FIX: Update the CONTENTS of the list, not the reference. ⭐
            this.productList.clear();
            this.productList.addAll(freshProducts);

            // Tell the adapter to redraw everything with the new contents
            inventoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Call the centralized method to ensure fresh data is always displayed
        loadAndRefreshInventory();
    }
}