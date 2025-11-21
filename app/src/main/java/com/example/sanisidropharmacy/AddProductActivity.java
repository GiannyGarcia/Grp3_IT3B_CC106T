package com.example.sanisidropharmacy; // adapt package

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

    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    private static final int REQUEST_PICK_IMAGE = 1001;

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

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CATEGORY_NAME)) {
            categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        } else {
            categoryName = "";
        }

        // Ensure bottom nav is on top and clickable
        View bottomNav = findViewById(R.id.bottom_nav);
        if (bottomNav != null) {
            bottomNav.bringToFront();
            bottomNav.setClickable(true);
            bottomNav.setFocusable(true);
            bottomNav.setFocusableInTouchMode(true);
        }

        // Wire bottom nav buttons
        navHome.setOnClickListener(v -> {
            Intent i = new Intent(AddProductActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        });

        navCart.setOnClickListener(v -> {
            Intent i = new Intent(AddProductActivity.this, CartActivity.class);
            startActivity(i);
        });

        navUser.setOnClickListener(v -> {
            Intent i = new Intent(AddProductActivity.this, UserProfileActivity.class);
            startActivity(i);
        });

        uploadImageButton.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQUEST_PICK_IMAGE);
        });

        expiryDateButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(AddProductActivity.this, (view, year, month, dayOfMonth) -> {
                expiryDateButton.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        saveProductButton.setOnClickListener(v -> {
            String name = productNameInput.getText().toString().trim();
            String desc = productDescriptionInput.getText().toString().trim();
            String intake = productIntakeInput.getText().toString().trim();
            String priceText = productPriceInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Please enter product name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (priceText.isEmpty()) {
                Toast.makeText(AddProductActivity.this, "Please enter product price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Price formatting
            String priceDisplay;
            try {
                double priceVal = Double.parseDouble(priceText);
                priceDisplay = String.format("₱%.2f", priceVal);
            } catch (Exception e) {
                priceDisplay = priceText; // fallback
            }

            // Prepare result Intent with product data (CategoryDetailsActivity will receive)
            Intent out = new Intent();
            out.putExtra("NEW_PRODUCT_NAME", name);
            out.putExtra("NEW_PRODUCT_DESC", desc);
            out.putExtra("NEW_PRODUCT_PRICE", priceDisplay);
            out.putExtra("NEW_PRODUCT_CATEGORY", categoryName);

            if (imageUri != null) {
                out.putExtra("NEW_PRODUCT_IMAGE_URI", imageUri.toString());
            }

            setResult(RESULT_OK, out);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Make sure bottom nav remains clickable after any view changes
        View bottomNav = findViewById(R.id.bottom_nav);
        if (bottomNav != null) {
            bottomNav.bringToFront();
            bottomNav.setClickable(true);
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
