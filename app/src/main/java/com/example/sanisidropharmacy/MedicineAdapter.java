package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine m = medicineList.get(position);

        holder.txtName.setText(m.getName());
        holder.txtCategory.setText(m.getCategory());
        holder.txtPrice.setText("₱" + m.getPrice());
        holder.txtStock.setText("Stock: " + m.getStock());
        holder.txtPrescription.setText(m.isPrescription() ? "Requires prescription" : "OTC");

        if (m.getImageUrl() != null && !m.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(m.getImageUrl()))
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.pharmacy_logo);
        }

        // ✅ Clicking opens MedicineDetailActivity with ALL required data
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailActivity.class);
            intent.putExtra("name", m.getName());
            intent.putExtra("category", m.getCategory());
            intent.putExtra("price", m.getPrice());
            intent.putExtra("description", m.getDescription());
            intent.putExtra("dosage", m.getDosage());
            intent.putExtra("image", m.getImageUrl());
            intent.putExtra("stock", m.getStock());
            intent.putExtra("prescription", m.isPrescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView txtName, txtCategory, txtPrice, txtStock, txtPrescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtName = itemView.findViewById(R.id.txtName);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtStock = itemView.findViewById(R.id.txtStock);
            txtPrescription = itemView.findViewById(R.id.txtPrescription);
        }
    }
}
