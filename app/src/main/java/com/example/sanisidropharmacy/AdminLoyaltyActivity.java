package com.example.sanisidropharmacy;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoyaltyActivity extends AppCompatActivity {

    TextView tvTotalCustomers, tvTotalPoints;
    RecyclerView recyclerTop;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_admin_loyalty);
        tvTotalCustomers = findViewById(R.id.tvTotalCustomers);
        tvTotalPoints = findViewById(R.id.tvTotalPoints);
        recyclerTop = findViewById(R.id.recyclerTopCustomers);
        recyclerTop.setLayoutManager(new LinearLayoutManager(this));
        setupBottomNav();
        loadSummary();
    }

    private void loadSummary() {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        api.getAdminLoyaltySummary().enqueue(new Callback<AdminLoyaltySummaryResponse>() {
            @Override
            public void onResponse(Call<AdminLoyaltySummaryResponse> call, Response<AdminLoyaltySummaryResponse> response) {
                if (!response.isSuccessful() || response.body()==null) {
                    Toast.makeText(AdminLoyaltyActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                AdminLoyaltySummaryResponse r = response.body();
                tvTotalCustomers.setText("Customers: " + r.getTotal_customers());
                tvTotalPoints.setText("Total Points: " + r.getTotal_points());
                recyclerTop.setAdapter(new TopCustomerAdapter(r.getTop_customers()));
            }
            @Override
            public void onFailure(Call<AdminLoyaltySummaryResponse> call, Throwable t) {
                Toast.makeText(AdminLoyaltyActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);
        bottomNav.findViewById(R.id.nav_home).setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));
        bottomNav.findViewById(R.id.nav_cart).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        bottomNav.findViewById(R.id.nav_user).setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
