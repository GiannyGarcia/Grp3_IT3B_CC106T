package com.example.sanisidropharmacy; // adapt package

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

    public static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
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

        productImageView = findViewById(R.id.productImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productIntakeInput = findViewById(R.id.productIntakeInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        expiryDateButton = findViewById(R.id.expiryDateButton);
        brandSpinner = findViewById(R.id.brandSpinner);
        saveProductButton = findViewById(R.id.saveProductButton);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_CATEGORY_NAME)) {
            categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        } else {
            categoryName = "";
        }

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
            double price = 0;
            try { price = Double.parseDouble(priceText); } catch (Exception ignored) {}

            // create Product (adapt Product constructor)
            Product p = new Product();
            p.setName(name);
            p.setBrand(brandSpinner.getSelectedItem().toString());
            p.setPrescriptionType("N/A");
            p.setPrice(price);
            p.setCategory(categoryName);
            // image: you may store URI string or a res id. Here we store Uri string.
            if (imageUri != null) p.setImageUrl(imageUri.toString());

            // add to DataStorage appropriate list
            switch (categoryName) {
                case "Prescription Medicines":
                    DataStorage.getPrescriptionList().add(p);
                    break;
                case "Non-Prescription Medicines":
                    DataStorage.getNonPrescriptionList().add(p);
                    break;
                case "Non-Intake Products":
                    DataStorage.getNonIntakeList().add(p);
                    break;
                case "Device or Monitoring Products":
                    DataStorage.getDeviceList().add(p);
                    break;
                default:
                    // fallback: add to a general products list
                    DataStorage.getAllProducts().add(p);
                    break;
            }

            Toast.makeText(AddProductActivity.this, "Product saved", Toast.LENGTH_SHORT).show();

            // go back to category details
            Intent out = new Intent(AddProductActivity.this, CategoryDetailsActivity.class);
            out.putExtra(CategoryDetailsActivity.EXTRA_CATEGORY_NAME, categoryName);
            startActivity(out);
            finish();
        });
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
