package com.example.sanisidropharmacy;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityCategories extends AppCompatActivity {

    EditText searchBar;
    ImageView searchBtn, homeBtn, cartBtn, profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories); // link to your XML layout (activity_categories.xml)

        // Initialize views
        searchBar = findViewById(R.id.search_bar);
        searchBtn = findViewById(R.id.ic_search1);
        homeBtn = findViewById(R.id.ic_home);
        cartBtn = findViewById(R.id.ic_cart);
        profileBtn = findViewById(R.id.ic_user);

        // Search button click
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBar.getText().toString().trim();
                if (!query.isEmpty()) {
                    Toast.makeText(ActivityCategories.this, "Searching: " + query, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityCategories.this, "Enter something to search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Bottom navigation clicks
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityCategories.this, "Home Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityCategories.this, "Cart Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityCategories.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
