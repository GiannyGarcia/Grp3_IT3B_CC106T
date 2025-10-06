package com.example.sanisidropharmacy;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        String category = getIntent().getStringExtra("CATEGORY_NAME");
        TextView categoryTitle = findViewById(R.id.category_title);
        if (categoryTitle != null) {
            categoryTitle.setText(category);
        }
    }
}
