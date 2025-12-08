package com.example.sanisidropharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {

    RecyclerView recycler;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_redeem);
        recycler = findViewById(R.id.recyclerRewards);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        setupBottomNav();
        loadRewards();
    }

    private void loadRewards() {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        api.getRewards().enqueue(new Callback<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {
                if (!response.isSuccessful() || response.body()==null) {
                    Toast.makeText(RedeemActivity.this, "Failed to load rewards", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<RewardsResponse.Reward> list = response.body().getRewards();
                recycler.setAdapter(new RewardsAdapter(list, RedeemActivity.this));
            }
            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                Toast.makeText(RedeemActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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
