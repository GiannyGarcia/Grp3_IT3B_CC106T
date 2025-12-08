package com.example.sanisidropharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailsItemAdapter extends RecyclerView.Adapter<OrderDetailsItemAdapter.ViewHolder> {

    private List<OrderHistoryResponse.OrderLine> list;
    private Context ctx;

    public OrderDetailsItemAdapter(List<OrderHistoryResponse.OrderLine> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.row_order_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        OrderHistoryResponse.OrderLine m = list.get(pos);

        h.name.setText(m.name);
        h.qty.setText("Qty: " + m.qty);
        h.price.setText("₱" + String.format("%.2f", m.price));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, qty, price;
        public ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.rowName);
            qty = v.findViewById(R.id.rowQty);
            price = v.findViewById(R.id.rowPrice);
        }
    }
}
