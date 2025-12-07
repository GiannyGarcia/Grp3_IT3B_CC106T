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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton;
    TextView signupLink;

    RadioGroup roleGroup;
    RadioButton radioUser, radioAdmin;

    private static final String USER_PREFS = "user_prefs";
    private static final String TAG = "LoginActivity";

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

        // 1) Try server login using Retrofit
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<AuthResponse> call = api.login(email, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    User user = response.body().getUser();
                    // Save session in SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("session_user_id", user.getId());
                    editor.putString("session_name", user.getFullname());
                    editor.putString("session_email", user.getEmail());
                    editor.putString("session_contact", user.getContact());
                    editor.putString("session_address", user.getAddress());
                    editor.putInt("session_loyalty", user.getLoyalty_points());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Redirect based on (old role value) — attempt to keep prior behavior:
                    // If your server returns role in future, use it. For now default to Catalog.
                    startActivity(new Intent(LoginActivity.this, CatalogActivity.class));
                    finish();
                } else {
                    // Server responded but login invalid
                    Toast.makeText(LoginActivity.this, "Invalid credentials (server)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Server login failed: " + t.getMessage());
                // Fallback: local SharedPreferences-based login (legacy behavior)
                performLocalLoginFallback(email, password);
            }
        });
    }

    private void performLocalLoginFallback(String email, String password) {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        String savedEmail = prefs.getString(email + "_email", null);
        String savedPassword = prefs.getString(email + "_password", null);
        String savedRole = prefs.getString(email + "_role", "User");

        // Check if user exists
        if (savedEmail == null) {
            runOnUiThread(() -> Toast.makeText(this, "Account not found (offline). Please sign up or check connection.", Toast.LENGTH_SHORT).show());
            return;
        }

        // Check password
        if (!savedPassword.equals(password)) {
            runOnUiThread(() -> Toast.makeText(this, "Incorrect password (offline)!", Toast.LENGTH_SHORT).show());
            return;
        }

        // Save active session (local)
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("loggedInUser", email);
        editor.apply();

        runOnUiThread(() -> {
            Toast.makeText(this, "Login successful (offline)!", Toast.LENGTH_SHORT).show();
            if (savedRole.equals("Admin")) {
                startActivity(new Intent(this, ProductPostActivity.class));
            } else {
                startActivity(new Intent(this, CatalogActivity.class));
            }
            finish();
        });
    }
}
