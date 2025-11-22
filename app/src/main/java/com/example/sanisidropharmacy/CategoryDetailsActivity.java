package com.example.sanisidropharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PharmacyProducts";
    private static final String KEY_PRODUCTS = "ProductsList";

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> productList = new ArrayList<>();

    private TextView tvCategoryTitle;
    private EditText searchBar;
    private FloatingActionButton fabAddProduct;

    private ImageView navCart, navHome, navUser;

    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        categoryName = getIntent().getStringExtra("selectedCategory");
        if (categoryName == null) categoryName = "";

        recyclerView = findViewById(R.id.recyclerView);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        searchBar = findViewById(R.id.search_bar);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        navCart = findViewById(R.id.nav_cart);
        navHome = findViewById(R.id.nav_home);
        navUser = findViewById(R.id.nav_user);

        tvCategoryTitle.setText(categoryName);

        loadProducts();

        adapter = new MedicineAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent i = new Intent(CategoryDetailsActivity.this, AddProductActivity.class);
            i.putExtra("CATEGORY_NAME", categoryName);
            startActivity(i);
        });

        navHome.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, MainActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, UserProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // reload newly added products
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        productList.clear();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PRODUCTS, "[]");

        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Medicine med = new Medicine(obj);

                if (med.getCategory().equals(categoryName)) {
                    productList.add(med);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void filterProducts(String query) {
        loadProducts();

        String q = query.toLowerCase();

        List<Medicine> filtered = new ArrayList<>();
        for (Medicine m : productList) {
            if (m.getName().toLowerCase().contains(q)) {
                filtered.add(m);
            }
        }

        productList.clear();
        productList.addAll(filtered);
        adapter.notifyDataSetChanged();
    }
}
