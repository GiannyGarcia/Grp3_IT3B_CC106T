package com.example.sanisidropharmacy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // ⭐ NEW IMPORT

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MedicineDetailActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDescription, detailDosage,
            detailCategory, detailStock, detailPrescription;
    private Button btnAddToCart;

    // ⭐ NEW: We need an ID to uniquely identify the product
    private String productID;

    // ⭐ NEW: Store the product object for easy cart insertion
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
        // GET DATA FROM INTENT & PREPARE FOR CART
        // -----------------------------
        // ⭐ Fetch all data needed to recreate a Product object ⭐
        productID = getIntent().getStringExtra("id"); // Assume 'id' is passed
        String name = getIntent().getStringExtra("name");
        // Convert price back to double/float if it was stored as a String for display
        String priceString = getIntent().getStringExtra("price");
        double price = 0.0;
        try {
            if (priceString != null) {
                price = Double.parseDouble(priceString.replace("₱", "").trim());
            }
        } catch (NumberFormatException ignored) { }

        String description = getIntent().getStringExtra("description");
        String dosage = getIntent().getStringExtra("dosage");
        String category = getIntent().getStringExtra("category");
        int stock = getIntent().getIntExtra("stock", 0);
        boolean prescription = getIntent().getBooleanExtra("prescription", false);
        String imageUri = getIntent().getStringExtra("image");
        String brand = getIntent().getStringExtra("brand"); // Assuming brand is also needed/used

        // -----------------------------
        // RE-CREATE THE PRODUCT OBJECT
        // -----------------------------
        currentProduct = new Product(
                productID,
                name,
                brand,      // Assuming brand is defined in your Product constructor
                category,
                description,
                price,      // Use the parsed double price
                stock,
                imageUri,
                prescription // Use the boolean
        );

        // -----------------------------
        // SET UI VALUES
        // -----------------------------
        detailName.setText(name != null ? name : "Unknown");
        detailPrice.setText(String.format("₱%.2f", price)); // Use formatted price
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

        // Disable button if out of stock
        if (stock <= 0) {
            btnAddToCart.setEnabled(false);
            btnAddToCart.setText("OUT OF STOCK");
        }

        // -----------------------------
        // ADD TO CART LOGIC
        // -----------------------------
        btnAddToCart.setOnClickListener(v -> {
            int quantity = 1; // Assuming adding 1 unit by default

            // ⭐ CORRECTED CALL: Pass the Product object and the quantity ⭐
            // Assuming CartStorage.addItem(Product product, int quantity) is the correct signature
            CartStorage.addItem(currentProduct, quantity);

            Toast.makeText(this, currentProduct.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

            // Move to cart screen (FLAG_ACTIVITY_CLEAR_TOP ensures a clean transition)
            Intent goToCart = new Intent(this, CartActivity.class);
            goToCart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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