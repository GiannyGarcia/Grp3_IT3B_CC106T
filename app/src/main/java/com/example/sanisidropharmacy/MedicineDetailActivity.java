package com.example.sanisidropharmacy;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MedicineDetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDescription, detailDosage,
            detailCategory, detailStock, detailPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        // Bind views
        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailPrice = findViewById(R.id.detailPrice);
        detailDescription = findViewById(R.id.detailDescription);
        detailDosage = findViewById(R.id.detailDosage);
        detailCategory = findViewById(R.id.detailCategory);
        detailStock = findViewById(R.id.detailStock);
        detailPrescription = findViewById(R.id.detailPrescription);

        // Retrieve data
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String dosage = getIntent().getStringExtra("dosage");
        String category = getIntent().getStringExtra("category");
        int stock = getIntent().getIntExtra("stock", 0);
        boolean prescription = getIntent().getBooleanExtra("prescription", false);
        String imageUri = getIntent().getStringExtra("image");

        // Set text
        detailName.setText(name != null ? name : "Unknown medicine");
        detailPrice.setText(price != null ? "₱" + price : "₱0.00");
        detailDescription.setText(description != null ? description : "No description available");
        detailDosage.setText(dosage != null ? dosage : "N/A");
        detailCategory.setText(category != null ? category : "Unknown Category");
        detailStock.setText("Stock: " + stock);
        detailPrescription.setText(prescription ? "Requires prescription" : "OTC");

        // Load image correctly (URI or fallback)
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .into(detailImage);
        } else {
            detailImage.setImageResource(R.drawable.pharmacy_logo);
        }
    }
}
