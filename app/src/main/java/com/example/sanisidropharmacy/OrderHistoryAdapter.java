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

    private final List<OrderDto> list;
    private final Context ctx;

    public OrderHistoryAdapter(List<OrderDto> list, Context ctx) {
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

        OrderDto o = list.get(position);

        String ref = (o.reference != null && !o.reference.trim().isEmpty())
                ? o.reference
                : "REF-" + o.id;

        holder.tvRef.setText("Ref: " + ref);
        holder.tvDate.setText(o.created_at != null ? o.created_at : "Unknown Date");
        holder.tvTotal.setText("₱" + String.format("%.2f", o.total));
        holder.tvStatus.setText(o.status != null ? o.status : "Unknown");

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, OrderDetailsActivity.class);
            intent.putExtra("order_dto", o);   // pass serialized OrderDto
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

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
