package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanisidropharmacy.R;
import com.example.sanisidropharmacy.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final List<Product> productList;
    private final List<Product> filteredList; // for searching

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredList = new ArrayList<>(productList); // copy for filtering
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

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.pm1_amlodipine)
                    .error(R.drawable.pm1_amlodipine)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.pm1_amlodipine);
        }
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

    // 🔍 FILTERING LOGIC
    @Override
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
                        if (product.getName().toLowerCase().contains(query)
                                || product.getBrand().toLowerCase().contains(query)
                                || product.getCategory().toLowerCase().contains(query)) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productBrand, productCategory, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productBrand = itemView.findViewById(R.id.productBrand);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
