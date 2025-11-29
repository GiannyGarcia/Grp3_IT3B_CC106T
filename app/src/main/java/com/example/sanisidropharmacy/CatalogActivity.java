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

        // Initialize Views
        menuIcon = findViewById(R.id.menu_icon);
        searchBar = findViewById(R.id.search_bar);
        cardPrescription = findViewById(R.id.card_prescription);
        cardNonPrescription = findViewById(R.id.card_non_prescription);
        cardNonIntake = findViewById(R.id.card_non_intake);
        cardDevice = findViewById(R.id.card_device);
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // Bottom navigation setup
        setupBottomNav(navHome, navCart, navUser);

        // Menu icon click
        menuIcon.setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        // Category clicks - Ensure category names match UserViewActivity.CATEGORIES exactly
        cardPrescription.setOnClickListener(v -> openCategory("Prescription Medicines"));
        cardNonPrescription.setOnClickListener(v -> openCategory("Non-Prescription Products")); // Corrected name
        cardNonIntake.setOnClickListener(v -> openCategory("Non-Intake Products"));
        cardDevice.setOnClickListener(v -> openCategory("Device Monitoring Products")); // Corrected name

        // Search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString().trim());
            }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    // Open selected category - NOW DIRECTS TO UserViewActivity
    private void openCategory(String categoryName) {
        Intent intent = new Intent(this, UserViewActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
    }

    // Search filtering - uses corrected names
    private void filterCategories(String query) {
        if (query.isEmpty()) return;

        switch (query.toLowerCase()) {
            case "prescription":
                openCategory("Prescription Medicines");
                break;
            case "non-prescription":
                openCategory("Non-Prescription Products");
                break;
            case "device":
                openCategory("Device Monitoring Products");
                break;
            case "non-intake":
                openCategory("Non-Intake Products");
                break;
            default:
                Toast.makeText(this, "No matching category found.", Toast.LENGTH_SHORT).show();
        }
    }

    // Bottom navigation helper
    private void setupBottomNav(ImageView home, ImageView cart, ImageView user) {
        home.setOnClickListener(v -> {
            // Always go to CatalogActivity
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cart.setOnClickListener(v -> {
            // Always go to CartActivity (You need to ensure CartActivity exists)
            Intent intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        user.setOnClickListener(v -> {
            // Always go to UserProfileActivity (You need to ensure UserProfileActivity exists)
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}