package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        TextView textConfirmation = findViewById(R.id.textConfirmationMessage);
        Button btnContinueShopping = findViewById(R.id.btnContinueShopping);

        // Retrieve data passed from CheckoutActivity
        Intent intent = getIntent();
        String grandTotal = intent.getStringExtra("grandTotal");
        String method = intent.getStringExtra("method");
        // ⭐ NEW: Retrieve the Reference Number ⭐
        String reference = intent.getStringExtra("reference");

        // Display a summary of the order
        String message = "Your order has been successfully placed!";

        // ⭐ NEW: Display the Reference Number prominently ⭐
        if (reference != null && !reference.isEmpty()) {
            message += "\n\nReference No.: " + reference;
        }

        message += "\n\nTotal Paid: " + grandTotal;
        message += "\nDelivery Method: " + method;
        message += "\n\nThank you for shopping with San Isidro Pharmacy!";

        textConfirmation.setText(message);

        // Set up the button to return to the catalog
        btnContinueShopping.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, CatalogActivity.class);
            // Flags clear the back stack so the user cannot press back to get to checkout
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        });
    }
}