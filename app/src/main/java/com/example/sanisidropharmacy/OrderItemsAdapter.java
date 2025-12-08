package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.VH> {

    private final Context ctx;
    private final List<OrderItemModel> items;

    public OrderItemsAdapter(Context ctx, List<OrderItemModel> items) {
        this.ctx = ctx;
        this.items = items;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtQty, txtPrice;
        public VH(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtItemName);
            txtQty = v.findViewById(R.id.txtItemQty);
            txtPrice = v.findViewById(R.id.txtItemPrice);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.order_item_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        OrderItemModel it = items.get(pos);
        h.txtName.setText(it.getName());
        h.txtQty.setText("Qty: " + it.getQty());
        h.txtPrice.setText("₱" + String.format("%.2f", it.getPrice()));
    }

    @Override
    public int getItemCount() { return items.size(); }
}
