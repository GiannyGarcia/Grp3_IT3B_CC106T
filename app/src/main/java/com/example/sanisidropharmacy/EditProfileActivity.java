package com.example.sanisidropharmacy;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView editProfileImage;
    private EditText editName, editEmail, editBirthDate;
    private Button btnChangePhoto, btnSaveChanges, btnCancelEdit;

    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageUri;

    private static final String USER_PREFS = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        editProfileImage = findViewById(R.id.editProfileImage);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editBirthDate = findViewById(R.id.editBirthDate);

        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        editName.setText(prefs.getString("session_name", ""));
        editEmail.setText(prefs.getString("session_email", ""));
        editBirthDate.setText(prefs.getString("session_birthdate", ""));

        String imgUri = prefs.getString("session_imageUri", null);

        if (imgUri != null) {
            imageUri = Uri.parse(imgUri);
            editProfileImage.setImageURI(imageUri);
        }
    }

    private void setupListeners() {

        btnChangePhoto.setOnClickListener(v -> openGallery());

        editBirthDate.setOnClickListener(v -> showDatePicker());

        btnSaveChanges.setOnClickListener(v -> saveProfile());

        btnCancelEdit.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                    editBirthDate.setText(date);
                },
                year, month, day
        );

        datePicker.show();
    }

    private void saveProfile() {
        SharedPreferences prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("session_name", editName.getText().toString());
        editor.putString("session_email", editEmail.getText().toString());
        editor.putString("session_birthdate", editBirthDate.getText().toString());

        if (imageUri != null) {
            editor.putString("session_imageUri", imageUri.toString());
        }

        editor.apply();

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                editProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
