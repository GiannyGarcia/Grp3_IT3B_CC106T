package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupLink;

    // Temporary in-memory user "database"
    private static HashMap<String, String> userDatabase = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userDatabase.containsKey(email)) {
                Toast.makeText(this, "Email not found. Please sign up first.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userDatabase.get(email).equals(password)) {
                Toast.makeText(this, "Incorrect password. Try again.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
            startActivity(intent);
            finish();
        });

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Allow SignUpActivity to add a new user
    public static void registerUser(String email, String password) {
        userDatabase.put(email, password);
    }
}
