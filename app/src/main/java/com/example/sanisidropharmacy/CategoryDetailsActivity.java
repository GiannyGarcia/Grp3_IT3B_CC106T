package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private String categoryName;
    private TextView tvCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        recyclerView = findViewById(R.id.recyclerView);

        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        tvCategoryTitle.setText(categoryName);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        loadCategoryProducts();

        // Floating Action Button click
        findViewById(R.id.fabAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            intent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(intent);
        });

        // Bottom Navigation setup
        View bottomNav = findViewById(R.id.include_bottom_nav);
        ImageView navHome = bottomNav.findViewById(R.id.nav_home);
        ImageView navCart = bottomNav.findViewById(R.id.nav_cart);
        ImageView navUser = bottomNav.findViewById(R.id.nav_user);

        navHome.setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));
        navCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        navUser.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }

    private void loadCategoryProducts() {
        List<Medicine> medicineList;

        switch (categoryName) {
            case "Prescription Medicines":
                medicineList = DataStorage.prescriptionList;
                break;
            case "Non-Prescription Medicines":
                medicineList = DataStorage.nonPrescriptionList;
                break;
            case "Non-Intake Products":
                medicineList = DataStorage.nonIntakeList;
                break;
            case "Device or Monitoring Products":
                medicineList = DataStorage.deviceList;
                break;
            default:
                medicineList = DataStorage.prescriptionList;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh UI after returning from AddProductActivity
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
