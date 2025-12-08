package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView textConfirmation;
    private Button btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        textConfirmation = findViewById(R.id.textConfirmationMessage);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        Intent intent = getIntent();
        String grandTotal = intent.getStringExtra("grandTotal");
        String method = intent.getStringExtra("method");
        String reference = intent.getStringExtra("reference");
        int orderId = intent.getIntExtra("orderId", -1);

        StringBuilder message = new StringBuilder();
        message.append("Your order has been placed successfully!");

        if (reference != null && !reference.isEmpty()) {
            message.append("\n\nReference No.: ").append(reference);
        }

        if (orderId != -1) {
            message.append("\nOrder ID: ").append(orderId);
        }

        if (grandTotal != null) {
            message.append("\n\nTotal Paid: ").append(grandTotal);
        }

        if (method != null) {
            message.append("\nDelivery Method: ").append(method);
        }

        message.append("\n\nThank you for shopping with San Isidro Pharmacy!");

        textConfirmation.setText(message.toString());

        btnContinueShopping.setOnClickListener(v -> {
            Intent home = new Intent(OrderConfirmationActivity.this, CatalogActivity.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
        });
    }
}
