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

        int userId = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                .getInt("session_user_id", -1);

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

                // Convert response DTO → actual OrderModel objects
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

    private List<OrderModel> convertToModel(List<OrderHistoryResponse.OrderItem> apiList) {
        List<OrderModel> out = new ArrayList<>();

        for (OrderHistoryResponse.OrderItem o : apiList) {
            OrderModel m = new OrderModel();
            assignFields(m, o);
            out.add(m);
        }

        return out;
    }

    private void assignFields(OrderModel m, OrderHistoryResponse.OrderItem o) {
        try {
            java.lang.reflect.Field f;

            f = OrderModel.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(m, o.id);

            f = OrderModel.class.getDeclaredField("user_id");
            f.setAccessible(true);
            f.set(m, o.user_id);

            f = OrderModel.class.getDeclaredField("total");
            f.setAccessible(true);
            f.set(m, o.total);

            f = OrderModel.class.getDeclaredField("status");
            f.setAccessible(true);
            f.set(m, o.status);

            f = OrderModel.class.getDeclaredField("created_at");
            f.setAccessible(true);
            f.set(m, o.created_at);

            f = OrderModel.class.getDeclaredField("shipping_address");
            f.setAccessible(true);
            f.set(m, o.shipping_address);

            f = OrderModel.class.getDeclaredField("delivery_address");
            f.setAccessible(true);
            f.set(m, o.delivery_address);

            f = OrderModel.class.getDeclaredField("payment_method");
            f.setAccessible(true);
            f.set(m, o.payment_method);

            f = OrderModel.class.getDeclaredField("reference");
            f.setAccessible(true);
            f.set(m, o.reference);

            m.setItems(o.items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);

        bottomNav.findViewById(R.id.nav_home)
                .setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));

        bottomNav.findViewById(R.id.nav_cart)
                .setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        bottomNav.findViewById(R.id.nav_user)
                .setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
