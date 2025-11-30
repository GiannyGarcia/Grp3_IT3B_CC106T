package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    private Uri imageUri;

    private ImageView productImageView;
    private EditText productNameInput, productDescriptionInput, productIntakeInput, productPriceInput;
    private Spinner brandSpinner;
    private Button expiryDateButton, saveProductButton;

    private String categoryName = "Uncategorized";
    private static final int PICK_IMAGE = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImageView = findViewById(R.id.productImageView);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        brandSpinner = findViewById(R.id.brandSpinner);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        saveProductButton = findViewById(R.id.saveProductButton);

        if (getIntent().hasExtra("CATEGORY_NAME"))
            categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        productImageView.setOnClickListener(v -> pickImage());
        expiryDateButton.setOnClickListener(v -> pickDate());
        saveProductButton.setOnClickListener(v -> saveProduct());

        BottomNavHelper.setup(this);
    }

    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }

    private void pickDate() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (vw, y, m, d) ->
                expiryDateButton.setText(y + "-" + (m + 1) + "-" + d),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveProduct() {

        String name = productNameInput.getText().toString().trim();
        String desc = productDescriptionInput.getText().toString().trim();
        String priceText = productPriceInput.getText().toString().trim();
        String expiry = expiryDateButton.getText().toString().trim();
        String brand = brandSpinner.getSelectedItem() != null ?
                brandSpinner.getSelectedItem().toString() : "Unknown";

        if (name.isEmpty()) {
            Toast.makeText(this, "Enter product name", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try { price = Double.parseDouble(priceText); }
        catch (Exception e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        Product p = new Product(
                0,
                name,
                price,
                10,             // Default stock so it doesn't appear OUT OF STOCK
                expiry,
                categoryName,
                brand,
                categoryName.contains("Prescription") ? "Prescription Required" : "OTC",
                imageUri != null ? imageUri.toString() : "",
                desc
        );

        ProductManager.getInstance().addProduct(p);
        ProductManager.getInstance().saveProductsToSharedPreferences(this);

        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImageView.setImageBitmap(bm);
            } catch (IOException ignored) {}
        }
    }
}
