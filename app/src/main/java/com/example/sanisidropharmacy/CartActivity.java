package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    private ImageView navHome, navCart, navUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize bottom nav
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // Setup bottom nav functionality
        setupBottomNav(navHome, navCart, navUser);
    }

    // Helper method for bottom navigation
    private void setupBottomNav(ImageView home, ImageView cart, ImageView user) {
        home.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CatalogActivity.class);
            // Always go back to catalog
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cart.setOnClickListener(v -> {
            // Already in CartActivity, but just in case
            Intent intent = new Intent(CartActivity.this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
