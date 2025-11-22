package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityCatalogNIM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forward user into the unified catalog system
        Intent intent = new Intent(ActivityCatalogNIM.this, CategoryDetailsActivity.class);
        intent.putExtra("selectedCategory", "Non-Intake Products");
        startActivity(intent);

        // Close this activity so it does not stay in the back stack
        finish();
    }
}
