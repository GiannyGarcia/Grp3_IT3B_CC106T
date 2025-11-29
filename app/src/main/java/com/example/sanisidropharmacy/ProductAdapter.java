package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent; // ⭐ NEW IMPORT
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final List<Product> productList;
    private final List<Product> filteredList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = filteredList.get(position);

        holder.productName.setText(product.getName());
        holder.productBrand.setText(product.getBrand() != null ? product.getBrand() : "Unknown Brand");
        holder.productCategory.setText(product.getCategory() != null ? product.getCategory() : "Uncategorized");
        holder.productPrice.setText(String.format("₱%.2f", product.getPrice()));

        // --- Image Loading Logic ---
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.pm1_amlodipine)
                    .error(R.drawable.pm1_amlodipine)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.pm1_amlodipine);
        }

        // --- STOCK CHECK AND BUTTON LOGIC (Line 66 Fix) ---
        final boolean isInStock = product.getStock() > 0;

        // Line 66: This line caused the crash because holder.btnAddProductToCart was null.
        // It should now be correctly initialized in the ViewHolder (see below).
        holder.btnAddProductToCart.setEnabled(isInStock);
        holder.btnAddProductToCart.setText(isInStock ? "Add to Cart" : "Out of Stock");

        // Set click listener for the ADD TO CART BUTTON
        holder.btnAddProductToCart.setOnClickListener(v -> {
            if (isInStock) {
                CartStorage.addItem(product, 1);
                int itemCount = CartStorage.getCart().size();

                Toast.makeText(context,
                        product.getName() + " added to cart! Total items: " + itemCount,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Cannot add, " + product.getName() + " is out of stock.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- ITEM CLICK LISTENER (To open MedicineDetailActivity) ---
        // This is often what the user means when they click the "catalog."
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailActivity.class);

            // ⭐ CRITICAL: Pass ALL product data using the keys MedicineDetailActivity expects
            intent.putExtra("id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("price", String.valueOf(product.getPrice())); // Send price as String for consistency
            intent.putExtra("brand", product.getBrand());
            intent.putExtra("category", product.getCategory());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("stock", product.getStock());
            intent.putExtra("prescription", product.isPrescriptionRequired()); // Assuming this method exists
            intent.putExtra("image", product.getImageUrl());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void addProduct(Product product) {
        productList.add(product);
        filteredList.add(product);
        notifyItemInserted(filteredList.size() - 1);
    }

    public void setProductList(List<Product> newProductList) {
        this.filteredList.clear();
        this.filteredList.addAll(newProductList);
        notifyDataSetChanged();
    }

    // 🔍 FILTERING LOGIC
    @Override
    @SuppressWarnings("unchecked") // Suppress warning for results.values cast
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<Product> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered.addAll(productList);
                } else {
                    for (Product product : productList) {
                        if ((product.getName() != null && product.getName().toLowerCase().contains(query))
                                || (product.getBrand() != null && product.getBrand().toLowerCase().contains(query))
                                || (product.getCategory() != null && product.getCategory().toLowerCase().contains(query))) {
                            filtered.add(product);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Product>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    // --- VIEWHOLDER ---
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productBrand, productCategory, productPrice;
        Button btnAddProductToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);

            // ⭐ CRITICAL FIX: ALL TextView IDs must be corrected to the longer, consistent naming
            // that matches your item_product_grid.xml file.
            productName = itemView.findViewById(R.id.textProductName);      // FIX: Used the correct long ID
            productBrand = itemView.findViewById(R.id.textProductBrand);    // FIX: Used the correct long ID
            productCategory = itemView.findViewById(R.id.textProductCategory); // FIX: Used the correct long ID
            productPrice = itemView.findViewById(R.id.textProductPrice);    // FIX: Used the correct long ID

            // This ID is now assumed correct based on your recent XML update
            btnAddProductToCart = itemView.findViewById(R.id.btnAddProductToCart);
        }
    }
}