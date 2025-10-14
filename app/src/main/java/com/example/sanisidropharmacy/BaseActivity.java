package com.example.sanisidropharmacy;

import android.content.Intent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected void setupBottomNav(ImageView navHome, ImageView navCart, ImageView navUser) {

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        navCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        navUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}
