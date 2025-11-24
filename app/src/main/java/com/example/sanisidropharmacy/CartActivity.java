package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView txtTotal, emptyMessage;
    private LinearLayout emptyLayout;

    private ImageView navHome, navCart, navUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // UI Bindings
        recyclerCart = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotalAmount);
        emptyMessage = findViewById(R.id.emptyMessage);
        emptyLayout = findViewById(R.id.emptyLayout);

        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        loadCartUI();
        setupBottomNav();
    }

    private void loadCartUI() {
        List<CartModel> cartItems = CartStorage.getCart();

        if (cartItems.isEmpty()) {
            recyclerCart.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            txtTotal.setText("₱0.00");
            return;
        }

        // Show list
        recyclerCart.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);

        CartAdapter adapter = new CartAdapter(cartItems, this, updatedItem -> {
            updateTotalPrice();
        });

        recyclerCart.setAdapter(adapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = CartStorage.getTotalCost();
        txtTotal.setText("₱" + total);
    }

    // -------------------------------
    // Bottom Navigation
    // -------------------------------
    private void setupBottomNav() {

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, CatalogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        navCart.setOnClickListener(v -> {
            // Already here
        });

        navUser.setOnClickListener(v -> {
            Intent i = new Intent(this, UserProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });
    }
}
