package com.example.sanisidropharmacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View; // Import View for the new findViewById
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

// Renamed the class to reflect its function (Posting a Product)
public class ProductPostActivity extends AppCompatActivity {

    // UI elements
    private ImageView productImagePreview;
    private Button buttonUploadImage, buttonPostProduct, buttonLogout;
    // ⭐ NEW BUTTON FIELD ⭐
    private Button buttonInventoryManager;
    private EditText inputProductLabel, inputQuantity, inputExpirationDate,
            inputDescription, inputPrice;

    private Spinner spinnerCategory;
    private EditText inputBrand;
    private Spinner spinnerPrescriptionType;

    // Image URI and Request Codes
    private Uri selectedImageUri = null;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int PERMISSION_REQUEST_CODE = 200; // For runtime permissions

    private static final String[] CATEGORIES = new String[]{
            "Prescription Medicines",
            "Non-Prescription Products",
            "Non-Intake Products",
            "Device Monitoring Products"
    };

    private static final String[] PRESCRIPTION_TYPES = new String[] {
            "Non-Prescription",
            "Prescription Required"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: Make sure to update your AndroidManifest.xml and Layout files
        // if you changed the layout file name from the default used here.
        setContentView(R.layout.product_post_activty);

        // Initialize UI
        productImagePreview = findViewById(R.id.productImagePreview);
        buttonUploadImage = findViewById(R.id.buttonUploadImage);
        buttonPostProduct = findViewById(R.id.buttonPostProduct);
        buttonLogout = findViewById(R.id.buttonLogout);

        // ⭐ NEW BUTTON INITIALIZATION ⭐
        buttonInventoryManager = findViewById(R.id.buttonInventoryManager);

        inputProductLabel = findViewById(R.id.inputProductLabel);
        inputQuantity = findViewById(R.id.inputQuantity);
        inputExpirationDate = findViewById(R.id.inputExpirationDate);
        inputDescription = findViewById(R.id.inputDescription);
        inputPrice = findViewById(R.id.inputPrice);

        inputBrand = findViewById(R.id.inputBrand);
        spinnerPrescriptionType = findViewById(R.id.spinnerPrescriptionType);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        setupCategorySpinner();
        setupPrescriptionSpinner();

        // Set Listeners
        buttonUploadImage.setOnClickListener(v -> openImagePicker());
        buttonPostProduct.setOnClickListener(v -> saveProduct());
        buttonLogout.setOnClickListener(v -> logoutUser());

        // ⭐ NEW LISTENER FOR INVENTORY NAVIGATION ⭐
        buttonInventoryManager.setOnClickListener(v -> navigateToInventoryTracker());
    }

    /**
     * ⭐ NEW METHOD: Handles navigation to the Inventory Tracker screen. ⭐
     */
    private void navigateToInventoryTracker() {
        // You should replace InventoryTrackerActivity.class with the actual class
        // name of your inventory management screen.
        Intent intent = new Intent(ProductPostActivity.this, InventoryTrackerActivity.class);
        startActivity(intent);
        // Do not finish() this activity if you want the admin to easily return here
        // to continue posting products.
    }

    // --- SPINNER SETUP METHODS (Unchanged) ---
    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setupPrescriptionSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PRESCRIPTION_TYPES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrescriptionType.setAdapter(adapter);
    }

    // --- LOGOUT METHOD (Unchanged) ---
    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("loggedInUser");
        editor.apply();

        Intent intent = new Intent(ProductPostActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // --- IMAGE PICKER METHODS (Unchanged) ---
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                productImagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // --- SAVE PRODUCT METHOD (Unchanged) ---
    private void saveProduct() {
        // 1. Get all inputs
        String name = inputProductLabel.getText().toString().trim();
        String quantityStr = inputQuantity.getText().toString().trim();
        String expirationDate = inputExpirationDate.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        String priceStr = inputPrice.getText().toString().trim();
        String brand = inputBrand.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String prescriptionType = spinnerPrescriptionType.getSelectedItem().toString();
        String imageUrl = selectedImageUri != null ? selectedImageUri.toString() : "";

        // 2. Validation
        if (name.isEmpty() || quantityStr.isEmpty() || expirationDate.isEmpty() ||
                description.isEmpty() || priceStr.isEmpty() || brand.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock;
        double price;
        try {
            stock = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity or Price format invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Create the Product object
        final Product newProduct = new Product(
                0, // ID
                name,
                price,
                stock,
                expirationDate,
                category,
                brand,
                prescriptionType,
                imageUrl,
                description
        );

        // 4. SAVE TO SINGLETON (In-memory)
        ProductManager.getInstance().addProduct(newProduct);

        // PERSIST THE ENTIRE LIST TO DISK
        ProductManager.getInstance().saveProductsToSharedPreferences(this);

        Toast.makeText(this, name + " successfully posted and saved!", Toast.LENGTH_LONG).show();

        clearFields();
    }

    // --- clearFields (Unchanged) ---
    private void clearFields() {
        inputProductLabel.setText("");
        inputQuantity.setText("");
        inputExpirationDate.setText("");
        inputDescription.setText("");
        inputPrice.setText("");
        inputBrand.setText("");
        productImagePreview.setImageResource(0);
        selectedImageUri = null;
        spinnerCategory.setSelection(0);
        spinnerPrescriptionType.setSelection(0);
    }
}