package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    // REQUIRED FIELDS
    EditText nameInput, emailInput, passwordInput;
    EditText birthDateInput;

    // OPTIONAL FIELDS
    EditText contactInput = null, addressInput = null;

    // ACCOUNT TYPE (User / Admin)
    RadioGroup accountTypeGroup;
    RadioButton userRadio, adminRadio;

    Button signupButton;

    private static final String USER_PREFS = "user_prefs";
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Required inputs
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        // Birthdate
        birthDateInput = findViewById(R.id.birthDateInput);

        // Account type radio group
        accountTypeGroup = findViewById(R.id.accountTypeGroup);
        userRadio = findViewById(R.id.userRadio);
        adminRadio = findViewById(R.id.adminRadio);

        // Sign-up button
        signupButton = findViewById(R.id.signupButton);

        // Optional fields (your layout does not include them, so we detect safely)
        int contactId = getResources().getIdentifier("contactInput", "id", getPackageName());
        if (contactId != 0) contactInput = findViewById(contactId);

        int addressId = getResources().getIdentifier("addressInput", "id", getPackageName());
        if (addressId != 0) addressInput = findViewById(addressId);

        signupButton.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String birthdate = birthDateInput.getText().toString().trim();
        String role = userRadio.isChecked() ? "User" : "Admin";

        // Optional fields
        String contact = (contactInput != null) ? contactInput.getText().toString().trim() : "";
        String address = (addressInput != null) ? addressInput.getText().toString().trim() : "";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<AuthResponse> call = api.register(
                name,
                email,
                password,
                contact,
                address,
                birthdate,
                role
        );

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(SignUpActivity.this, "Server error: Invalid response", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthResponse res = response.body();

                if (!res.isSuccess()) {
                    Toast.makeText(SignUpActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = res.getUser();

                // Save session
                SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putInt("session_user_id", user.getId());
                editor.putString("session_name", user.getFullname());
                editor.putString("session_email", user.getEmail());
                editor.putString("session_birthdate", user.getBirthdate());
                editor.putString("session_role", user.getRole());
                editor.putString("session_contact", user.getContact());
                editor.putString("session_address", user.getAddress());
                editor.putInt("session_loyalty", user.getLoyalty_points());
                editor.apply();

                Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Redirect
                startActivity(new Intent(SignUpActivity.this, CatalogActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Register failed: " + t.getMessage());
                Toast.makeText(SignUpActivity.this, "Network error. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
