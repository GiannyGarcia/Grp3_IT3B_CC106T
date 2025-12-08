package com.example.sanisidropharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recycler;
    OrderHistoryAdapter adapter;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_order_history);

        recycler = findViewById(R.id.recyclerOrderHistory);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNav();
        loadOrders();
    }

    private void loadOrders() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        int userId = prefs.getInt("session_user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getUserOrders(userId).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(OrderHistoryActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<OrderModel> list = convertToModel(response.body().getOrders());
                adapter = new OrderHistoryAdapter(list, OrderHistoryActivity.this);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------------------------------------------
    // DTO → ORDER MODEL CONVERSION
    // ------------------------------------------------------
    private List<OrderModel> convertToModel(List<OrderHistoryResponse.OrderItem> src) {
        List<OrderModel> out = new ArrayList<>();
        if (src == null) return out;

        for (OrderHistoryResponse.OrderItem dto : src) {

            OrderModel m = new OrderModel();

            setField(m, "id", dto.id);
            setField(m, "user_id", dto.user_id);
            setField(m, "total", dto.total);
            setField(m, "status", dto.status);
            setField(m, "created_at", dto.created_at);
            setField(m, "shipping_address", dto.shipping_address);
            setField(m, "delivery_address", dto.delivery_address);
            setField(m, "payment_method", dto.payment_method);
            setField(m, "reference", dto.reference);

            m.setItems(convertItems(dto.items));

            out.add(m);
        }

        return out;
    }

    private List<OrderHistoryResponse.OrderLine> convertItems(List<OrderHistoryResponse.OrderLine> src) {
        if (src == null) return new ArrayList<>();
        return src;
    }

    private void setField(OrderModel m, String name, Object value) {
        try {
            java.lang.reflect.Field f = OrderModel.class.getDeclaredField(name);
            f.setAccessible(true);
            f.set(m, value);
        } catch (Exception ignored) {}
    }

    // ------------------------------------------------------
    // BOTTOM NAV
    // ------------------------------------------------------
    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);
        bottomNav.findViewById(R.id.nav_home).setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));
        bottomNav.findViewById(R.id.nav_cart).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        bottomNav.findViewById(R.id.nav_user).setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
