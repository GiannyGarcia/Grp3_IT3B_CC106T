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
    private Button editProfileButton, logoutButton;
    private Button btnTestCRM;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userBirthdate = findViewById(R.id.userBirthdate);
        profileImage = findViewById(R.id.profileImage);
        editProfileButton = findViewById(R.id.btnEditProfile);
        logoutButton = findViewById(R.id.btnLogout);
        btnTestCRM = findViewById(R.id.btnTestCRM);

        // CRM test
        btnTestCRM.setOnClickListener(v -> testCRM());

        loadUserData();

        // 👉 Open Edit Profile Activity
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        setupBottomNav();
    }

    // ---------------- CRM TEST FUNCTIONS ----------------

    private void testCRM() {

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("session_user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        // 1) TEST: createOrder
        JsonObject order = new JsonObject();
        order.addProperty("user_id", userId);
        order.addProperty("total", 150.00);
        order.addProperty("payment_method", "GCash");
        order.addProperty("delivery_address", "Test Address");
        order.addProperty("reference", "TESTREF999");

        JsonArray items = new JsonArray();

        JsonObject item1 = new JsonObject();
        item1.addProperty("product_id", 1);
        item1.addProperty("name", "Test Product");
        item1.addProperty("qty", 2);
        item1.addProperty("price", 75.00);
        items.add(item1);

        order.add("items", items);

        RequestBody body = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                order.toString()
        );


        api.createOrder(body).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                Log.d("CRM", "createOrder → " + new Gson().toJson(response.body()));
                Toast.makeText(UserProfileActivity.this, "createOrder OK", Toast.LENGTH_SHORT).show();

                // When OK → fetch order history
                testGetOrders(userId);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.e("CRM", "createOrder ERROR: " + t.getMessage());
            }
        });
    }

    private void testGetOrders(int userId) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getUserOrders(userId).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                Log.d("CRM", "getUserOrders → " + new Gson().toJson(response.body()));
                Toast.makeText(UserProfileActivity.this, "getUserOrders OK", Toast.LENGTH_SHORT).show();

                // next, test loyalty update
                testUpdateLoyalty(userId);
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Log.e("CRM", "getUserOrders ERROR: " + t.getMessage());
            }
        });
    }

    private void testUpdateLoyalty(int userId) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.updateLoyalty(userId, 5).enqueue(new Callback<LoyaltyResponse>() {
            @Override
            public void onResponse(Call<LoyaltyResponse> call, Response<LoyaltyResponse> response) {
                Log.d("CRM", "updateLoyalty → " + new Gson().toJson(response.body()));
                Toast.makeText(UserProfileActivity.this, "updateLoyalty OK", Toast.LENGTH_SHORT).show();

                // final → fetch loyalty
                testGetLoyalty(userId);
            }

            @Override
            public void onFailure(Call<LoyaltyResponse> call, Throwable t) {
                Log.e("CRM", "updateLoyalty ERROR: " + t.getMessage());
            }
        });
    }

    private void testGetLoyalty(int userId) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.getLoyalty(userId).enqueue(new Callback<LoyaltyResponse>() {
            @Override
            public void onResponse(Call<LoyaltyResponse> call, Response<LoyaltyResponse> response) {
                Log.d("CRM", "getLoyalty → " + new Gson().toJson(response.body()));
                Toast.makeText(UserProfileActivity.this, "getLoyalty OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LoyaltyResponse> call, Throwable t) {
                Log.e("CRM", "getLoyalty ERROR: " + t.getMessage());
            }
        });
    }

    // ---------------- END CRM TEST FUNCTIONS ----------------

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        String name = prefs.getString("session_name", "Guest User");
        String email = prefs.getString("session_email", "guest@example.com");
        String birthdate = prefs.getString("session_birthdate", "N/A");
        String imageUri = prefs.getString("session_imageUri", null);

        userName.setText(name);
        userEmail.setText(email);
        userBirthdate.setText("Birthdate: " + birthdate);

        if (imageUri != null) {
            profileImage.setImageURI(Uri.parse(imageUri));
        } else {
            profileImage.setImageResource(R.drawable.ic_user_profile);
        }
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.include_bottom_nav);

        if (bottomNav != null) {
            ImageView navHome = bottomNav.findViewById(R.id.nav_home);
            ImageView navCart = bottomNav.findViewById(R.id.nav_cart);
            ImageView navUser = bottomNav.findViewById(R.id.nav_user);

            if (navHome != null) {
                navHome.setOnClickListener(v -> {
                    Intent intent = new Intent(UserProfileActivity.this, CatalogActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            if (navCart != null) {
                navCart.setOnClickListener(v -> {
                    Intent intent = new Intent(UserProfileActivity.this, CartActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            if (navUser != null) {
                Toast.makeText(this, "You're already viewing your profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
