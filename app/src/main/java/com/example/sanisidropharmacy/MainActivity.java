package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // link to XML layout

        continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(v -> {
            // Example: move to LoginActivity after splash screen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
