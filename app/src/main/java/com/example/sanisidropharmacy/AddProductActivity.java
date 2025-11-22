package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1001;
    private static final String PREFS_NAME = "PharmacyProducts";
    private static final String KEY_PRODUCTS = "ProductsList";

    private ImageView productImageView;
    private EditText productNameInput, productDescriptionInput, productIntakeInput, productPriceInput;
    private Spinner brandSpinner;
    private Button uploadImageButton, expiryDateButton, saveProductButton;

    private ImageView navCart, navHome, navUser;

    private Uri imageUri;
    private String categoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productImageView = findViewById(R.id.productImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        brandSpinner = findViewById(R.id.brandSpinner);
        saveProductButton = findViewById(R.id.saveProductButton);

        navCart = findViewById(R.id.nav_cart);
        navHome = findViewById(R.id.nav_home);
        navUser = findViewById(R.id.nav_user);

        if (getIntent() != null && getIntent().hasExtra("CATEGORY_NAME")) {
            categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        }

        uploadImageButton.setOnClickListener(v -> pickImageFromGallery());

        expiryDateButton.setOnClickListener(v -> openDatePicker());

        saveProductButton.setOnClickListener(v -> saveProduct());
    }

    private void pickImageFromGallery() {
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, REQUEST_PICK_IMAGE);
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            expiryDateButton.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveProduct() {

        String name = productNameInput.getText().toString().trim();
        String desc = productDescriptionInput.getText().toString().trim();
        String intake = productIntakeInput.getText().toString().trim();
        String priceText = productPriceInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceText.isEmpty()) {
            Toast.makeText(this, "Please enter product price", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject product = new JSONObject();
        try {
            product.put("name", name);
            product.put("category", categoryName);
            product.put("price", priceText);
            product.put("description", desc);
            product.put("dosage", intake);
            product.put("stock", 0);
            product.put("prescription", categoryName.contains("Prescription"));
            product.put("imageUri", imageUri != null ? imageUri.toString() : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveToStorage(product);

        Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveToStorage(JSONObject productObj) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String existing = prefs.getString(KEY_PRODUCTS, "[]");

        try {
            JSONArray arr = new JSONArray(existing);
            arr.put(productObj);

            prefs.edit().putString(KEY_PRODUCTS, arr.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                productImageView.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
