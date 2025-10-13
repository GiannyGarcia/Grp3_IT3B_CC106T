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

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private Context context;
    private List<Medicine> originalList; // full list
    private List<Medicine> filteredList; // filtered list for search

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.originalList = medicineList;
        this.filteredList = new ArrayList<>(medicineList);
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = filteredList.get(position);

        holder.medicineName.setText(medicine.getName());
        holder.medicinePrice.setText("₱" + medicine.getPrice());
        holder.medicineStock.setText("Stock: " + medicine.getStock());
        holder.medicineCategory.setText("Category: " + medicine.getCategory());
        holder.medicinePrescription.setText("Prescription: " + medicine.getPrescriptionType());

        // Load image with Glide
        Glide.with(context)
                .load(medicine.getImageUrl())
                .placeholder(R.drawable.ic_medicine_placeholder)
                .into(holder.medicineImage);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // --- Filtering method ---
    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            text = text.toLowerCase();
            for (Medicine m : originalList) {
                if (m.getName().toLowerCase().contains(text)) {
                    filteredList.add(m);
                }
            }
        }
        notifyDataSetChanged();
    }

    // --- Add new medicine dynamically ---
    public void addMedicine(Medicine medicine) {
        originalList.add(medicine);
        filteredList.add(medicine);
        notifyItemInserted(filteredList.size() - 1);
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
