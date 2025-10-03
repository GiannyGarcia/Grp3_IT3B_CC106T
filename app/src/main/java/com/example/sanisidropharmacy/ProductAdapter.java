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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Medicine> medicineList;

    public ProductAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout that actually exists in res/layout/
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Medicine med = medicineList.get(position);
        holder.medicineName.setText(med.getName());
        holder.medicinePrice.setText("₱" + med.getPrice());
        holder.prescription.setText("Prescription: " + med.getPrescriptionType());
        holder.medicineStock.setText("Stock: " + med.getStock());

        Glide.with(context)
                .load(med.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_report_image) // fallback if no custom drawable
                .into(holder.medicineImage);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicinePrice, prescription, medicineStock;
        ImageView medicineImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            medicinePrice = itemView.findViewById(R.id.medicinePrice);
            prescription = itemView.findViewById(R.id.medicinePrescription);
            medicineStock = itemView.findViewById(R.id.medicineStock);
            medicineImage = itemView.findViewById(R.id.medicineImage);
        }
    }
}
