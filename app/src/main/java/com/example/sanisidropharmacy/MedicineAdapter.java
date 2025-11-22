package com.example.sanisidropharmacy;

import android.content.Context;
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
    private List<MedicineModel> medicineList;
    private OnItemClickListener listener;

    // -------------------------------------------
    // Constructor
    // -------------------------------------------
    public MedicineAdapter(List<MedicineModel> medicineList, Context context, OnItemClickListener listener) {
        this.context = context;
        this.medicineList = medicineList;
        this.listener = listener;
    }

    // -------------------------------------------
    // Interface for item click
    // -------------------------------------------
    public interface OnItemClickListener {
        void onItemClick(MedicineModel item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicineModel m = medicineList.get(position);

        holder.txtName.setText(m.getName());
        holder.txtCategory.setText(m.getCategory());
        holder.txtPrice.setText("₱" + m.getPrice());
        holder.txtStock.setText("Stock: " + m.getStock());
        holder.txtPrescription.setText(m.isPrescription() ? "Requires prescription" : "OTC");

        // Load image
        if (m.getImage() != null && !m.getImage().isEmpty()) {
            Glide.with(context)
                    .load(m.getImage())
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.pharmacy_logo);
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(m);
            }
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
