package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView navHome, navCart, navUser, btnMenu, fabAddProduct;
    private EditText searchBar;
    private TextView tvCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        // ----------------------------------------
        // UI BINDINGS
        // ----------------------------------------
        btnMenu = findViewById(R.id.btnMenu);
        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recyclerView);

        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        fabAddProduct = findViewById(R.id.fabAddProduct);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);

        // ----------------------------------------
        // FIXED: BOTTOM NAVIGATION
        // ----------------------------------------
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));

        // ----------------------------------------
        // FAB ADD PRODUCT (existing AddProductActivity)
        // ----------------------------------------
        fabAddProduct.setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));

        // ----------------------------------------
        // LOAD CATEGORY NAME FROM INTENT
        // ----------------------------------------
        String categoryName = getIntent().getStringExtra("categoryName");
        tvCategoryTitle.setText(categoryName);   // <-- fixes title sync

        loadCategoryItems(categoryName);
    }

    // -------------------------------------------
    // LOAD MEDICINES FOR THIS CATEGORY
    // -------------------------------------------
    private void loadCategoryItems(String category) {

        // FIXED: use SampleData (the real existing datasource)
        List<MedicineModel> medicineList = SampleData.getMedicinesByCategory(category);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // FIXED: Adapter now uses correct constructor
        MedicineAdapter adapter = new MedicineAdapter(
                medicineList,
                this,
                item -> {

                    Intent i = new Intent(CategoryDetailsActivity.this, MedicineDetailActivity.class);
                    i.putExtra("name", item.getName());
                    i.putExtra("price", item.getPrice());
                    i.putExtra("description", item.getDescription());
                    i.putExtra("dosage", item.getDosage());
                    i.putExtra("category", item.getCategory());
                    i.putExtra("stock", item.getStock());
                    i.putExtra("prescription", item.isPrescription());
                    i.putExtra("image", item.getImage()); // IMPORTANT FIX

                    startActivity(i);
                });

        recyclerView.setAdapter(adapter);
    }
}
