package com.example.sanisidropharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    private static final String[] CATEGORIES = {
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
            startActivity(intent);
        });

        productAdapter = new ProductAdapter(this, allProductsList);
        recyclerView.setAdapter(productAdapter);

        setupCategoryFilters();

        String initialCategory = getIntent().getStringExtra("CATEGORY_NAME");
        if (initialCategory != null) {
            currentFilter = initialCategory;
        }

        loadAndRefresh();

        BottomNavHelper.setup(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndRefresh();
    }

    private void loadAndRefresh() {
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        allProductsList.clear();
        allProductsList.addAll(ProductManager.getInstance().getProducts());

        filterProducts(currentFilter);
    }

    private void setupCategoryFilters() {
        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> filterProducts("All Products"));

        for (String cat : CATEGORIES) {
            Button b = new Button(this);
            b.setText(cat);
            b.setOnClickListener(v -> filterProducts(cat));
            buttonLayout.addView(b);
        }
    }

    private void filterProducts(String filterCategory) {
        currentFilter = filterCategory;

        List<Product> filtered = new ArrayList<>();

        if (filterCategory.equals("All Products")) {
            filtered.addAll(allProductsList);
        } else {
            for (Product p : allProductsList) {
                if (p.getCategory() != null && p.getCategory().equals(filterCategory)) {
                    filtered.add(p);
                }
            }
        }

        productAdapter.setProductList(filtered);
        textViewProductCount.setText(filtered.size() + " items found");
    }
}
