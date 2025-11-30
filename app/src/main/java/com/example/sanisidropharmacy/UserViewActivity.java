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

    private String currentFilter = "All Products";

    private static final String[] CATEGORIES = {
            "Prescription Medicines",
            "Non-Prescription Products",
            "Non-Intake Products",
            "Device Monitoring Products"
    };

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

        productAdapter = new ProductAdapter(this, allProductsList);
        recyclerView.setAdapter(productAdapter);

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(UserViewActivity.this, AddProductActivity.class);
            intent.putExtra("CATEGORY_NAME", currentFilter);
            startActivity(intent);
        });

        setupCategoryButtons();

        String initialCategory = getIntent().getStringExtra("CATEGORY_NAME");
        if (initialCategory != null) {
            currentFilter = initialCategory;
        }

        loadAndRefreshUserView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndRefreshUserView();
    }

    // -------------------------------------------------------------
    // LOAD + FILTER
    // -------------------------------------------------------------
    private void loadAndRefreshUserView() {
        ProductManager.getInstance().loadProductsFromSharedPreferences(this);

        allProductsList.clear();
        allProductsList.addAll(ProductManager.getInstance().getProducts());

        filterProducts(currentFilter);
    }

    // -------------------------------------------------------------
    // CATEGORY BUTTONS
    // -------------------------------------------------------------
    private void setupCategoryButtons() {
        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setOnClickListener(v -> filterProducts("All Products"));

        for (String c : CATEGORIES) {
            Button b = createCategoryButton(c);
            buttonLayout.addView(b);
        }
    }

    private Button createCategoryButton(String name) {
        Button btn = new Button(this);
        btn.setText(name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginEnd(8);
        btn.setLayoutParams(params);

        btn.setBackgroundColor(getColor(android.R.color.darker_gray));
        btn.setOnClickListener(v -> filterProducts(name));

        return btn;
    }

    // -------------------------------------------------------------
    // FILTERING
    // -------------------------------------------------------------
    private void filterProducts(String category) {
        currentFilter = category;

        List<Product> filtered = new ArrayList<>();

        if (category.equals("All Products")) {
            filtered.addAll(allProductsList);
        } else {
            for (Product p : allProductsList) {
                if (p.getCategory() != null && p.getCategory().equals(category)) {
                    filtered.add(p);
                }
            }
        }

        productAdapter.setProductList(filtered);
        textViewProductCount.setText(filtered.size() + " items found");

        highlightButton(category);
    }

    private void highlightButton(String selected) {
        Button btnAll = findViewById(R.id.btnAll);
        btnAll.setBackgroundColor(getColor(android.R.color.darker_gray));

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View v = buttonLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setBackgroundColor(getColor(android.R.color.darker_gray));
            }
        }

        int highlight = getColor(android.R.color.holo_green_dark);

        if (selected.equals("All Products")) {
            btnAll.setBackgroundColor(highlight);
            return;
        }

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View v = buttonLayout.getChildAt(i);
            if (v instanceof Button) {
                Button b = (Button) v;
                if (b.getText().toString().equals(selected)) {
                    b.setBackgroundColor(highlight);
                    break;
                }
            }
        }
    }
}
