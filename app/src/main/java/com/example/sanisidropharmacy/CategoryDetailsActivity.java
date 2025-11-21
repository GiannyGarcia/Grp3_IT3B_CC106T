package com.example.sanisidropharmacy;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<MedicineModel> productList;
    private List<MedicineModel> filteredList;

    private TextView tvCategoryTitle;
    private EditText searchBar;
    private FloatingActionButton fabAddProduct;

    private ImageView navCart, navHome, navUser;

    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        // Get category name
        categoryName = getIntent().getStringExtra("category");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        searchBar = findViewById(R.id.search_bar);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        navCart = findViewById(R.id.nav_cart);
        navHome = findViewById(R.id.nav_home);
        navUser = findViewById(R.id.nav_user);

        tvCategoryTitle.setText(categoryName);

        // Load product list
        productList = loadProductsByCategory(categoryName);
        filteredList = new ArrayList<>(productList);

        adapter = new MedicineAdapter(filteredList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Search filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });

        // FAB Action
        fabAddProduct.setOnClickListener(v -> {
            Intent i = new Intent(CategoryDetailsActivity.this, AddProductActivity.class);
            startActivity(i);
        });

        // Bottom Navigation Listeners
        navHome.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, HomeActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, ProfileActivity.class)));
    }

    // Search filtering
    private void filterProducts(String query) {
        filteredList.clear();
        for (MedicineModel item : productList) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    // TEMPORARY SAMPLE DATA — replace this with Firebase / SQLite
    private List<MedicineModel> loadProductsByCategory(String category) {
        List<MedicineModel> list = new ArrayList<>();

        list.add(new MedicineModel("Biogesic", "Pain Reliever", "₱5.00", category));
        list.add(new MedicineModel("Neozep", "Cold Medicine", "₱8.00", category));
        list.add(new MedicineModel("Bioflu", "Flu Treatment", "₱10.00", category));
        list.add(new MedicineModel("Alaxan", "Body Pain", "₱12.00", category));

        return list;
    }
}
