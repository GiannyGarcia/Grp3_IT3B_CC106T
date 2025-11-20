package com.example.sanisidropharmacy; // adjust package if different

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private final Context context;
    private final List<Medicine> medicineList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Medicine medicine);
    }

    public MedicineAdapter(Context context, List<Medicine> medicineList, OnItemClickListener listener) {
        this.context = context;
        this.medicineList = medicineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine m = medicineList.get(position);
        if (m == null) return;

        holder.name.setText(m.getName() != null ? m.getName() : "No name");
        holder.category.setText(m.getCategory() != null ? m.getCategory() : "");
        holder.price.setText(String.format("₱%.2f", m.getPrice()));
        holder.stock.setText("Stock: " + m.getStock());
        holder.prescription.setText(m.isPrescription() ? "Requires prescription" : "OTC");

        // image: if you store drawable names or resource ids, adapt here
        if (m.getImageResId() != 0) {
            holder.image.setImageResource(m.getImageResId());
        } else {
            // fallback thumbnail; keep a drawable placeholder in resources
            holder.image.setImageResource(R.drawable.pharmacy_logo);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(m);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList == null ? 0 : medicineList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView name, category, price, stock, prescription;
        ImageView image;

        MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicineName);
            category = itemView.findViewById(R.id.medicineCategory);
            price = itemView.findViewById(R.id.medicinePrice);
            stock = itemView.findViewById(R.id.medicineStock);
            prescription = itemView.findViewById(R.id.medicinePrescription);
            image = itemView.findViewById(R.id.medicineImage);
        }
    }
}
