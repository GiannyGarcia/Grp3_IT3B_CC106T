package com.example.sanisidropharmacy;

import android.content.Intent;
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

                // Convert DTO → UI Model
                List<OrderModel> list = convertToModel(response.body().getOrders());

                adapter = new AdminOrdersAdapter(list, AdminOrdersActivity.this);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Toast.makeText(AdminOrdersActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------------------------------------------
    // DTO → MODEL CONVERSION
    // ------------------------------------------------------
    private List<OrderModel> convertToModel(List<OrderHistoryResponse.OrderItem> src) {
        List<OrderModel> out = new ArrayList<>();
        if (src == null) return out;

        for (OrderHistoryResponse.OrderItem dto : src) {

            OrderModel m = new OrderModel();

            // Map fields
            setField(m, "id", dto.id);
            setField(m, "user_id", dto.user_id);
            setField(m, "total", dto.total);
            setField(m, "status", dto.status);
            setField(m, "created_at", dto.created_at);
            setField(m, "shipping_address", dto.shipping_address);
            setField(m, "delivery_address", dto.delivery_address);
            setField(m, "payment_method", dto.payment_method);
            setField(m, "reference", dto.reference);

            // Map item list
            m.setItems(convertItems(dto.items));

            out.add(m);
        }

        return out;
    }

    // Convert DTO item list → UI OrderLine list
    private List<OrderHistoryResponse.OrderLine> convertItems(List<OrderHistoryResponse.OrderLine> src) {
        if (src == null) return new ArrayList<>();
        return src; // they match exactly, no conversion needed
    }

    // Reflection setter (your model uses private fields)
    private void setField(OrderModel m, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = OrderModel.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(m, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------
    // BOTTOM NAV
    // ------------------------------------------------------
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
