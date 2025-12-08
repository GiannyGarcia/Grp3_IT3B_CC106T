package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView textRef, textDate, textPayment, textStatus, textDelivery, textTotal;
    RecyclerView recyclerItems;

    OrderDto orderDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Get passed DTO (key "order_dto")
        orderDto = (OrderDto) getIntent().getSerializableExtra("order_dto");

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
        if (orderDto == null) return;

        String ref = (orderDto.reference != null && !orderDto.reference.isEmpty()) ? orderDto.reference : "REF-" + orderDto.id;
        textRef.setText("Ref: " + ref);
        textDate.setText("Date: " + (orderDto.created_at != null ? orderDto.created_at : "Unknown"));
        textPayment.setText("Payment: " + (orderDto.payment_method != null ? orderDto.payment_method : "N/A"));
        textStatus.setText("Status: " + (orderDto.status != null ? orderDto.status : "N/A"));
        textDelivery.setText("Delivery Address: " + (orderDto.delivery_address != null ? orderDto.delivery_address : "N/A"));
        textTotal.setText("Total: ₱" + String.format("%.2f", orderDto.total));

        // Build and set local adapter for items (OrderDto.OrderItemDto)
        List<OrderDto.OrderItemDto> items = orderDto.items;
        OrderItemsLocalAdapter adapter = new OrderItemsLocalAdapter(items);
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

    // --------------------------
    // Local Adapter for items
    // --------------------------
    private static class OrderItemsLocalAdapter extends RecyclerView.Adapter<OrderItemsLocalAdapter.VH> {

        private final List<OrderDto.OrderItemDto> items;

        OrderItemsLocalAdapter(List<OrderDto.OrderItemDto> items) {
            this.items = items;
        }

        @Override
        public VH onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_order_item, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            OrderDto.OrderItemDto it = (items != null && position < items.size()) ? items.get(position) : null;
            if (it == null) {
                holder.name.setText("Item");
                holder.qty.setText("Qty: 0");
                holder.price.setText("₱0.00");
            } else {
                holder.name.setText(it.name != null ? it.name : "Item");
                holder.qty.setText("Qty: " + it.qty);
                holder.price.setText("₱" + String.format("%.2f", it.price));
            }
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView name, qty, price;
            VH(View v) {
                super(v);
                name = v.findViewById(R.id.rowName);
                qty = v.findViewById(R.id.rowQty);
                price = v.findViewById(R.id.rowPrice);
            }
        }
    }
}
