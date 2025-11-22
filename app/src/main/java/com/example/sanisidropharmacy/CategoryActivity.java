package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the selected category
        String categoryName = getIntent().getStringExtra("category_name");
        if (categoryName == null) categoryName = "";

        // Forward user to the unified CategoryDetailsActivity
        Intent intent = new Intent(CategoryActivity.this, CategoryDetailsActivity.class);
        intent.putExtra("selectedCategory", categoryName);
        startActivity(intent);

        // Finish so this activity doesn't stay in back stack
        finish();
    }
}
