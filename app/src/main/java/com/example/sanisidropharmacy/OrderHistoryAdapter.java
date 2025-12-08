package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.VH> {

    private final List<OrderModel> list;
    private final Context ctx;

    public OrderHistoryAdapter(List<OrderModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        OrderModel o = list.get(position);

        // --------------------------
        // SAFE REFERENCE HANDLING
        // --------------------------
        String ref = (o.getReference() != null && !o.getReference().trim().isEmpty())
                ? o.getReference()
                : "REF-" + o.getId();

        holder.tvRef.setText("Ref: " + ref);

        // Created_at
        holder.tvDate.setText(
                o.getCreated_at() != null ? o.getCreated_at() : "Unknown Date"
        );

        // Total
        holder.tvTotal.setText("₱" + String.format("%.2f", o.getTotal()));

        // Status handling
        holder.tvStatus.setText(
                o.getStatus() != null ? o.getStatus() : "Unknown"
        );

        // --------------------------
        // VIEW DETAILS BUTTON
        // --------------------------
        holder.btnDetails.setOnClickListener(v -> {

            // Pass FULL OrderModel object (Serializable)
            Intent intent = new Intent(ctx, OrderDetailsActivity.class);
            intent.putExtra("order_data", o);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // -----------------------------------------------------
    // VIEW HOLDER
    // -----------------------------------------------------
    static class VH extends RecyclerView.ViewHolder {

        TextView tvRef, tvDate, tvTotal, tvStatus;
        Button btnDetails;

        VH(View v) {
            super(v);

            tvRef = v.findViewById(R.id.tvRef);
            tvDate = v.findViewById(R.id.tvDate);
            tvTotal = v.findViewById(R.id.tvTotal);
            tvStatus = v.findViewById(R.id.tvStatus);
            btnDetails = v.findViewById(R.id.btnViewDetails);
        }
    }
}
