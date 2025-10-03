package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView productImageView;
    private EditText productNameInput, productPriceInput, productStockInput;
    private Button expiryDateButton, saveButton, uploadImageButton;
    private Spinner categorySpinner, brandSpinner;

    private String expiryDate = "";
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImageView = findViewById(R.id.productImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        productNameInput = findViewById(R.id.productNameInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        productStockInput = findViewById(R.id.productStockInput);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        brandSpinner = findViewById(R.id.brandSpinner);
        saveButton = findViewById(R.id.saveProductButton);

        // Setup category spinner
        String[] categories = {"Requires Prescription", "Non-Prescription", "Others"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);

        // Setup brand spinner
        String[] brands = {"Generic", "Branded"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands);
        brandSpinner.setAdapter(brandAdapter);

        // Upload image
        uploadImageButton.setOnClickListener(v -> openImageChooser());

        // Expiry date picker
        expiryDateButton.setOnClickListener(v -> showDatePicker());

        // Save product
        saveButton.setOnClickListener(v -> saveProduct());
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int year1, int month1, int dayOfMonth) -> {
                    expiryDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    expiryDateButton.setText(expiryDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveProduct() {
        String name = productNameInput.getText().toString().trim();
        String priceStr = productPriceInput.getText().toString().trim();
        String stockStr = productStockInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String brand = brandSpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || expiryDate.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        Medicine medicine = new Medicine(name, price, stock, expiryDate, category, brand, imageUri.toString());

        DataStorage.medicineList.add(medicine);

        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddProductActivity.this, CatalogActivity.class);
        startActivity(intent);
        finish();
    }
}
