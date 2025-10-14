package com.example.sanisidropharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private FloatingActionButton fabAdd;
    private String categoryName;
    private ArrayList<Medicine> currentList;
    private ImageView navHome, navCart, navUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAddProduct);
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // Setup bottom navigation
        setupBottomNav(navHome, navCart, navUser);

        // Get category name from intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        setTitle(categoryName);

        // Load the correct category list
        switch (categoryName) {
            case "Prescription Medicines": currentList = DataStorage.prescriptionList; break;
            case "Non-Prescription Medicines": currentList = DataStorage.nonPrescriptionList; break;
            case "Non-Intake Products": currentList = DataStorage.nonIntakeList; break;
            case "Device or Monitoring Products": currentList = DataStorage.deviceList; break;
            default: currentList = new ArrayList<>();
        }

        // Setup RecyclerView
        adapter = new MedicineAdapter(this, currentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // FloatingActionButton click — robust login+redirect logic
        fabAdd.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            boolean loggedIn = prefs.getBoolean("isLoggedIn", false);

            if (!loggedIn) {
                // Not logged in → go to LoginActivity, pass category for redirect
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra("REDIRECT_TO_ADD", true);
                loginIntent.putExtra("CATEGORY_NAME", categoryName);
                startActivity(loginIntent);

                // Close this activity to avoid stale state
                finish();
            } else {
                // Already logged in → go to AddProductActivity directly
                Intent addIntent = new Intent(this, AddProductActivity.class);
                addIntent.putExtra("CATEGORY_NAME", categoryName);
                startActivity(addIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
    }

    // Bottom navigation helper
    private void setupBottomNav(ImageView home, ImageView cart, ImageView user) {
        home.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}
