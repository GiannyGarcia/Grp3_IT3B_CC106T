package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the product grid in UserViewActivity.
 * - Exposes a click-listener so the activity can open detail screen.
 * - Implements Add to Cart directly from the grid.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final Context context;
    private List<Product> productList = new ArrayList<>();

    public interface OnItemClickListener {
        void onClick(Product product);
    }

    private OnItemClickListener listener;

    public ProductAdapter(Context ctx, List<Product> list) {
        this.context = ctx;
        this.productList = list != null ? list : new ArrayList<>();
    }

    // allow host activity to set listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // allow host to replace list (e.g. filtered list)
    public void setProductList(List<Product> newList) {
        this.productList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_product_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Product p = productList.get(position);

        holder.name.setText(p.getName() != null ? p.getName() : "Unknown");
        holder.brand.setText(p.getBrand() != null ? p.getBrand() : "Unknown");
        holder.category.setText(p.getCategory() != null ? p.getCategory() : "Uncategorized");
        holder.price.setText(String.format("₱%.2f", p.getPrice()));

        // Load image using Glide, fallback to placeholder
        if (p.hasImage()) {
            Glide.with(context)
                    .load(p.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder) // ensure this drawable exists; change if needed
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder);
        }

        // Add-to-cart button: store into CartStorage using context
        holder.btnAddToCart.setOnClickListener(v -> {
            CartStorage.addItem(context, p, 1);
            Toast.makeText(context, p.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });

        // Item click to open detail screen
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(p);
                return;
            }
            // fallback: open MedicineDetailActivity directly if no listener set
            Intent i = new Intent(context, MedicineDetailActivity.class);
            i.putExtra("id", p.getId());
            i.putExtra("name", p.getName());
            i.putExtra("brand", p.getBrand());
            i.putExtra("category", p.getCategory());
            i.putExtra("description", p.getDescription());
            i.putExtra("dosage", "");
            i.putExtra("expiryDate", p.getExpiryDate());
            i.putExtra("image", p.getImageUrl());
            i.putExtra("price", String.valueOf(p.getPrice()));
            i.putExtra("stock", p.getStock());
            i.putExtra("prescription", p.isPrescriptionRequired());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, brand, category, price;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.textProductName);
            brand = itemView.findViewById(R.id.textProductBrand);
            category = itemView.findViewById(R.id.textProductCategory);
            price = itemView.findViewById(R.id.textProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddProductToCart);
        }
    }
}
