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

    private List<OrderHistoryResponse.OrderItem> list;
    private Context ctx;

    public OrderHistoryAdapter(List<OrderHistoryResponse.OrderItem> list, Context ctx) { this.list = list; this.ctx=ctx; }

    @Override
    public VH onCreateViewHolder(ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_order_history, p, false));
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        OrderHistoryResponse.OrderItem o = list.get(pos);
        h.tvRef.setText("Ref: " + (o.reference!=null?o.reference:String.valueOf(o.id)));
        h.tvDate.setText(o.created_at);
        h.tvTotal.setText("₱" + String.format("%.2f", o.total));
        h.tvStatus.setText(o.status);
        h.btnDetails.setOnClickListener(v -> {
            Intent i = new Intent(ctx, OrderDetailsActivity.class);
            i.putExtra("order_id", o.id);
            ctx.startActivity(i);
        });
    }

    @Override public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRef,tvDate,tvTotal,tvStatus;
        Button btnDetails;
        VH(View v) {
            super(v);
            tvRef=v.findViewById(R.id.tvRef);
            tvDate=v.findViewById(R.id.tvDate);
            tvTotal=v.findViewById(R.id.tvTotal);
            tvStatus=v.findViewById(R.id.tvStatus);
            btnDetails=v.findViewById(R.id.btnViewDetails);
        }
    }
}
