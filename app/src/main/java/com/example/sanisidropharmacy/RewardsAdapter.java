package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.VH> {
    private List<RewardsResponse.Reward> list;
    private Context ctx;
    private static final String USER_PREFS = "user_prefs";

    public RewardsAdapter(List<RewardsResponse.Reward> list, Context ctx){ this.list=list; this.ctx=ctx; }

    @Override public VH onCreateViewHolder(ViewGroup p,int v){ return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_reward,p,false));}
    @Override public void onBindViewHolder(VH h,int pos){
        RewardsResponse.Reward r = list.get(pos);
        h.title.setText(r.title);
        h.points.setText(r.points_required + " pts");
        h.btn.setOnClickListener(v -> {
            SharedPreferences prefs = ctx.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            int userId = prefs.getInt("session_user_id", -1);
            if (userId == -1) { Toast.makeText(ctx, "Login required", Toast.LENGTH_SHORT).show(); return; }

            ApiService api = ApiClient.getRetrofit().create(ApiService.class);
            api.redeemReward(userId, r.id).enqueue(new Callback<BasicResponse>() {
                @Override public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                    if (!response.isSuccessful() || response.body()==null || !response.body().isSuccess()) {
                        Toast.makeText(ctx, "Redeem failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ctx, "Redeemed: " + r.title, Toast.LENGTH_SHORT).show();
                }
                @Override public void onFailure(Call<BasicResponse> call, Throwable t) {
                    Toast.makeText(ctx, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    @Override public int getItemCount(){ return list.size(); }
    static class VH extends RecyclerView.ViewHolder {
        TextView title, points; Button btn;
        VH(View v){ super(v); title=v.findViewById(R.id.tvRewardTitle); points=v.findViewById(R.id.tvRewardPoints); btn=v.findViewById(R.id.btnRedeemNow); }
    }
}
