package com.example.sanisidropharmacy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoyaltyActivity extends AppCompatActivity {

    private TextView textPoints, textTier, textNextTier;
    private ProgressBar progressBar;
    private ImageView tierBadge;

    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty);

        initViews();
        loadLoyaltyPoints();
        setupBottomNav();
    }

    private void initViews() {
        textPoints = findViewById(R.id.textPoints);
        textTier = findViewById(R.id.textTier);
        textNextTier = findViewById(R.id.textNextTier);
        progressBar = findViewById(R.id.progressPoints);
        tierBadge = findViewById(R.id.tierBadge);
    }

    private void loadLoyaltyPoints() {

        int userId = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                .getInt("session_user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getLoyalty(userId).enqueue(new Callback<LoyaltyResponse>() {
            @Override
            public void onResponse(Call<LoyaltyResponse> call, Response<LoyaltyResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LoyaltyActivity.this, "Failed to load points", Toast.LENGTH_SHORT).show();
                    return;
                }

                int points = response.body().getPoints();
                textPoints.setText(points + " Points");

                updateTier(points);
            }

            @Override
            public void onFailure(Call<LoyaltyResponse> call, Throwable t) {
                Toast.makeText(LoyaltyActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTier(int points) {
        String tier;
        int nextTierPoints;
        int progressMax;

        if (points < 100) {
            tier = "Bronze";
            nextTierPoints = 100 - points;
            progressMax = 100;
            tierBadge.setImageResource(R.drawable.ic_bronze);
        }
        else if (points < 250) {
            tier = "Silver";
            nextTierPoints = 250 - points;
            progressMax = 250;
            tierBadge.setImageResource(R.drawable.ic_silver);
        }
        else if (points < 500) {
            tier = "Gold";
            nextTierPoints = 500 - points;
            progressMax = 500;
            tierBadge.setImageResource(R.drawable.ic_gold);
        }
        else {
            tier = "Platinum";
            nextTierPoints = 0;
            progressMax = points;
            tierBadge.setImageResource(R.drawable.ic_platinum);
        }

        textTier.setText("Tier: " + tier);

        if (tier.equals("Platinum")) {
            textNextTier.setText("You reached the highest tier!");
        } else {
            textNextTier.setText("Earn " + nextTierPoints + " more points to reach next tier");
        }

        progressBar.setMax(progressMax);
        progressBar.setProgress(points);
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);

        bottomNav.findViewById(R.id.nav_home).setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        bottomNav.findViewById(R.id.nav_cart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        bottomNav.findViewById(R.id.nav_user).setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
