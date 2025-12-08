package com.example.sanisidropharmacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopCustomerAdapter extends RecyclerView.Adapter<TopCustomerAdapter.VH> {
    private List<AdminLoyaltySummaryResponse.TopCustomer> list;
    public TopCustomerAdapter(List<AdminLoyaltySummaryResponse.TopCustomer> list){ this.list=list; }
    @Override public VH onCreateViewHolder(ViewGroup p,int v){ return new VH(LayoutInflater.from(p.getContext()).inflate(android.R.layout.simple_list_item_2,p,false)); }
    @Override public void onBindViewHolder(VH h,int pos){
        AdminLoyaltySummaryResponse.TopCustomer t = list.get(pos);
        h.tv1.setText(t.fullname);
        h.tv2.setText("Points: " + t.loyalty_points);
    }
    @Override public int getItemCount(){return list.size();}
    static class VH extends RecyclerView.ViewHolder {
        TextView tv1,tv2;
        VH(View v){ super(v); tv1=v.findViewById(android.R.id.text1); tv2=v.findViewById(android.R.id.text2); }
    }
}
