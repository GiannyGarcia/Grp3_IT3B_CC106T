package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail, userBirthdate;
    private ImageView profileImage;
    private Button editProfileButton, logoutButton, btnTestCRM;

    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initViews();
        loadUserData();
        setupButtons();
        setupBottomNav();
    }

    // --------------------------------------------------------------------
    // INITIALIZE UI COMPONENTS
    // --------------------------------------------------------------------
    private void initViews() {
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userBirthdate = findViewById(R.id.userBirthdate);
        profileImage = findViewById(R.id.profileImage);
        editProfileButton = findViewById(R.id.btnEditProfile);
        logoutButton = findViewById(R.id.btnLogout);
        btnTestCRM = findViewById(R.id.btnTestCRM);
    }

    private void setupButtons() {

        // VIEW ORDERS
        Button btnViewOrders = findViewById(R.id.btnViewOrders);
        btnViewOrders.setOnClickListener(v ->
                startActivity(new Intent(UserProfileActivity.this, OrderHistoryActivity.class)));

        // VIEW LOYALTY
        Button btnLoyalty = findViewById(R.id.btnLoyalty);
        btnLoyalty.setOnClickListener(v ->
                startActivity(new Intent(UserProfileActivity.this, LoyaltyActivity.class)));

        // EDIT PROFILE
        editProfileButton.setOnClickListener(v ->
                startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class)));

        // LOGOUT
        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // CRM DEBUG BUTTON
        btnTestCRM.setOnClickListener(v -> testCRM());
    }

    // --------------------------------------------------------------------
    // CRM TEST SEQUENCE (Order → Order List → Loyalty Update → Loyalty Fetch)
    // --------------------------------------------------------------------
    private void testCRM() {

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        int userId = prefs.getInt("session_user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        // -------------------------
        // Build Valid Test Order
        // -------------------------
        JsonObject order = new JsonObject();
        order.addProperty("user_id", userId);
        order.addProperty("total", 150.00);
        order.addProperty("payment_method", "GCash");
        order.addProperty("delivery_address", "Test Address");
        order.addProperty("shipping_address", "Test Address");
        order.addProperty("reference", "TESTREF999");

        // MUST MATCH MYSQL PRODUCT IDs
        JsonArray items = new JsonArray();

        JsonObject item = new JsonObject();
        item.addProperty("product_id", 1); // <-- VALID PRODUCT ID
        item.addProperty("name", "Test Product");
        item.addProperty("qty", 2);
        item.addProperty("price", 75.00);

        items.add(item);
        order.add("items", items);

        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                order.toString()
        );

        // -------------------------
        // API CALL
        // -------------------------
        api.createOrder(body).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(UserProfileActivity.this, "createOrder FAILED", Toast.LENGTH_SHORT).show();
                    return;
                }

                OrderResponse res = response.body();

                if (!res.isSuccess()) {
                    Toast.makeText(UserProfileActivity.this, "Server Error: " + res.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(UserProfileActivity.this,
                        "Order Created! ID = " + res.getOrderId(),
                        Toast.LENGTH_SHORT).show();

                testGetOrders(userId);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void testGetOrders(int userId) {

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getUserOrders(userId).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {

                if (response.body() == null) {
                    Log.e("CRM", "getUserOrders ERROR: Null body");
                    return;
                }

                OrderHistoryResponse res = response.body();
                Log.d("CRM", "getUserOrders → " + new Gson().toJson(res));

                Toast.makeText(UserProfileActivity.this,
                        "Orders Found: " + res.getOrders().size(),
                        Toast.LENGTH_SHORT).show();

                testUpdateLoyalty(userId);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Log.e("CRM", "getUserOrders failure → " + t.getMessage());
            }
        });
    }

    private void testUpdateLoyalty(int userId) {

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.updateLoyalty(userId, 5).enqueue(new Callback<LoyaltyResponse>() {
            @Override
            public void onResponse(Call<LoyaltyResponse> call, Response<LoyaltyResponse> response) {

                if (response.body() == null) {
                    Log.e("CRM", "updateLoyalty ERROR: Null body");
                    return;
                }

                LoyaltyResponse res = response.body();
                Log.d("CRM", "updateLoyalty → " + new Gson().toJson(res));

                Toast.makeText(UserProfileActivity.this,
                        "Loyalty Updated (+5 pts)",
                        Toast.LENGTH_SHORT).show();

                testGetLoyalty(userId);
            }

            @Override
            public void onFailure(Call<LoyaltyResponse> call, Throwable t) {
                Log.e("CRM", "updateLoyalty failure → " + t.getMessage());
            }
        });
    }

    private void testGetLoyalty(int userId) {

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getLoyalty(userId).enqueue(new Callback<LoyaltyResponse>() {
            @Override
            public void onResponse(Call<LoyaltyResponse> call, Response<LoyaltyResponse> response) {

                if (response.body() == null) {
                    Log.e("CRM", "getLoyalty ERROR: Null body");
                    return;
                }

                LoyaltyResponse res = response.body();
                Log.d("CRM", "getLoyalty → " + new Gson().toJson(res));

                Toast.makeText(UserProfileActivity.this,
                        "Current Points: " + res.getPoints(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoyaltyResponse> call, Throwable t) {
                Log.e("CRM", "getLoyalty failure → " + t.getMessage());
            }
        });
    }

    // --------------------------------------------------------------------
    // LOAD USER INFO
    // --------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, MODE_PRIVATE);

        userName.setText(prefs.getString("session_name", "Guest User"));
        userEmail.setText(prefs.getString("session_email", "guest@example.com"));
        userBirthdate.setText("Birthdate: " + prefs.getString("session_birthdate", "N/A"));

        String imageUri = prefs.getString("session_imageUri", null);
        if (imageUri != null) profileImage.setImageURI(Uri.parse(imageUri));
        else profileImage.setImageResource(R.drawable.ic_user_profile);
    }

    // --------------------------------------------------------------------
    // BOTTOM NAVIGATION
    // --------------------------------------------------------------------
    private void setupBottomNav() {
        LinearLayout bottom = findViewById(R.id.include_bottom_nav);
        if (bottom == null) return;

        bottom.findViewById(R.id.nav_home).setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        bottom.findViewById(R.id.nav_cart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        bottom.findViewById(R.id.nav_user).setOnClickListener(v ->
                Toast.makeText(this, "Already on Profile", Toast.LENGTH_SHORT).show());
    }
}
