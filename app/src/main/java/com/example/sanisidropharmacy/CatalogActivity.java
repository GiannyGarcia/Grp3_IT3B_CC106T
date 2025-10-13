package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
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
        setContentView(R.layout.activity_catalog);

        // 🔹 Initialize Views
        menuIcon = findViewById(R.id.menu_icon);
        searchBar = findViewById(R.id.search_bar);
        cardPrescription = findViewById(R.id.card_prescription);
        cardNonPrescription = findViewById(R.id.card_non_prescription);
        cardNonIntake = findViewById(R.id.card_non_intake);
        cardDevice = findViewById(R.id.card_device);
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // 🔹 MENU ICON CLICK
        menuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, MenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // 🔹 CATEGORY CLICKS — open each category
        cardPrescription.setOnClickListener(v -> openCategory("Prescription Medicines"));
        cardNonPrescription.setOnClickListener(v -> openCategory("Non-Prescription Medicines"));
        cardNonIntake.setOnClickListener(v -> openCategory("Non-Intake Products"));
        cardDevice.setOnClickListener(v -> openCategory("Device or Monitoring Products"));

        // 🔹 BOTTOM NAVIGATION
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, CartActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        navUser.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, UserProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // 🔹 SEARCH BAR FUNCTIONALITY
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    // 🔹 Helper function for opening a category
    private void openCategory(String categoryName) {
        Intent intent = new Intent(CatalogActivity.this, CategoryDetailsActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    // 🔹 Simple filtering — show Toast for demo purpose
    private void filterCategories(String query) {
        if (query.isEmpty()) {
            // Nothing typed
            return;
        }

        if (query.equalsIgnoreCase("prescription")) {
            openCategory("Prescription Medicines");
        } else if (query.equalsIgnoreCase("non-prescription")) {
            openCategory("Non-Prescription Medicines");
        } else if (query.equalsIgnoreCase("device")) {
            openCategory("Device or Monitoring Products");
        } else if (query.equalsIgnoreCase("non-intake")) {
            openCategory("Non-Intake Products");
        } else {
            Toast.makeText(this, "No matching category found.", Toast.LENGTH_SHORT).show();
        }
    }
}
