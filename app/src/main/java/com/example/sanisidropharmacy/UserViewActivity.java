package com.example.sanisidropharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;   // REQUIRED FOR FAB & details
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> allProductsList = new ArrayList<>();
    private LinearLayout buttonLayout;
    private TextView textViewProductCount;

    private FloatingActionButton fabAddProduct;

    private static final String[] CATEGORIES = new String[]{
            "Prescription Medicines",
            "Non-Prescription Products",
            "Non-Intake Products",
            "Device Monitoring Products"
    };

    private String currentFilter = "All Products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        ProductManager.getInstance().initialize(this);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonLayout = findViewById(R.id.layoutCategoryButtons);
        textViewProductCount = findViewById(R.id.textViewProductCount);

        fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(UserViewActivity.this, AddProductActivity.class);
            // If you want to prefill category from the currently selected filter:
            if (!"All Products".equals(currentFilter)) {
                intent.putExtra("CATEGORY_NAME", currentFilter);
            }
            startActivity(intent);
        });

        // Adapter initialization
        productAdapter = new ProductAdapter(this, allProductsList);

        // IMPORTANT: set click listener so clicking a product opens details
        productAdapter.setOnItemClickListener(product -> {
            Intent i = new Intent(UserViewActivity.this, MedicineDetailActivity.class);
            i.putExtra("id", product.getId());
            i.putExtra("name", product.getName());
            i.putExtra("brand", product.getBrand());
            i.putExtra("category", product.getCategory());
            i.putExtra("description", product.getDescription());
            i.putExtra("dosage", "");
            i.putExtra("expiryDate", product.getExpiryDate());
            i.putExtra("image", product.getImageUrl());
            i.putExtra("price", String.valueOf(product.getPrice()));
            i.putExtra("stock", product.getStock());
            i.putExtra("prescription", product.isPrescriptionRequired());
            startActivity(i);
        });

        recyclerView.setAdapter(productAdapter);

        setupCategoryFilters();

        String initialCategory = getIntent().getStringExtra("CATEGORY_NAME");
        if (initialCategory != null && !initialCategory.isEmpty()) {
            currentFilter = initialCategory;
        }

        loadAndRefreshUserView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndRefreshUserView();
    }

    private void loadAndRefreshUserView() {
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        List<Product> freshProducts = ProductManager.getInstance().getProducts();

        if (productAdapter != null) {
            this.allProductsList.clear();
            if (freshProducts != null) {
                this.allProductsList.addAll(freshProducts);
            }
            filterProducts(currentFilter);
        }
    }

    private void setupCategoryFilters() {
        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> filterProducts("All Products"));

        for (String categoryName : CATEGORIES) {
            Button btn = createCategoryButton(categoryName);
            buttonLayout.addView(btn);
        }
    }

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

    private void highlightButton(String selectedCategory) {
        Button btnAll = findViewById(R.id.btnAll);

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

    private void filterProducts(String filterCategory) {
        currentFilter = filterCategory;
        List<Product> filteredList = new ArrayList<>();

        if ("All Products".equals(filterCategory)) {
            filteredList.addAll(allProductsList);
        } else {
            for (Product product : allProductsList) {
                if (product.getCategory() != null &&
                        product.getCategory().equals(filterCategory)) {
                    filteredList.add(product);
                }
            }
        }

        productAdapter.setProductList(filteredList);

        if (textViewProductCount != null) {
            textViewProductCount.setText(String.format("%d items found", filteredList.size()));
        }

        highlightButton(filterCategory);
    }
}
