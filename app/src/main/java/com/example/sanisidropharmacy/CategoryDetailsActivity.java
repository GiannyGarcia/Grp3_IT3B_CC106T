package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryDetailsActivity extends AppCompatActivity
        implements MedicineAdapter.OnItemClickListener {

    public static final String EXTRA_CATEGORY_NAME = "category";

    private RecyclerView recyclerView;
    private MedicineAdapter adapter;

    private List<MedicineModel> productList;   // original list (string prices)
    private List<Medicine> convertedList;      // final list for adapter

    private TextView tvCategoryTitle;
    private EditText searchBar;
    private FloatingActionButton fabAddProduct;

    private ImageView navCart, navHome, navUser;

    private String categoryName = "";

    // Activity result launcher for add-product flow
    private ActivityResultLauncher<Intent> addProductLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);

        // Get category name passed from previous screen
        categoryName = getIntent().getStringExtra("category");

        // Initialize Views
        recyclerView = findViewById(R.id.recyclerView);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        searchBar = findViewById(R.id.search_bar);
        fabAddProduct = findViewById(R.id.fabAddProduct);

        navCart = findViewById(R.id.nav_cart);
        navHome = findViewById(R.id.nav_home);
        navUser = findViewById(R.id.nav_user);

        if (categoryName == null) categoryName = "";
        tvCategoryTitle.setText(categoryName);

        // Prepare ActivityResultLauncher before launching AddProductActivity
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        // Recreate MedicineModel from returned extras
                        String name = data.getStringExtra("NEW_PRODUCT_NAME");
                        String desc = data.getStringExtra("NEW_PRODUCT_DESC");
                        String priceStr = data.getStringExtra("NEW_PRODUCT_PRICE");
                        String category = data.getStringExtra("NEW_PRODUCT_CATEGORY");
                        String imageUri = data.getStringExtra("NEW_PRODUCT_IMAGE_URI");

                        if (name != null && priceStr != null) {
                            MedicineModel newModel = new MedicineModel(name, desc == null ? "" : desc, priceStr, category == null ? "" : category);
                            // If your MedicineModel has imageUrl field later, set it there. For now keep with provided model.
                            // Add to source lists and adapter
                            productList.add(newModel);
                            Medicine converted = convertSingle(newModel, imageUri);
                            convertedList.add(converted);
                            adapter.notifyItemInserted(convertedList.size() - 1);
                        }
                    }
                }
        );

        // Load product list (initial data)
        productList = loadProductsByCategory(categoryName);

        // Convert List<MedicineModel> → List<Medicine>
        convertedList = convertToMedicineList(productList);

        // Adapter init (context, list, listener)
        adapter = new MedicineAdapter(this, convertedList, this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable editable) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
        });

        // FAB action: open AddProductActivity with category info
        fabAddProduct.setOnClickListener(v -> {
            Intent i = new Intent(CategoryDetailsActivity.this, AddProductActivity.class);
            i.putExtra(AddProductActivity.EXTRA_CATEGORY_NAME, categoryName);
            addProductLauncher.launch(i);
        });

        // Bottom Navigation
        navHome.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, HomeActivity.class)));

        navCart.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, CartActivity.class)));

        navUser.setOnClickListener(v ->
                startActivity(new Intent(CategoryDetailsActivity.this, UserProfileActivity.class)));
    }


    /**
     * Convert List<MedicineModel> → List<Medicine>
     * Uses your 7-parameter Medicine constructor
     */
    private List<Medicine> convertToMedicineList(List<MedicineModel> models) {
        List<Medicine> list = new ArrayList<>();

        for (MedicineModel m : models) {
            double price = parsePrice(m.getPrice());

            // CORRECT constructor based on your Medicine.java (7-arg)
            list.add(new Medicine(
                    m.getName(),       // name
                    price,             // price
                    0,                 // stock (default)
                    "N/A",             // expiryDate (default)
                    m.getCategory(),   // category
                    "Unknown",         // manufacturer (default)
                    ""                 // imageUrl (default)
            ));
        }

        return list;
    }

    // Convert a single MedicineModel into Medicine; includes optional imageUri
    private Medicine convertSingle(MedicineModel m, String imageUri) {
        double price = parsePrice(m.getPrice());
        String imageUrl = imageUri != null ? imageUri : "";
        return new Medicine(
                m.getName(),
                price,
                0,
                "N/A",
                m.getCategory(),
                "Unknown",
                imageUrl
        );
    }

    /**
     * Search filter
     * Filters original productList, converts back to Medicine objects
     */
    private void filterProducts(String query) {
        String q = query == null ? "" : query.toLowerCase().trim();

        List<Medicine> filtered = new ArrayList<>();

        for (MedicineModel item : productList) {
            if (item.getName() != null && item.getName().toLowerCase().contains(q)) {

                double price = parsePrice(item.getPrice());

                filtered.add(new Medicine(
                        item.getName(),
                        price,
                        0,
                        "N/A",
                        item.getCategory(),
                        "Unknown",
                        ""
                ));
            }
        }

        convertedList.clear();
        convertedList.addAll(filtered);
        adapter.notifyDataSetChanged();
    }


    /**
     * Parse price strings like:
     * ₱5.00 → 5.00
     * P10 → 10
     * 12 → 12
     */
    private double parsePrice(String priceStr) {
        if (priceStr == null) return 0.0;

        String cleaned = priceStr.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) return 0.0;

        try {
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0.0;
        }
    }


    /**
     * Temporary fake data
     */
    private List<MedicineModel> loadProductsByCategory(String category) {
        List<MedicineModel> list = new ArrayList<>();

        list.add(new MedicineModel("Biogesic", "Pain Reliever", "₱5.00", category));
        list.add(new MedicineModel("Neozep", "Cold Medicine", "₱8.00", category));
        list.add(new MedicineModel("Bioflu", "Flu Treatment", "₱10.00", category));
        list.add(new MedicineModel("Alaxan", "Body Pain", "₱12.00", category));

        return list;
    }


    /**
     * Click handler from MedicineAdapter
     */
    @Override
    public void onItemClick(Medicine medicine) {
        // TODO: open medicine details page
    }
}
