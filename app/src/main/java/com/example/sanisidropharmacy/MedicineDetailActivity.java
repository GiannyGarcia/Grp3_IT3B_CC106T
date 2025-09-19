package com.example.sanisidropharmacy;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        ImageView image = findViewById(R.id.detailImage);
        TextView name = findViewById(R.id.detailName);
        TextView price = findViewById(R.id.detailPrice);
        TextView description = findViewById(R.id.detailDescription);
        TextView dosage = findViewById(R.id.detailDosage);

        // Get data from intent safely
        int imgRes = getIntent().getIntExtra("image", R.drawable.ic_medicine_placeholder); // default image
        String medName = getIntent().getStringExtra("name");
        String medPrice = getIntent().getStringExtra("price");
        String medDescription = getIntent().getStringExtra("description");
        String medDosage = getIntent().getStringExtra("dosage");

        // Set values safely
        image.setImageResource(imgRes);
        name.setText(medName != null ? medName : "Unknown medicine");
        price.setText(medPrice != null ? medPrice : "N/A");
        description.setText(medDescription != null ? medDescription : "No description available");
        dosage.setText("Dosage: " + (medDosage != null ? medDosage : "N/A"));
    }
}
