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
    private static final String TAG = "LOGIN_ERROR";

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

        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        Call<AuthResponse> call = api.login(email, password);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthResponse res = response.body();
                if (res == null) {
                    Toast.makeText(LoginActivity.this, "Empty server response", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!res.isSuccess()) {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // VALID LOGIN
                User user = res.getUser();
                saveSession(user);

                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, CatalogActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login failed: " + t.getMessage());
                Toast.makeText(LoginActivity.this,
                        "Network Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveSession(User user) {
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
    }
}
