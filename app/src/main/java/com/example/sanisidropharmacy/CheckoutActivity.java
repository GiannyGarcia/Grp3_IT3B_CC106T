package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.lang.Math; // Ensure Math is available for random generation

public class CheckoutActivity extends AppCompatActivity {

    private static final double DELIVERY_FEE = 50.00;

    private TextInputEditText editName, editAddress, editPhone;
    private RadioGroup radioDeliveryMethodGroup, radioPaymentGroup;
    private TextView textSubtotal, textShippingFee, textGrandTotal, textPickupInfo;
    private LinearLayout layoutAddressInputs;
    private TableRow rowShipping;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // 1. Initialize Views
        initializeViews();

        // 2. Set Listeners
        radioDeliveryMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            updateDeliveryMethodView(checkedId == R.id.radioDelivery);
        });

        btnPlaceOrder.setOnClickListener(v -> placeOrder());

        // 3. Initial Display
        updateDeliveryMethodView(true);
    }

    // --- View Initialization ---
    private void initializeViews() {
        // Shipping/Delivery Views
        radioDeliveryMethodGroup = findViewById(R.id.radioDeliveryMethodGroup);
        layoutAddressInputs = findViewById(R.id.layoutAddressInputs);
        textPickupInfo = findViewById(R.id.textPickupInfo);
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        editPhone = findViewById(R.id.editPhone);

        // Payment Views
        radioPaymentGroup = findViewById(R.id.radioPaymentGroup);

        // Summary Views
        textSubtotal = findViewById(R.id.textSubtotal);
        rowShipping = findViewById(R.id.rowShipping);
        textShippingFee = findViewById(R.id.textShippingFee);
        textGrandTotal = findViewById(R.id.textGrandTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    // --- Logic to Handle Delivery/Pickup Selection ---
    private void updateDeliveryMethodView(boolean isDelivery) {
        if (isDelivery) {
            layoutAddressInputs.setVisibility(View.VISIBLE);
            textPickupInfo.setVisibility(View.GONE);
            rowShipping.setVisibility(View.VISIBLE);
            textShippingFee.setText(String.format("₱%.2f", DELIVERY_FEE));
        } else {
            layoutAddressInputs.setVisibility(View.GONE);
            textPickupInfo.setVisibility(View.VISIBLE);
            rowShipping.setVisibility(View.GONE);
            textShippingFee.setText("FREE");
        }

        calculateTotals(isDelivery);
    }

    // --- Price Calculation Logic ---
    private void calculateTotals(boolean isDelivery) {
        List<CartModel> items = CartStorage.getCart(this);
        double subtotal = 0.0;

        for (CartModel cartItem : items) {
            subtotal += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }

        double finalShippingFee = isDelivery ? DELIVERY_FEE : 0.0;
        double grandTotal = subtotal + finalShippingFee;

        textSubtotal.setText(String.format("₱%.2f", subtotal));
        textGrandTotal.setText(String.format("₱%.2f", grandTotal));
    }

    // ⭐ NEW METHOD: Generates a random, unique reference number ⭐
    /**
     * Generates a simple 8-character alphanumeric reference number.
     */
    private String generateReferenceNumber() {
        // Use current timestamp and a small random number
        long timestamp = System.currentTimeMillis();
        long uniqueID = timestamp + (long)(Math.random() * 100000);

        // Convert to base-36 (0-9, a-z) for a compact alphanumeric string, then capitalize
        String ref = Long.toString(uniqueID, 36).toUpperCase();

        // Return the last 8 characters, or the whole thing if shorter
        if (ref.length() > 8) {
            return ref.substring(ref.length() - 8);
        }
        return ref;
    }

    // --- Order Placement Logic (UPDATED) ---
    private void placeOrder() {
        if (CartStorage.getCart(this).isEmpty()) {
            Toast.makeText(this, "Your cart is empty. Cannot place order.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Determine delivery method
        boolean isDelivery = radioDeliveryMethodGroup.getCheckedRadioButtonId() == R.id.radioDelivery;

        // 2. Validate input fields based on method
        if (isDelivery) {
            if (!validateDeliveryFields()) {
                return; // Validation failed
            }
        }

        // 3. Process Payment
        String orderMethod = isDelivery ? "Delivery" : "Pickup";

        // ⭐ NEW: Generate Reference Number ⭐
        String transactionRef = generateReferenceNumber();

        // 4. Finalize the order (Database/Server save would happen here)

        // 5. Clear the Cart (Crucial step)
        CartStorage.clearCart(this);

        // 6. Navigate to Confirmation Screen
        Toast.makeText(this, "Order placed successfully! Ref: " + transactionRef, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, OrderConfirmationActivity.class);
        // Pass essential details
        intent.putExtra("grandTotal", textGrandTotal.getText().toString());
        intent.putExtra("method", orderMethod);
        // ⭐ NEW: Pass the Reference Number ⭐
        intent.putExtra("reference", transactionRef);
        startActivity(intent);

        finish();
    }

    // --- Simple Validation for Delivery Mode ---
    private boolean validateDeliveryFields() {
        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill out all required delivery fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}