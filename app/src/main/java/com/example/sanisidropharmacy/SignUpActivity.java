package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        birthDateInput = findViewById(R.id.birthDateInput);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        loginRedirect.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        birthDateInput.setOnClickListener(v -> showDatePicker());

        signupButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String birthDate = birthDateInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || birthDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@")) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register user in temporary database
            LoginActivity.registerUser(email, password);

            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (DatePicker view, int y, int m, int d) -> {
            String date = d + "/" + (m + 1) + "/" + y;
            birthDateInput.setText(date);
        }, year, month, day);

        dialog.show();
    }
}
