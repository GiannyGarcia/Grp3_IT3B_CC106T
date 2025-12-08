package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.VH> {

    private List<OrderModel> list;   // ← NOW USING ORDERMODEL
    private Context ctx;

    // Allowed status flow
    private final String[] statusFlow = {"Pending", "Processing", "Completed", "Cancelled"};

    public AdminOrdersAdapter(List<OrderModel> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_admin, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

        OrderModel o = list.get(position);

        holder.tvRef.setText("Ref: " + (o.getReference() != null ? o.getReference() : o.getId()));
        holder.tvUser.setText("User ID: " + o.getUserId());
        holder.tvTotal.setText("₱" + String.format("%.2f", o.getTotal()));
        holder.tvStatus.setText(o.getStatus());

        // View order details
        holder.btnView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, OrderDetailsActivity.class);
            i.putExtra("orderModel", o);
            ctx.startActivity(i);
        });

        // NEXT STATUS
        holder.btnNext.setOnClickListener(v -> {
            int idx = indexOf(statusFlow, o.getStatus());
            int next = (idx < statusFlow.length - 1) ? idx + 1 : idx;
            String newStatus = statusFlow[next];
            updateStatus(o.getId(), newStatus, holder, o);
        });

        // PREVIOUS STATUS
        holder.btnPrev.setOnClickListener(v -> {
            int idx = indexOf(statusFlow, o.getStatus());
            int prev = (idx > 0) ? idx - 1 : idx;
            String newStatus = statusFlow[prev];
            updateStatus(o.getId(), newStatus, holder, o);
        });
    }

    private int indexOf(String[] arr, String v) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equalsIgnoreCase(v)) return i;
        return -1;
    }

    private void updateStatus(int orderId, String status, VH holder, OrderModel o) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);

        api.updateOrderStatus(orderId, status).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {

                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) {
                    Toast.makeText(ctx, "Failed to update status", Toast.LENGTH_SHORT).show();
                    return;
                }

                o.setStatus(status);           // update model
                holder.tvStatus.setText(status);
                Toast.makeText(ctx, "Status updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(ctx, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRef, tvUser, tvTotal, tvStatus;
        Button btnPrev, btnNext, btnView;

        VH(View v) {
            super(v);
            tvRef = v.findViewById(R.id.tvAdminRef);
            tvUser = v.findViewById(R.id.tvAdminUser);
            tvTotal = v.findViewById(R.id.tvAdminTotal);
            tvStatus = v.findViewById(R.id.tvAdminStatus);
            btnPrev = v.findViewById(R.id.btnPrevStatus);
            btnNext = v.findViewById(R.id.btnNextStatus);
            btnView = v.findViewById(R.id.btnViewAdminDetails);
        }
    }
}
