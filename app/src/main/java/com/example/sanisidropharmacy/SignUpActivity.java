package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, birthDateInput;
    private Button signupButton;
    private TextView loginRedirect;

    private SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        birthDateInput = findViewById(R.id.birthDateInput);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        // Redirect to Login
        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        // Show date picker
        birthDateInput.setOnClickListener(v -> showDatePicker());

        // Handle sign up
        signupButton.setOnClickListener(v -> handleSignUp());
    }

    private void handleSignUp() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String birthDate = birthDateInput.getText().toString().trim();

        // Field validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email validation
        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Password validation
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Age validation
        if (!isAtLeast18(birthDate)) {
            Toast.makeText(this, "You must be at least 18 years old to register", Toast.LENGTH_SHORT).show();
            return;
        }

        // Duplicate check
        if (sharedPreferences.contains(email + "_password")) {
            Toast.makeText(this, "User already registered! Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Save user locally (persistent user record)
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email + "_name", name);
        editor.putString(email + "_email", email);
        editor.putString(email + "_password", password);
        editor.putString(email + "_birthdate", birthDate);

        // ✅ Create a session snapshot for immediate reflection in profile
        editor.putBoolean("isLoggedIn", true);
        editor.putString("loggedInUser", email);
        editor.putString("session_name", name);
        editor.putString("session_email", email);
        editor.putString("session_birthdate", birthDate);
        editor.apply();

        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

        // ✅ Go directly to CatalogActivity or Profile (choose one)
        Intent intent = new Intent(SignUpActivity.this, CatalogActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (DatePicker view, int y, int m, int d) -> {
            // Save as yyyy-MM-dd for DB compatibility
            String date = y + "-" + (m + 1) + "-" + d;
            birthDateInput.setText(date);
        }, year, month, day);

        dialog.show();
    }

    private boolean isAtLeast18(String birthDate) {
        try {
            // Format: yyyy-MM-dd
            String[] parts = birthDate.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);

            Calendar birthCal = Calendar.getInstance();
            birthCal.set(year, month, day);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);

            if (today.get(Calendar.MONTH) < birthCal.get(Calendar.MONTH) ||
                    (today.get(Calendar.MONTH) == birthCal.get(Calendar.MONTH) &&
                            today.get(Calendar.DAY_OF_MONTH) < birthCal.get(Calendar.DAY_OF_MONTH))) {
                age--;
            }

            return age >= 18;

        } catch (Exception e) {
            return false; // Invalid format
        }
    }
}
