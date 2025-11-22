package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityCatalogNPM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forward user into the unified catalog screen
        Intent intent = new Intent(ActivityCatalogNPM.this, CategoryDetailsActivity.class);
        intent.putExtra("selectedCategory", "Non-Prescription Medicines");
        startActivity(intent);

        // Close this activity so it doesn't stay in history
        finish();
    }
}
