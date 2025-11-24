package com.example.sanisidropharmacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MedicineDetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDescription, detailDosage,
            detailCategory, detailStock, detailPrescription;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        // -----------------------------
        // VIEW BINDINGS
        // -----------------------------
        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailPrice = findViewById(R.id.detailPrice);
        detailDescription = findViewById(R.id.detailDescription);
        detailDosage = findViewById(R.id.detailDosage);
        detailCategory = findViewById(R.id.detailCategory);
        detailStock = findViewById(R.id.detailStock);
        detailPrescription = findViewById(R.id.detailPrescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // -----------------------------
        // GET DATA FROM INTENT
        // -----------------------------
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String dosage = getIntent().getStringExtra("dosage");
        String category = getIntent().getStringExtra("category");
        int stock = getIntent().getIntExtra("stock", 0);
        boolean prescription = getIntent().getBooleanExtra("prescription", false);
        String imageUri = getIntent().getStringExtra("image");

        // -----------------------------
        // SET UI VALUES
        // -----------------------------
        detailName.setText(name != null ? name : "Unknown");
        detailPrice.setText(price != null ? "₱" + price : "₱0.00");
        detailDescription.setText(description != null ? description : "No description available.");
        detailDosage.setText("Dosage: " + (dosage != null ? dosage : "N/A"));
        detailCategory.setText(category != null ? category : "Unknown Category");
        detailStock.setText("Stock: " + stock);
        detailPrescription.setText(prescription ? "Requires Prescription" : "OTC");

        // Load image safely
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUri)).into(detailImage);
        } else {
            detailImage.setImageResource(R.drawable.pharmacy_logo);
        }

        // -----------------------------
        // ADD TO CART LOGIC
        // -----------------------------
        btnAddToCart.setOnClickListener(v -> {

            CartModel item = new CartModel(
                    name,
                    price,
                    description,
                    dosage,
                    category,
                    stock,
                    prescription,
                    imageUri,
                    1
            );

            CartStorage.addToCart(item);

            // Move to cart screen
            Intent goToCart = new Intent(this, CartActivity.class);
            startActivity(goToCart);
        });


        // -----------------------------
        // BOTTOM NAVIGATION
        // -----------------------------
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navCart = findViewById(R.id.nav_cart);
        ImageView navUser = findViewById(R.id.nav_user);

        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
