package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCatalogPM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forward user into the unified catalog screen
        Intent intent = new Intent(ActivityCatalogPM.this, CategoryDetailsActivity.class);
        intent.putExtra("selectedCategory", "Prescription Medicines");
        startActivity(intent);

        // Close this activity so it doesn't stay in history
        finish();
    }
}
