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

    private final List<OrderDto> list;
    private final Context ctx;
    private final String[] statusFlow = {"Pending","Processing","Completed","Cancelled"};

    public AdminOrdersAdapter(List<OrderDto> list, Context ctx) { this.list=list; this.ctx=ctx; }

    @Override
    public VH onCreateViewHolder(ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_order_admin, p, false));
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        OrderDto o = list.get(pos);
        h.tvRef.setText("Ref: " + (o.reference != null ? o.reference : String.valueOf(o.id)));
        h.tvUser.setText("User ID: " + o.user_id);
        h.tvTotal.setText("₱" + String.format("%.2f", o.total));
        h.tvStatus.setText(o.status != null ? o.status : "Unknown");

        h.btnView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, OrderDetailsActivity.class);
            i.putExtra("order_dto", o);
            ctx.startActivity(i);
        });

        h.btnNext.setOnClickListener(v -> {
            int idx = indexOf(statusFlow, o.status);
            int next = (idx == -1 ? 0 : Math.min(statusFlow.length-1, idx+1));
            String newStatus = statusFlow[next];
            updateStatus(o.id, newStatus, h, o);
        });

        h.btnPrev.setOnClickListener(v -> {
            int idx = indexOf(statusFlow, o.status);
            int prev = (idx <= 0 ? 0 : idx-1);
            String newStatus = statusFlow[prev];
            updateStatus(o.id, newStatus, h, o);
        });
    }

    private int indexOf(String[] arr, String v) {
        for (int i=0;i<arr.length;i++) if (arr[i].equalsIgnoreCase(v)) return i;
        return -1;
    }

    private void updateStatus(int orderId, String status, VH holder, OrderDto model) {
        ApiService api = ApiClient.getRetrofit().create(ApiService.class);
        // Assume ApiService.updateOrderStatus exists and returns BasicResponse
        api.updateOrderStatus(orderId, status).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                if (!response.isSuccessful() || response.body()==null || !response.body().isSuccess()) {
                    Toast.makeText(ctx, "Failed to update status", Toast.LENGTH_SHORT).show();
                    return;
                }
                // update UI & model
                holder.tvStatus.setText(status);
                model.status = status;
                Toast.makeText(ctx, "Status updated", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Toast.makeText(ctx, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public int getItemCount() { return (list==null) ? 0 : list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvRef,tvUser,tvTotal,tvStatus;
        Button btnPrev,btnNext,btnView;
        VH(View v) {
            super(v);
            tvRef=v.findViewById(R.id.tvAdminRef);
            tvUser=v.findViewById(R.id.tvAdminUser);
            tvTotal=v.findViewById(R.id.tvAdminTotal);
            tvStatus=v.findViewById(R.id.tvAdminStatus);
            btnPrev=v.findViewById(R.id.btnPrevStatus);
            btnNext=v.findViewById(R.id.btnNextStatus);
            btnView=v.findViewById(R.id.btnViewAdminDetails);
        }
    }
}
