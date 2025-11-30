package com.example.sanisidropharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private List<CartModel> cartList;

    private LinearLayout emptyLayout;
    private TextView textTotalPrice, txtTotalAmount;
    private Button btnProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        textTotalPrice = findViewById(R.id.textTotalPrice);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        emptyLayout = findViewById(R.id.emptyLayout);
        btnProceed = findViewById(R.id.btnProceedToCheckout);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        btnProceed.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    private void loadCart() {
        cartList = CartStorage.getCart(this);

        if (cartList.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerCart.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerCart.setVisibility(View.VISIBLE);
        }

        cartAdapter = new CartAdapter(cartList, this, this::updateTotalPrice);
        recyclerCart.setAdapter(cartAdapter);

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartModel item : cartList) {
            total += item.getTotalPrice();
        }

        textTotalPrice.setText(String.format("Total: ₱%.2f", total));
        txtTotalAmount.setText(String.format("₱%.2f", total));
    }

    private void setupBottomNav() {
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navCart = findViewById(R.id.nav_cart);
        ImageView navUser = findViewById(R.id.nav_user);

        navCart.setOnClickListener(v -> {});
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, UserViewActivity.class)));
        navUser.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
