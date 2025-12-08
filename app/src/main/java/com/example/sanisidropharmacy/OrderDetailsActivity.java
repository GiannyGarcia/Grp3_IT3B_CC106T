package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView textRef, textDate, textPayment, textStatus, textDelivery, textTotal;
    RecyclerView recyclerItems;

    OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderModel = (OrderModel) getIntent().getSerializableExtra("orderModel");

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

        textRef.setText("Ref: " + orderModel.getReference());
        textDate.setText("Date: " + orderModel.getCreatedAt());
        textPayment.setText("Payment: " + orderModel.getPaymentMethod());
        textStatus.setText("Status: " + orderModel.getStatus());
        textDelivery.setText("Delivery Address: " + orderModel.getDeliveryAddress());
        textTotal.setText("Total: ₱" + orderModel.getTotal());

        OrderDetailsItemAdapter adapter =
                new OrderDetailsItemAdapter(orderModel.getItems(), this);
        recyclerItems.setAdapter(adapter);
    }

    private void setupBottomNav() {
        View bottom = findViewById(R.id.include_bottom_nav);
        if (bottom == null) return;

        bottom.findViewById(R.id.nav_home).setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        bottom.findViewById(R.id.nav_cart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        bottom.findViewById(R.id.nav_user).setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
