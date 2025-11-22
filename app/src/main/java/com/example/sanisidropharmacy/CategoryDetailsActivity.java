package com.example.sanisidropharmacy;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CategoryDetailsActivity extends AppCompatActivity {

    private ImageView detailImage;
    private TextView detailName, detailCategory, detailPrice, detailDescription, detailDosage, detailStock;
    private Button btnAddToCart;

    private String name, category, price, description, dosage, stock, imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        detailImage = findViewById(R.id.detailImage);
        detailName = findViewById(R.id.detailName);
        detailCategory = findViewById(R.id.detailCategory);
        detailPrice = findViewById(R.id.detailPrice);
        detailDescription = findViewById(R.id.detailDescription);
        detailDosage = findViewById(R.id.detailDosage);
        detailStock = findViewById(R.id.detailStock);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // get data
        name = getIntent().getStringExtra("name");
        category = getIntent().getStringExtra("category");
        price = getIntent().getStringExtra("price");
        description = getIntent().getStringExtra("description");
        dosage = getIntent().getStringExtra("dosage");
        stock = getIntent().getStringExtra("stock");
        imageUri = getIntent().getStringExtra("image");

        // assign values
        detailName.setText(name);
        detailCategory.setText(category);
        detailPrice.setText("₱" + price);
        detailDescription.setText(description);
        detailDosage.setText("Dosage: " + dosage);
        detailStock.setText("Stock: " + stock);

        // image
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(imageUri))
                    .into(detailImage);
        } else {
            detailImage.setImageResource(R.drawable.pharmacy_logo);
        }

        // add to cart
        btnAddToCart.setOnClickListener(v -> showQuantityDialog());

        // Bottom nav
        BottomNavHelper.attach(this);
    }

    private void showQuantityDialog() {
        final String[] qtyOptions = {"1", "2", "3", "4", "5"};

        new AlertDialog.Builder(this)
                .setTitle("Select Quantity")
                .setItems(qtyOptions, (dialog, which) -> {
                    String qty = qtyOptions[which];
                    CartStorage.addToCart(name, price, qty, imageUri, category);
                })
                .show();
    }
}
