package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView textRef, textDate, textPayment, textStatus, textDelivery, textTotal;
    private RecyclerView recyclerItems;

    private OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // FIXED: Correct intent key
        orderModel = (OrderModel) getIntent().getSerializableExtra("order_data");

        initViews();
        displayData();
        setupBottomNav();
    }

    private void initViews() {
        textRef = findViewById(R.id.textRef);
        textDate = findViewById(R.id.textDate);
        textPayment = findViewById(R.id.textPayment);
        textStatus = findViewById(R.id.textStatus);
        textDelivery = findViewById(R.id.textDelivery);
        textTotal = findViewById(R.id.textTotal);

        recyclerItems = findViewById(R.id.recyclerOrderItems);
        recyclerItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayData() {
        if (orderModel == null) return;

        // FIXED: Use the correct original getters
        textRef.setText("Ref: " + safe(orderModel.getReference()));
        textDate.setText("Date: " + safe(orderModel.getCreated_at()));
        textPayment.setText("Payment: " + safe(orderModel.getPayment_method()));
        textStatus.setText("Status: " + safe(orderModel.getStatus()));
        textDelivery.setText("Delivery Address: " + safe(orderModel.getDelivery_address()));
        textTotal.setText("Total: ₱" + String.format("%.2f", orderModel.getTotal()));

        OrderDetailsItemAdapter adapter =
                new OrderDetailsItemAdapter(orderModel.getItems(), this);

        recyclerItems.setAdapter(adapter);
    }

    private String safe(String s) {
        return (s != null) ? s : "N/A";
    }

    private void setupBottomNav() {
        View bottom = findViewById(R.id.include_bottom_nav);
        if (bottom == null) return;

        bottom.findViewById(R.id.nav_home)
                .setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));

        bottom.findViewById(R.id.nav_cart)
                .setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

        bottom.findViewById(R.id.nav_user)
                .setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
