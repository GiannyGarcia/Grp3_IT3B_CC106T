package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; // ⭐ NEW IMPORT ⭐
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast; // ⭐ NEW IMPORT ⭐

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView txtTotal, emptyMessage;
    private LinearLayout emptyLayout;
    private Button btnCheckout; // ⭐ NEW: Checkout Button Variable ⭐

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
        btnCheckout = findViewById(R.id.btnProceedToCheckout); // ⭐ NEW: Bind the checkout button ⭐

        // Assuming these IDs are correctly defined in include_bottom_nav.xml
        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        setupCheckoutButton(); // ⭐ NEW: Setup checkout logic ⭐
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartUI();
    }

    private void loadCartUI() {
        List<CartModel> cartItems = CartStorage.getCart();

        if (cartItems == null || cartItems.isEmpty()) {
            recyclerCart.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            // Hide the checkout button when the cart is empty
            btnCheckout.setVisibility(View.GONE);
            txtTotal.setText(String.format("₱%.2f", 0.00));
            return;
        }

        // Show list and button
        recyclerCart.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        btnCheckout.setVisibility(View.VISIBLE); // Show the checkout button

        CartAdapter adapter = new CartAdapter(cartItems, this, () -> updateTotalPrice());

        recyclerCart.setAdapter(adapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = CartStorage.getTotalCost();
        txtTotal.setText(String.format("₱%.2f", total));
    }

    // -------------------------------
    // ⭐ NEW: Checkout Button Setup ⭐
    // -------------------------------
    private void setupCheckoutButton() {
        btnCheckout.setOnClickListener(v -> {
            if (CartStorage.getCart().isEmpty()) {
                Toast.makeText(this, "Your cart is empty. Add items to checkout.", Toast.LENGTH_SHORT).show();
            } else {
                // Launch the CheckoutActivity
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    // -------------------------------
    // Bottom Navigation
    // -------------------------------
    private void setupBottomNav() {
        // ... (Navigation setup remains the same)

        navHome.setOnClickListener(v -> {
            Intent i = new Intent(this, CatalogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        navCart.setOnClickListener(v -> {
            // Already here, no action needed
        });

        navUser.setOnClickListener(v -> {
            Intent i = new Intent(this, UserProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });
    }
}