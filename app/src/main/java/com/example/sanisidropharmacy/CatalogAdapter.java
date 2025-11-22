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

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    private final Context context;
    private final List<MedicineModel> list;

    public CatalogAdapter(Context ctx, List<MedicineModel> data) {
        this.context = ctx;
        this.list = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_medicine_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        MedicineModel m = list.get(pos);

        h.name.setText(m.getName());
        h.category.setText(m.getCategory());
        h.price.setText("₱" + (m.getPrice() != null ? m.getPrice() : "0.00"));

        String imageUrl = m.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(imageUrl))
                    .into(h.image);
        } else {
            h.image.setImageResource(R.drawable.pharmacy_logo);
        }

        // Click: open detail
        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailActivity.class);
            intent.putExtra("name", m.getName());
            intent.putExtra("price", m.getPrice());
            intent.putExtra("description", m.getDescription());
            intent.putExtra("dosage", m.getDosage());
            intent.putExtra("category", m.getCategory());
            intent.putExtra("stock", m.getStock());
            intent.putExtra("prescription", m.isPrescription());
            intent.putExtra("image", m.getImage());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, category, price;

        public ViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.medicineImage);
            name = v.findViewById(R.id.medicineName);
            category = v.findViewById(R.id.medicineCategory);
            price = v.findViewById(R.id.medicinePrice);
        }
    }
}
