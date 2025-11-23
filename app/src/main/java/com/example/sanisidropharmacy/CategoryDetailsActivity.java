package com.example.sanisidropharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PharmacyProducts";
    private static final String KEY_PRODUCTS = "ProductsList";

    private RecyclerView recyclerView;
    private ImageView navHome, navCart, navUser, btnMenu, fabAdd;
    private EditText searchBar;
    private TextView tvCategoryTitle;

    private String categoryName;
    private MedicineAdapter adapter;
    private List<MedicineModel> medicineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        // ----------- UI BINDINGS ----------
        btnMenu = findViewById(R.id.btnMenu);
        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recyclerView);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        fabAdd = findViewById(R.id.fabAddProduct);

        navHome = findViewById(R.id.nav_home);
        navCart = findViewById(R.id.nav_cart);
        navUser = findViewById(R.id.nav_user);

        // ----------- GET CATEGORY (FIXED KEY) ----------
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName == null) categoryName = "Unknown Category";

        tvCategoryTitle.setText(categoryName);

        // ----------- LOAD PRODUCTS ----------
        loadProducts();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MedicineAdapter(medicineList, this, item -> {
            Intent i = new Intent(this, MedicineDetailActivity.class);
            i.putExtra("name", item.getName());
            i.putExtra("price", item.getPrice());
            i.putExtra("description", item.getDescription());
            i.putExtra("dosage", item.getDosage());
            i.putExtra("category", item.getCategory());
            i.putExtra("stock", item.getStock());
            i.putExtra("prescription", item.isPrescription());
            i.putExtra("image", item.getImage());
            startActivity(i);
        });

        recyclerView.setAdapter(adapter);

        // ----------- FAB ADD ----------
        fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, AddProductActivity.class);
            i.putExtra("CATEGORY_NAME", categoryName); // send correct category
            startActivity(i);
        });

        // ----------- BOTTOM NAV ----------
        navHome.setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(this, UserProfileActivity.class)));
    }

    // ----------- RELOAD WHEN RETURNING ----------
    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
        adapter.notifyDataSetChanged();
    }

    // ----------- LOAD PRODUCTS FROM STORAGE ----------
    private void loadProducts() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_PRODUCTS, "[]");

        medicineList.clear();

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                if (!obj.getString("category").equals(categoryName)) continue;

                medicineList.add(new MedicineModel(
                        obj.getString("name"),
                        obj.getString("description"),
                        obj.getString("price"),
                        obj.getString("category"),
                        obj.getString("imageUri"),
                        obj.getString("dosage"),
                        obj.getInt("stock"),
                        obj.getBoolean("prescription")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
