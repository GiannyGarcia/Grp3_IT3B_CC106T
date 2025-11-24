package com.example.sanisidropharmacy;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final List<CartModel> cartList;
    private final Context context;
    private final OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public CartAdapter(List<CartModel> cartList, Context context, OnCartUpdatedListener listener) {
        this.cartList = cartList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        CartModel item = cartList.get(pos);

        h.txtName.setText(item.getName());
        h.txtPrice.setText("₱" + item.getPrice());
        h.txtQty.setText(String.valueOf(item.getQty()));

        // Load image
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(item.getImage()))
                    .into(h.imgProduct);
        } else {
            h.imgProduct.setImageResource(R.drawable.pharmacy_logo);
        }

        // -----------------------
        // PLUS BUTTON
        // -----------------------
        h.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQty() + 1;

            if (item.getStock() > 0 && newQty > item.getStock()) {
                newQty = item.getStock();
            }

            item.setQty(newQty);
            notifyItemChanged(pos);
            listener.onCartUpdated();
        });

        // -----------------------
        // MINUS BUTTON
        // -----------------------
        h.btnMinus.setOnClickListener(v -> {
            int newQty = item.getQty() - 1;

            if (newQty <= 0) {
                CartStorage.removeItem(item);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, cartList.size());
            } else {
                item.setQty(newQty);
                notifyItemChanged(pos);
            }

            listener.onCartUpdated();
        });

        // -----------------------
        // DELETE BUTTON
        // -----------------------
        h.btnDelete.setOnClickListener(v -> {
            CartStorage.removeItem(item);
            notifyItemRemoved(pos);
            notifyItemRangeChanged(pos, cartList.size());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtName, txtPrice, txtQty;
        ImageButton btnPlus, btnMinus, btnDelete;

        public ViewHolder(@NonNull View v) {
            super(v);

            imgProduct = v.findViewById(R.id.cartImage);
            txtName = v.findViewById(R.id.cartItemName);
            txtPrice = v.findViewById(R.id.cartItemPrice);
            txtQty = v.findViewById(R.id.cartItemQty);

            btnPlus = v.findViewById(R.id.btnPlus);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
