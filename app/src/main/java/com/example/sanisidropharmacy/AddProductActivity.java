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

    private static final int REQUEST_PICK_IMAGE = 1001;

    private ImageView productImageView;
    private EditText productNameInput, productDescriptionInput, productIntakeInput, productPriceInput;
    private Spinner brandSpinner;
    private Button uploadImageButton, expiryDateButton, saveProductButton;

    private Uri imageUri;
    private String categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize inputs
        productImageView = findViewById(R.id.productImageView);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        brandSpinner = findViewById(R.id.brandSpinner);

        // Buttons
        uploadImageButton = findViewById(R.id.uploadImageButton);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        saveProductButton = findViewById(R.id.saveProductButton);

        // Retrieve category from FAB intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName == null || categoryName.equals("All Products")) {
            categoryName = "Uncategorized";
        }

        // Normalize category
        categoryName = normalizeCategory(categoryName);

        uploadImageButton.setOnClickListener(v -> pickImageFromGallery());
        expiryDateButton.setOnClickListener(v -> openDatePicker());
        saveProductButton.setOnClickListener(v -> saveProduct());
    }

    // ---------------------------------------------------------
    // CATEGORY NORMALIZER (Critical Fix)
    // ---------------------------------------------------------
    private String normalizeCategory(String c) {
        if (c == null) return "Uncategorized";

        if (c.contains("Prescription Medicines")) return "Prescription Medicines";
        if (c.contains("Non-Prescription")) return "Non-Prescription Products";
        if (c.contains("Non-Intake")) return "Non-Intake Products";
        if (c.contains("Device")) return "Device Monitoring Products";

        return "Uncategorized";
    }

    // ---------------------------------------------------------
    // IMAGE PICKER
    // ---------------------------------------------------------
    private void pickImageFromGallery() {
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, REQUEST_PICK_IMAGE);
    }

    // ---------------------------------------------------------
    // DATE PICKER
    // ---------------------------------------------------------
    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) ->
                        expiryDateButton.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // ---------------------------------------------------------
    // SAVE PRODUCT (Final Fix)
    // ---------------------------------------------------------
    private void saveProduct() {

        String name = productNameInput.getText().toString().trim();
        String desc = productDescriptionInput.getText().toString().trim();
        String intake = productIntakeInput.getText().toString().trim();
        String priceText = productPriceInput.getText().toString().trim();
        String expiry = expiryDateButton.getText().toString().trim();
        String brand = brandSpinner.getSelectedItem() != null ?
                brandSpinner.getSelectedItem().toString() : "Unknown";

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceText.isEmpty()) {
            Toast.makeText(this, "Please enter product price", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Product (uses your correct 10-argument constructor)
        Product newProduct = new Product(
                0,
                name,
                price,
                0,
                expiry,
                categoryName,
                brand,
                categoryName.contains("Prescription") ? "Prescription Required" : "OTC",
                imageUri != null ? imageUri.toString() : "",
                desc
        );

        // Save to ProductManager (not JSON!)
        ProductManager.getInstance().addProduct(newProduct);
        ProductManager.getInstance().saveProductsToSharedPreferences(this);

        Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    // ---------------------------------------------------------
    // IMAGE PICKER RESULT
    // ---------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
