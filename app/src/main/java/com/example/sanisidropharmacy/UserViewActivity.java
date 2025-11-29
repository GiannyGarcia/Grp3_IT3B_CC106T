package com.example.sanisidropharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView; // ⭐ NEW IMPORT for product count
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    // CRITICAL: Initialize with an empty, mutable list reference
    private List<Product> allProductsList = new ArrayList<>();
    private LinearLayout buttonLayout;
    private TextView textViewProductCount; // ⭐ NEW: Product count TextView

    private static final String[] CATEGORIES = new String[]{
            "Prescription Medicines",
            "Non-Prescription Products",
            "Non-Intake Products",
            "Device Monitoring Products"
    };

    // Store the currently active filter to re-apply it after refreshing the data
    private String currentFilter = "All Products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        // 1. Initialize the singleton and load persistent data from disk
        ProductManager.getInstance().initialize(this);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonLayout = findViewById(R.id.layoutCategoryButtons);
        // ⭐ NEW: Initialize the product count TextView
        textViewProductCount = findViewById(R.id.textViewProductCount);

        // 2. Initialize the adapter with the empty, mutable list reference.
        productAdapter = new ProductAdapter(this, allProductsList);
        recyclerView.setAdapter(productAdapter);

        // 3. Set up the category filter buttons
        setupCategoryFilters();

        // 4. Check for initial filter from Intent
        String initialCategory = getIntent().getStringExtra("CATEGORY_NAME");
        if (initialCategory != null && !initialCategory.isEmpty()) {
            currentFilter = initialCategory;
        }

        // 5. Load and refresh data (this calls the initial filter and highlight)
        loadAndRefreshUserView();
    }

    // ⬇️ CRITICAL FIX: Add onResume to reload data whenever the activity becomes visible. ⬇️
    @Override
    protected void onResume() {
        super.onResume();
        // This ensures the view is always updated with the latest stock/price changes from the admin.
        loadAndRefreshUserView();
    }

    /**
     * Loads the latest data from the ProductManager (disk), updates the core list,
     * and reapplies the currently active filter.
     */
    private void loadAndRefreshUserView() {
        // 1. Load the latest data from persistence
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        // 2. Get the fresh list (which is the internal list reference from ProductManager)
        List<Product> freshProducts = ProductManager.getInstance().getProducts();

        if (productAdapter != null) {
            // 3. CRITICAL: Update the CONTENTS of the list, not the reference.
            this.allProductsList.clear();
            this.allProductsList.addAll(freshProducts);

            // 4. Re-apply the last known filter to the refreshed list
            filterProducts(currentFilter);
        }
    }


    // --- setupCategoryFilters (Unchanged) ---
    private void setupCategoryFilters() {
        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> filterProducts("All Products"));

        for (String categoryName : CATEGORIES) {
            Button btn = createCategoryButton(categoryName);
            buttonLayout.addView(btn);
        }
    }

    // --- createCategoryButton (Unchanged) ---
    private Button createCategoryButton(String categoryName) {
        Button btn = new Button(this);
        btn.setText(categoryName);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        btn.setLayoutParams(params);

        btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        btn.setOnClickListener(v -> filterProducts(categoryName));

        return btn;
    }

    // --- highlightButton (Unchanged) ---
    private void highlightButton(String selectedCategory) {
        Button btnAll = findViewById(R.id.btnAll);

        // Reset colors
        btnAll.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View v = buttonLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }

        int highlightColor = getResources().getColor(android.R.color.holo_green_dark);

        if ("All Products".equals(selectedCategory)) {
            btnAll.setBackgroundColor(highlightColor);
            setTitle("All Products");
        } else {
            for (int i = 0; i < buttonLayout.getChildCount(); i++) {
                View v = buttonLayout.getChildAt(i);
                if (v instanceof Button) {
                    Button btn = (Button) v;
                    if (btn.getText().toString().equals(selectedCategory)) {
                        btn.setBackgroundColor(highlightColor);
                        break;
                    }
                }
            }
            setTitle(selectedCategory);
        }
    }


    // --- filterProducts (Fixed and Updated for count display) ---
    private void filterProducts(String filterCategory) {
        // Update the state
        currentFilter = filterCategory;

        List<Product> filteredList = new ArrayList<>();

        if ("All Products".equals(filterCategory)) {
            // When filtering for All Products, use the full list
            filteredList.addAll(allProductsList);
        } else {
            // Filter the full list based on the category
            for (Product product : allProductsList) {
                if (product.getCategory() != null && product.getCategory().equals(filterCategory)) {
                    filteredList.add(product);
                }
            }
        }

        // ⭐ FIX: Removed redundant and incorrect conditional logic. ⭐
        // Simply set the new list and let the adapter notify the change.
        productAdapter.setProductList(filteredList);

        // ⭐ NEW: Update the product count TextView
        if (textViewProductCount != null) {
            textViewProductCount.setText(String.format("%d items found", filteredList.size()));
        }

        highlightButton(filterCategory);
    }
}