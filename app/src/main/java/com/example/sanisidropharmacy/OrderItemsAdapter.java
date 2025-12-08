package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.VH> {

    private final List<OrderHistoryResponse.OrderLine> items;
    private final Context ctx;

    public OrderItemsAdapter(Context ctx, List<OrderHistoryResponse.OrderLine> items) {
        this.ctx = ctx;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.row_order_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        OrderHistoryResponse.OrderLine it = items.get(position);
        holder.name.setText(it.name != null ? it.name : "Item");
        holder.qty.setText("Qty: " + it.qty);
        holder.price.setText("₱" + String.format("%.2f", it.price));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, qty, price;
        VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.rowName);
            qty = v.findViewById(R.id.rowQty);
            price = v.findViewById(R.id.rowPrice);
        }
    }
}
