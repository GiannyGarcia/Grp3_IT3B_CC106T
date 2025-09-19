package com.example.sanisidropharmacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> implements Filterable {

    private List<Medicine> medicineList;
    private List<Medicine> medicineListFull;
    private OnMedicineClickListener listener;

    public interface OnMedicineClickListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineAdapter(List<Medicine> medicineList, OnMedicineClickListener listener) {
        this.medicineList = medicineList;
        this.medicineListFull = new ArrayList<>(medicineList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.medicinePrice.setText(medicine.getPrice());
        holder.medicineImage.setImageResource(medicine.getImageResId());

        holder.itemView.setOnClickListener(v -> listener.onMedicineClick(medicine));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    @Override
    public Filter getFilter() {
        return medicineFilter;
    }

    private final Filter medicineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Medicine> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(medicineListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Medicine item : medicineListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            medicineList.clear();
            medicineList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicinePrice;
        ImageView medicineImage;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            medicinePrice = itemView.findViewById(R.id.medicinePrice);
            medicineImage = itemView.findViewById(R.id.medicineImage);
        }
    }
}
