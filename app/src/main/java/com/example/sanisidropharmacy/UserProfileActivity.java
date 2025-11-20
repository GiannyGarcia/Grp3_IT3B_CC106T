package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail, userBirthdate;
    private ImageView profileImage;
    private Button editProfileButton, logoutButton;

    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // ✅ Initialize views
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userBirthdate = findViewById(R.id.userBirthdate);
        profileImage = findViewById(R.id.profileImage);
        editProfileButton = findViewById(R.id.btnEditProfile);
        logoutButton = findViewById(R.id.btnLogout);

        // ✅ Load user data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String name = prefs.getString("session_name", "Guest User");
        String email = prefs.getString("session_email", "guest@example.com");
        String birthdate = prefs.getString("session_birthdate", "N/A");
        String imageUri = prefs.getString("session_imageUri", null);

        // ✅ Display stored values
        userName.setText(name);
        userEmail.setText(email);
        userBirthdate.setText("Birthdate: " + birthdate);

        if (imageUri != null && !imageUri.isEmpty()) {
            try {
                profileImage.setImageURI(Uri.parse(imageUri));
            } catch (Exception e) {
                profileImage.setImageResource(R.drawable.ic_user_profile);
            }
        } else {
            profileImage.setImageResource(R.drawable.ic_user_profile);
        }

        // ✅ Edit Profile placeholder
        editProfileButton.setOnClickListener(v ->
                Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show()
        );

        // ✅ Logout: clear session + redirect to MainActivity
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // ✅ Keep bottom navigation accessible
        setupBottomNav();
    }

    // ✅ Bottom navigation setup (consistent with all activities)
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
                    overridePendingTransition(0, 0);
                    finish();
                });
            }

            if (navCart != null) {
                navCart.setOnClickListener(v -> {
                    Intent intent = new Intent(UserProfileActivity.this, CartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                });
            }

            if (navUser != null) {
                Toast.makeText(this, "You're already viewing your profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
