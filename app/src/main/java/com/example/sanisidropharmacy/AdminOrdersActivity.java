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

                // Convert API DTO → UI model
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

    private List<OrderModel> convertToModel(List<OrderHistoryResponse.OrderItem> apiList) {
        List<OrderModel> out = new ArrayList<>();

        for (OrderHistoryResponse.OrderItem o : apiList) {

            OrderModel m = new OrderModel();

            // Assign fields (OrderModel uses snake_case internally)
            assign(m, o);

            out.add(m);
        }

        return out;
    }

    private void assign(OrderModel m, OrderHistoryResponse.OrderItem o) {
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

            m.setItems(o.items); // setter exists

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
