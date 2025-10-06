package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CatalogActivity extends AppCompatActivity {

    // Top Menu
    private ImageView menuIcon;

    // Search Bar
    private EditText searchBar;

    // Category Cards
    private CardView cardPrescription, cardNonPrescription, cardNonIntake, cardDevice;

    // Bottom Navigation
    private ImageView navHome, navCart, navUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure this layout name matches your XML file name (e.g., activity_catalog.xml)
        setContentView(R.layout.activity_catalog);

        // Top Menu
        menuIcon = findViewById(R.id.menu_icon);

        // Search bar
        searchBar = findViewById(R.id.search_bar);

        // Categories
        cardPrescription = findViewById(R.id.card_prescription);
        cardNonPrescription = findViewById(R.id.card_non_prescription);
        cardNonIntake = findViewById(R.id.card_non_intake);
        cardDevice = findViewById(R.id.card_device);

        // Bottom Navigation
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // 🔹 MENU ICON CLICK
        menuIcon.setOnClickListener(v -> {
            // For example, open a drawer or menu activity
            startActivity(new Intent(CatalogActivity.this, MenuActivity.class));
        });

        // 🔹 CATEGORY CLICKS
        cardPrescription.setOnClickListener(v -> openCategory("Prescription Medicines"));
        cardNonPrescription.setOnClickListener(v -> openCategory("Non-Prescription Medicines"));
        cardNonIntake.setOnClickListener(v -> openCategory("Non-Intake Products"));
        cardDevice.setOnClickListener(v -> openCategory("Device or Monitoring Products"));

        // 🔹 BOTTOM NAVIGATION
        navHome.setOnClickListener(v -> {
            // Example: navigate to Home
            startActivity(new Intent(CatalogActivity.this, HomeActivity.class));
            overridePendingTransition(0, 0);
        });

        navCart.setOnClickListener(v -> {
            // Example: navigate to Cart
            startActivity(new Intent(CatalogActivity.this, CartActivity.class));
            overridePendingTransition(0, 0);
        });

        navUser.setOnClickListener(v -> {
            // Example: navigate to User/Profile
            startActivity(new Intent(CatalogActivity.this, UserProfileActivity.class));
            overridePendingTransition(0, 0);
        });
    }

    // 🔹 Helper function for opening a category
    private void openCategory(String categoryName) {
        Intent intent = new Intent(CatalogActivity.this, CategoryDetailsActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
    }
}
