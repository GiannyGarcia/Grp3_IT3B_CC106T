package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private final Context context;
    private List<Product> productList = new ArrayList<>();

    public interface OnItemClickListener {
        void onClick(Product product);
    }

    private OnItemClickListener listener;

    public ProductAdapter(Context ctx, List<Product> list) {
        this.context = ctx;
        this.productList = list;
    }

    // Allow UserViewActivity to update filtered list
    public void setProductList(List<Product> newList) {
        this.productList = newList;
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

        holder.name.setText(p.getName());
        holder.brand.setText(p.getBrand() != null ? p.getBrand() : "Unknown");
        holder.category.setText(p.getCategory());
        holder.price.setText("₱" + p.getPrice());

        // Load image using Glide
        if (p.hasImage()) {
            Glide.with(context)
                    .load(p.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder);
        }

        // Add to cart button — can be expanded later
        holder.btnAddToCart.setOnClickListener(v -> {
            // Toast or future cart logic here
        });

        // Item click → go to details if you want later
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
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
