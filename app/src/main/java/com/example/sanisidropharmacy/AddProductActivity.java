package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView productImageView;
    private EditText productNameInput, productDescriptionInput, productIntakeInput, productPriceInput;
    private Button expiryDateButton, saveButton, uploadImageButton;
    private Spinner brandSpinner;

    private ImageView navHome, navCart, navUser;
    private String expiryDate = "";
    private Uri imageUri = null;
    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Get category from intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        // Initialize product views
        productImageView = findViewById(R.id.productImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        brandSpinner = findViewById(R.id.brandSpinner);
        saveButton = findViewById(R.id.saveProductButton);

        // Populate brand spinner
        String[] brands = {"Generic", "Branded"};
        brandSpinner.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands));

        // Button listeners
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        expiryDateButton.setOnClickListener(v -> showDatePicker());
        saveButton.setOnClickListener(v -> saveProduct());

        // Bottom navigation
        View bottomNav = findViewById(R.id.include_bottom_nav);
        navHome = bottomNav.findViewById(R.id.nav_home);
        navCart = bottomNav.findViewById(R.id.nav_cart);
        navUser = bottomNav.findViewById(R.id.nav_user);
        setupBottomNav(navHome, navCart, navUser);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
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
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    expiryDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    expiryDateButton.setText(expiryDate);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveProduct() {
        String name = productNameInput.getText().toString().trim();
        String desc = productDescriptionInput.getText().toString().trim();
        String intake = productIntakeInput.getText().toString().trim();
        String priceStr = productPriceInput.getText().toString().trim();
        String brand = brandSpinner.getSelectedItem().toString();

        if (name.isEmpty() || desc.isEmpty() || intake.isEmpty() || priceStr.isEmpty() || expiryDate.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove peso sign if user typed it
        priceStr = priceStr.replace("₱", "").trim();
        double price = Double.parseDouble(priceStr);

        // Create medicine object
        Medicine newMedicine = new Medicine(name, desc, intake, price, expiryDate, categoryName, brand, imageUri.toString());

        // Add product to correct category list
        switch (categoryName) {
            case "Prescription Medicines":
                DataStorage.prescriptionList.add(newMedicine);
                break;
            case "Non-Prescription Medicines":
                DataStorage.nonPrescriptionList.add(newMedicine);
                break;
            case "Non-Intake Products":
                DataStorage.nonIntakeList.add(newMedicine);
                break;
            case "Device or Monitoring Products":
                DataStorage.deviceList.add(newMedicine);
                break;
        }

        Toast.makeText(this, "Product added to " + categoryName, Toast.LENGTH_SHORT).show();

        // Return to CategoryDetailsActivity
        Intent intent = new Intent(AddProductActivity.this, CategoryDetailsActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav(ImageView home, ImageView cart, ImageView user) {
        home.setOnClickListener(v -> startActivity(new Intent(this, CatalogActivity.class)));
        cart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        user.setOnClickListener(v -> startActivity(new Intent(this, UserProfileActivity.class)));
    }
}
