package com.example.sanisidropharmacy; // adapt if needed

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CatalogByTypeActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";

    private RecyclerView recyclerView;
    private CatalogAdapter adapter;
    private FloatingActionButton fab;
    private String categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_generic);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        fab = findViewById(R.id.fabAddProduct);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CATEGORY_NAME)) {
            categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        } else {
            categoryName = "";
        }

        // set title text if present
        findViewById(R.id.title_text).setVisibility(android.view.View.VISIBLE);
        // set the title text
        ((android.widget.TextView)findViewById(R.id.title_text)).setText(categoryName);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        loadProductsForCategory();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogByTypeActivity.this, AddProductActivity.class);
            intent.putExtra(AddProductActivity.EXTRA_CATEGORY_NAME, categoryName);
            startActivity(intent);
        });
    }

    private void loadProductsForCategory() {
        List<Product> productList;
        switch (categoryName) {
            case "Prescription Medicines":
                productList = DataStorage.getPrescriptionList();
                break;
            case "Non-Prescription Medicines":
                productList = DataStorage.getNonPrescriptionList();
                break;
            case "Non-Intake Products":
                productList = DataStorage.getNonIntakeList();
                break;
            case "Device or Monitoring Products":
                productList = DataStorage.getDeviceList();
                break;
            default:
                productList = DataStorage.getAllProducts();
                break;
        }
        adapter = new CatalogAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
