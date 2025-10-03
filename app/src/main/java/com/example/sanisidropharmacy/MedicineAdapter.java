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

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.medicineName.setText(medicine.getName());
        holder.medicinePrice.setText("₱" + medicine.getPrice());
        holder.medicineStock.setText("Stock: " + medicine.getStock());
        holder.medicineCategory.setText("Category: " + medicine.getCategory());
        holder.medicinePrescription.setText("Prescription: " + medicine.getPrescriptionType());

        // Load image with Glide
        Glide.with(context)
                .load(medicine.getImageUrl())
                .placeholder(R.drawable.ic_medicine_placeholder) // fallback image
                .into(holder.medicineImage);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicinePrice, medicineStock, medicineCategory, medicinePrescription;
        ImageView medicineImage;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            medicinePrice = itemView.findViewById(R.id.medicinePrice);
            medicineStock = itemView.findViewById(R.id.medicineStock);
            medicineCategory = itemView.findViewById(R.id.medicineCategory);
            medicinePrescription = itemView.findViewById(R.id.medicinePrescription);
            medicineImage = itemView.findViewById(R.id.medicineImage);
        }
    }
}
