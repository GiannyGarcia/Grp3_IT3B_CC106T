package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersActivity extends AppCompatActivity {

    RecyclerView recycler;
    AdminOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_admin_orders);

        recycler = findViewById(R.id.recyclerOrders);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNav();
        loadAllOrders();
    }

    private void loadAllOrders() {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        api.getAllOrders().enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(AdminOrdersActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<OrderDto> dtoList = response.body().getOrders();
                adapter = new AdminOrdersAdapter(dtoList, AdminOrdersActivity.this);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(AdminOrdersActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);
        if (bottomNav == null) return;

        bottomNav.findViewById(R.id.nav_home).setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));
        bottomNav.findViewById(R.id.nav_cart).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        bottomNav.findViewById(R.id.nav_user).setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
