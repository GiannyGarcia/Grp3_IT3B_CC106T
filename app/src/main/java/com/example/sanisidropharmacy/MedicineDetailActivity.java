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

    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDescription, detailDosage,
            detailCategory, detailStock, detailPrescription;
    private Button btnAddToCart;

    private Product currentProduct;

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

        // PRICE
        String priceString = getIntent().getStringExtra("price");
        double price = 0.0;
        if (priceString != null) {
            try {
                price = Double.parseDouble(priceString.replace("₱", "").trim());
            } catch (Exception ignored) {}
        }

        // SAFE DEFAULTS
        if (brand == null) brand = "Unknown";
        if (expiryDate == null) expiryDate = "N/A";
        if (imageUrl == null) imageUrl = "";

        // -----------------------------
        // RE-CREATE PRODUCT OBJECT — NO ERRORS
        // -----------------------------
        currentProduct = new Product(
                id,                                                     // int id
                name,                                                   // name
                price,                                                  // price
                stock,                                                  // stock
                expiryDate,                                             // expiryDate
                category,                                               // category
                brand,                                                  // brand
                prescription ? "Prescription Required" : "OTC",         // prescriptionType
                imageUrl,                                               // imageUrl
                description                                             // description
        );

        // -----------------------------
        // SET UI
        // -----------------------------
        detailName.setText(name);
        detailPrice.setText(String.format("₱%.2f", price));
        detailDescription.setText(description);
        detailDosage.setText("Dosage: " + (dosage != null ? dosage : "N/A"));
        detailCategory.setText(category);
        detailStock.setText("Stock: " + stock);
        detailPrescription.setText(prescription ? "Requires Prescription" : "OTC");

        // IMAGE
        if (!imageUrl.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUrl)).into(detailImage);
        } else {
            detailImage.setImageResource(R.drawable.pharmacy_logo);
        }

        // OUT OF STOCK
        if (stock <= 0) {
            btnAddToCart.setEnabled(false);
            btnAddToCart.setText("OUT OF STOCK");
        }

        // -----------------------------
        // ADD TO CART
        // -----------------------------
        btnAddToCart.setOnClickListener(v -> {
            CartStorage.addItem(MedicineDetailActivity.this, currentProduct, 1);

            Toast.makeText(this,
                    currentProduct.getName() + " added to cart!",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, CartActivity.class));
        });

        // -----------------------------
        // BOTTOM NAV
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
