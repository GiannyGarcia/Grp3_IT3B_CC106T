package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
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
    private EditText productNameInput, productDescriptionInput, productIntakeInput, productPriceInput;
    private Button expiryDateButton, saveButton, uploadImageButton;
    private Spinner categorySpinner, brandSpinner;

    private ImageView navHome, navCart, navUser;
    private String expiryDate = "";
    private Uri imageUri = null;
    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔹 Check login
        if (!isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_add_product);

        // 🔹 Get category from intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        // 🔹 Initialize product views
        productImageView = findViewById(R.id.productImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        brandSpinner = findViewById(R.id.brandSpinner);
        saveButton = findViewById(R.id.saveProductButton);

        // 🔹 Populate spinners
        String[] categories = {"Prescription", "Non-Prescription", "Non-Intake", "Device"};
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        String[] brands = {"Generic", "Branded"};
        brandSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands));

        // 🔹 Button listeners
        uploadImageButton.setOnClickListener(v -> openImageChooser());
        expiryDateButton.setOnClickListener(v -> showDatePicker());
        saveButton.setOnClickListener(v -> saveProduct());

        // 🔹 Initialize Bottom Navigation from include
        View bottomNav = findViewById(R.id.include_bottom_nav);
        navHome = bottomNav.findViewById(R.id.nav_home);
        navCart = bottomNav.findViewById(R.id.nav_cart);
        navUser = bottomNav.findViewById(R.id.nav_user);
        setupBottomNav(navHome, navCart, navUser);
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                .getBoolean("isLoggedIn", false);
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
                (DatePicker view, int year, int month, int dayOfMonth) -> {
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
        String category = categorySpinner.getSelectedItem().toString();
        String brand = brandSpinner.getSelectedItem().toString();

        if (name.isEmpty() || desc.isEmpty() || intake.isEmpty() || priceStr.isEmpty() || expiryDate.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        Medicine newMedicine = new Medicine(name, desc, intake, price, expiryDate, category, brand, imageUri.toString());

        // Add product to correct category list
        switch (categoryName) {
            case "Prescription Medicines": DataStorage.prescriptionList.add(newMedicine); break;
            case "Non-Prescription Medicines": DataStorage.nonPrescriptionList.add(newMedicine); break;
            case "Non-Intake Products": DataStorage.nonIntakeList.add(newMedicine); break;
            case "Device or Monitoring Products": DataStorage.deviceList.add(newMedicine); break;
        }

        Toast.makeText(this, "Product added to " + categoryName, Toast.LENGTH_SHORT).show();

        // Return to CategoryDetailsActivity
        Intent intent = new Intent(AddProductActivity.this, CategoryDetailsActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        startActivity(intent);
        finish();
    }

    // 🔹 Bottom navigation helper
    private void setupBottomNav(ImageView home, ImageView cart, ImageView user) {
        home.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        user.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }
}
