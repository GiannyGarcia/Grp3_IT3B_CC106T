package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> items;

    public CatalogAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CatalogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAdapter.ViewHolder holder, int position) {
        Product p = items.get(position);
        holder.name.setText(p.getName());
        holder.brand.setText(p.getBrand() != null ? p.getBrand() : "");
        holder.category.setText(p.getCategory() != null ? p.getCategory() : "");
        holder.price.setText(String.format("₱%.2f", p.getPrice()));

        String url = p.getImageUrl();
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.pm1_amlodipine)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.pm1_amlodipine);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, MedicineDetailActivity.class);
            i.putExtra("product", p);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, brand, category, price;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            brand = itemView.findViewById(R.id.productBrand);
            category = itemView.findViewById(R.id.productCategory);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
}
