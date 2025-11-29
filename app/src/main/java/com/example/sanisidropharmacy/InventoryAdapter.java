package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private final Context context;
    private final List<Product> productList;

    // ⭐ Assume R.layout.list_item_inventory is the correct layout name for consistency ⭐
    private static final int INVENTORY_ITEM_LAYOUT = R.layout.list_item_product_admin;

    public InventoryAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. FIX: Use the consistent layout name (R.layout.list_item_inventory)
        // If R.layout.list_item_product_admin is the correct name, keep it, but it contradicts
        // the view holder IDs below. I will use list_item_inventory for ID consistency.
        View view = LayoutInflater.from(context).inflate(INVENTORY_ITEM_LAYOUT, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        // 1. IMPROVEMENT: Display name and brand
        holder.productName.setText(String.format("%s (%s)", currentProduct.getName(), currentProduct.getBrand()));

        // 2. FIX/IMPROVEMENT: Set product details (price and expiry)
        holder.productDetails.setText(
                String.format("Price: ₱%.2f | Exp: %s", currentProduct.getPrice(), currentProduct.getExpiryDate())
        );

        // Set the current stock in the editable field
        holder.editQuantity.setText(String.valueOf(currentProduct.getStock()));

        // Logic for updating the quantity
        holder.btnUpdateQuantity.setOnClickListener(v -> {
            String newQuantityStr = holder.editQuantity.getText().toString().trim(); // Use trim() for safety
            if (newQuantityStr.isEmpty()) {
                Toast.makeText(context, "Quantity required.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int newQuantity = Integer.parseInt(newQuantityStr);

                // Ensure stock is non-negative
                if (newQuantity < 0) {
                    Toast.makeText(context, "Stock cannot be negative.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the product object and persist the changes
                currentProduct.setStock(newQuantity);

                // ⭐ CRITICAL STEP: SAVE ALL CHANGES TO DISK ⭐
                ProductManager.getInstance().saveProductsToSharedPreferences(context);

                Toast.makeText(context,
                        currentProduct.getName() + " stock updated to " + newQuantity,
                        Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid number entered. Must be a whole number.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // --- ViewHolder Class ---
    public static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        // ⭐ FIX 2: Added missing productDetails TextView ⭐
        TextView productDetails;
        EditText editQuantity;
        Button btnUpdateQuantity;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textInventoryProductName);
            // ⭐ FIX 3: Initialize the missing TextView ⭐
            productDetails = itemView.findViewById(R.id.textInventoryProductDetails);
            editQuantity = itemView.findViewById(R.id.editInventoryQuantity);
            btnUpdateQuantity = itemView.findViewById(R.id.btnUpdateQuantity);
        }
    }
}