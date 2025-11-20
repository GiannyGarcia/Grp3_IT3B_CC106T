package com.example.sanisidropharmacy;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityCatalogPM extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CatalogAdapter adapter;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_pm);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new CatalogAdapter(this, DataStorage.getPrescriptionList());
        recyclerView.setAdapter(adapter);

        fabAdd = findViewById(R.id.fabAddProduct);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(ActivityCatalogPM.this, AddProductActivity.class);
                intent.putExtra("CATEGORY_NAME", "Prescription Medicines");
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (!DataStorage.getPrescriptionList().isEmpty()) {
            recyclerView.post(() ->
                    recyclerView.scrollToPosition(DataStorage.getPrescriptionList().size() - 1)
            );
        }
    }
}
