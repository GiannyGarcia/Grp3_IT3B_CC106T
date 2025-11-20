package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupLink, forgotPasswordLink;

    private SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!isValidInput(email, password)) return;

            // Retrieve saved password
            String savedPassword = sharedPreferences.getString(email + "_password", null);

            if (savedPassword == null) {
                Toast.makeText(this, "Account not found. Please sign up.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!savedPassword.equals(password)) {
                Toast.makeText(this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Login successful
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Save login session + user profile snapshot
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("loggedInUser", email);

// Try to retrieve stored profile fields saved at sign up (keys are: email + "_name", etc.)
            String savedName = sharedPreferences.getString(email + "_name", null);
            String savedPhone = sharedPreferences.getString(email + "_phone", null);
            String savedImageUri = sharedPreferences.getString(email + "_imageUri", null);

// If signup did not save a name, derive a friendly username from the email
            if (savedName == null || savedName.trim().isEmpty()) {
                String username = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
                savedName = username;
            }

// Save a simple session snapshot that other screens will read
            editor.putString("session_name", savedName);
            editor.putString("session_email", email);
            if (savedPhone != null) editor.putString("session_phone", savedPhone);
            if (savedImageUri != null) editor.putString("session_imageUri", savedImageUri);

            editor.apply();


            // Handle optional redirect to AddProductActivity
            boolean redirectToAdd = getIntent().getBooleanExtra("REDIRECT_TO_ADD", false);
            String categoryName = getIntent().getStringExtra("CATEGORY_NAME");

            Intent nextIntent;
            if (redirectToAdd && categoryName != null) {
                nextIntent = new Intent(this, AddProductActivity.class);
                nextIntent.putExtra("CATEGORY_NAME", categoryName);
            } else {
                nextIntent = new Intent(this, CatalogActivity.class);
            }
            startActivity(nextIntent);
            finish();
        });

        // Handle signup link click
        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Handle forgot password click
        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private boolean isValidInput(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
