package com.example.sanisidropharmacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MedicineDetailActivity extends AppCompatActivity {

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        // VIEW BINDINGS
        ImageView detailImage = findViewById(R.id.detailImage);
        TextView detailName = findViewById(R.id.detailName);
        TextView detailPrice = findViewById(R.id.detailPrice);
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDosage = findViewById(R.id.detailDosage);
        TextView detailCategory = findViewById(R.id.detailCategory);
        TextView detailStock = findViewById(R.id.detailStock);
        TextView detailPrescription = findViewById(R.id.detailPrescription);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        // GET INTENT DATA
        int id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        String brand = getIntent().getStringExtra("brand");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");
        String dosage = getIntent().getStringExtra("dosage");
        String expiryDate = getIntent().getStringExtra("expiryDate");
        String imageUrl = getIntent().getStringExtra("image");
        boolean prescription = getIntent().getBooleanExtra("prescription", false);
        int stock = getIntent().getIntExtra("stock", 0);

        String priceString = getIntent().getStringExtra("price");
        double price = 0;
        if (priceString != null) {
            price = Double.parseDouble(priceString.replace("₱", "").trim());
        }

        if (brand == null) brand = "Unknown";
        if (expiryDate == null) expiryDate = "N/A";

        // BUILD PRODUCT OBJECT
        currentProduct = new Product(
                id,
                name,
                price,
                stock,
                expiryDate,
                category,
                brand,
                prescription ? "Prescription Required" : "OTC",
                imageUrl != null ? imageUrl : "",
                description
        );

        // SET UI
        detailName.setText(name);
        detailPrice.setText("₱" + price);
        detailDescription.setText(description);
        detailDosage.setText("Dosage: " + (dosage != null ? dosage : "N/A"));
        detailCategory.setText(category);
        detailStock.setText("Stock: " + stock);
        detailPrescription.setText(prescription ? "Requires Prescription" : "OTC");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUrl)).into(detailImage);
        }

        if (stock <= 0) {
            btnAddToCart.setEnabled(false);
            btnAddToCart.setText("OUT OF STOCK");
        }

        btnAddToCart.setOnClickListener(v -> {
            CartStorage.addItem(MedicineDetailActivity.this, currentProduct, 1);
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartActivity.class));
        });

        BottomNavHelper.setup(this);
    }
}
