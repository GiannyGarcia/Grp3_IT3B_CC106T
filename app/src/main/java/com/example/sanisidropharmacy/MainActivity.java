package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // link to XML layout

        // ✅ Initialize databases (keep this)
        DatabaseSetup.initializeDatabases(this);

        // ✅ Test MySQL connection
        Connection conn = DatabaseConnection.getMySQLConnection();
        if (conn != null) {
            System.out.println("Connected to MySQL successfully!");
        } else {
            System.out.println("Connection to MySQL failed!");
        }

        // ✅ Button reference
        continueBtn = findViewById(R.id.continueBtn);

        // ✅ FIX: Go to LoginActivity instead of CatalogActivity
        continueBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // prevents going back to main after login
        });
    }
}
