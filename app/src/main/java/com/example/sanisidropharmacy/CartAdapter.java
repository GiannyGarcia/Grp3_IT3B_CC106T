package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // ⭐ NEW IMPORT
import android.widget.TextView;
import android.widget.Toast; // ⭐ NEW IMPORT
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final List<CartModel> cartItems;
    private final Context context;
    private final PriceUpdateCallback callback;

    public interface PriceUpdateCallback {
        void onPriceUpdated();
    }

    public CartAdapter(List<CartModel> cartItems, Context context, PriceUpdateCallback callback) {
        this.cartItems = cartItems;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Assuming your layout file is named 'item_cart'
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CartModel item = cartItems.get(position); // Use final for inner class reference
        Product product = item.getProduct();

        // Calculate item subtotal (Price * Quantity)
        double itemSubtotal = product.getPrice() * item.getQuantity();

        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.format("₱%.2f", itemSubtotal));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // --- ⭐ IMPLEMENT BUTTON LOGIC ⭐ ---

        // 1. Delete Button Logic (ID: btnDelete)
        holder.btnDelete.setOnClickListener(v -> {
            CartStorage.removeItem(item);

            // Notify RecyclerView of item removal
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            callback.onPriceUpdated(); // Update total in CartActivity

            Toast.makeText(context, product.getName() + " removed.", Toast.LENGTH_SHORT).show();
        });

        // 2. Plus Button Logic (ID: btnPlus)
        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;

            CartStorage.updateQuantity(item, newQty);
            item.setQuantity(newQty);

            notifyItemChanged(position);
            callback.onPriceUpdated();
        });

        // 3. Minus Button Logic (ID: btnMinus)
        holder.btnMinus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                int newQty = currentQty - 1;

                CartStorage.updateQuantity(item, newQty);
                item.setQuantity(newQty);

                notifyItemChanged(position);
                callback.onPriceUpdated();
            } else {
                Toast.makeText(context, "Minimum quantity is 1. Use the delete icon to remove the item.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- END BUTTON LOGIC ---
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // --- ⭐ UPDATED VIEWHOLDER CLASS (Matching final XML IDs) ⭐ ---
    static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare all views from item_cart.xml
        ImageView imgCart, btnMinus, btnPlus, btnDelete;
        TextView txtName, txtPrice, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize TextViews
            txtName = itemView.findViewById(R.id.cartItemName);
            txtPrice = itemView.findViewById(R.id.cartItemPrice);

            // Initialize Quantity & Control Views
            btnMinus = itemView.findViewById(R.id.btnMinus);
            txtQuantity = itemView.findViewById(R.id.cartItemQty); // ⭐ CORRECTED ID: cartItemQty
            btnPlus = itemView.findViewById(R.id.btnPlus);

            // Initialize Image Views
            imgCart = itemView.findViewById(R.id.cartImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}