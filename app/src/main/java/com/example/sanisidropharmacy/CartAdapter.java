package com.example.sanisidropharmacy;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface PriceUpdateCallback {
        void onPriceUpdated();
    }

    private List<CartModel> cartList;
    private Context context;
    private PriceUpdateCallback callback;

    public CartAdapter(List<CartModel> cartList, Context context, PriceUpdateCallback callback) {
        this.cartList = cartList;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartModel item = cartList.get(position);
        Product p = item.getProduct();

        holder.name.setText(p.getName());
        holder.price.setText("₱" + p.getPrice());
        holder.qty.setText(String.valueOf(item.getQuantity()));

        // Image
        if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(p.getImageUrl()))
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.pharmacy_logo);
        }

        // Delete
        holder.btnDelete.setOnClickListener(v -> {
            CartStorage.removeItem(context, item);
            cartList.remove(position);
            notifyItemRemoved(position);
            callback.onPriceUpdated();
        });

        // Minus
        holder.btnMinus.setOnClickListener(v -> {
            int q = item.getQuantity();
            if (q > 1) {
                item.setQuantity(q - 1);
                CartStorage.updateQuantity(context, item);
                holder.qty.setText(String.valueOf(item.getQuantity()));
                callback.onPriceUpdated();
            }
        });

        // Plus
        holder.btnPlus.setOnClickListener(v -> {
            int q = item.getQuantity() + 1;
            item.setQuantity(q);
            CartStorage.updateQuantity(context, item);
            holder.qty.setText(String.valueOf(item.getQuantity()));
            callback.onPriceUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView image, btnMinus, btnPlus, btnDelete;
        TextView name, price, qty;

        public CartViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.cartImage);
            name = itemView.findViewById(R.id.cartItemName);
            price = itemView.findViewById(R.id.cartItemPrice);
            qty = itemView.findViewById(R.id.cartItemQty);

            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
