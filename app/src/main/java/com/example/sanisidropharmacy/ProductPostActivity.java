package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ProductPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_post);

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnAdminOrders = findViewById(R.id.btnAdminOrders);
        Button btnAdminLoyalty = findViewById(R.id.btnAdminLoyalty);
        Button btnInventory = findViewById(R.id.btnInventory);

        // Add Product
        btnAddProduct.setOnClickListener(v -> {
            startActivity(new Intent(this, AddProductActivity.class));
        });

        // View all orders (CRM)
        btnAdminOrders.setOnClickListener(v -> {
            Intent i = new Intent(this, OrderHistoryActivity.class);
            i.putExtra("showAll", true);   // ADMIN MODE
            startActivity(i);
        });

        // Loyalty stats
        btnAdminLoyalty.setOnClickListener(v -> {
            Intent i = new Intent(this, LoyaltyActivity.class);
            startActivity(i);
        });

        // Inventory Management
        btnInventory.setOnClickListener(v -> {
            startActivity(new Intent(this, InventoryTrackerActivity.class));
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);

        ImageView navHome = bottomNav.findViewById(R.id.nav_home);
        ImageView navCart = bottomNav.findViewById(R.id.nav_cart);
        ImageView navUser = bottomNav.findViewById(R.id.nav_user);

        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
