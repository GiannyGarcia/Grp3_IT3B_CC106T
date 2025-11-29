package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;
    TextView signupLink;

    RadioGroup roleGroup;
    RadioButton radioUser, radioAdmin;

    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        signupLink = findViewById(R.id.signupLink);

        roleGroup = findViewById(R.id.roleGroup);
        radioUser = findViewById(R.id.radioUser);
        radioAdmin = findViewById(R.id.radioAdmin);

        loginButton.setOnClickListener(v -> handleLogin());

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        String savedEmail = prefs.getString(email + "_email", null);
        String savedPassword = prefs.getString(email + "_password", null);
        String savedRole = prefs.getString(email + "_role", "User");

        // Check if user exists
        if (savedEmail == null) {
            Toast.makeText(this, "Account not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check password
        if (!savedPassword.equals(password)) {
            Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save active session
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("loggedInUser", email);
        editor.apply();

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

        // Redirect based on role
        if (savedRole.equals("Admin")) {
            startActivity(new Intent(this, ProductPostActivity.class));
        } else {
            startActivity(new Intent(this, CatalogActivity.class));
        }

        finish();
    }
}
