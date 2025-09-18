package com.example.sanisidropharmacy;

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

        holder.nameTextView.setText(medicine.getName());
        holder.categoryTextView.setText(medicine.getCategory());
        holder.priceTextView.setText("₱" + medicine.getPrice());
        holder.imageView.setImageResource(medicine.getImageResId());

        // ✅ Use corrected method
        if (medicine.isPrescriptionRequired()) {
            holder.prescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            holder.prescriptionTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, categoryTextView, priceTextView, prescriptionTextView;
        ImageView imageView;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.medicineName);
            categoryTextView = itemView.findViewById(R.id.medicineCategory);
            priceTextView = itemView.findViewById(R.id.medicinePrice);
            prescriptionTextView = itemView.findViewById(R.id.medicinePrescription);
            imageView = itemView.findViewById(R.id.medicineImage);
        }
    }
}
