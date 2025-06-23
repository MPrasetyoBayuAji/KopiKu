package com.example.penjualankopi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- IMPORT INI
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.penjualankopi.adapter.KopiAdapter;
import com.example.penjualankopi.db.DatabaseHelper;
import com.example.penjualankopi.model.Kopi;
import java.util.List;

public class KopiListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KopiAdapter adapter;
    private DatabaseHelper db;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kopi_list);

        // --- MULAI KODE BARU ---
        // (Asumsi Anda sudah menambahkan Toolbar di activity_kopi_list.xml)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Kelola Produk Kopi"); // Memberi judul
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // --- SELESAI KODE BARU ---

        recyclerView = findViewById(R.id.recyclerViewKopi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);
        List<Kopi> kopiList = db.getAllKopi();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String role = prefs.getString("role", "user");
        isAdmin = role.equals("admin");

        adapter = new KopiAdapter(this, kopiList, isAdmin);
        recyclerView.setAdapter(adapter);
    }

    // --- MULAI KODE BARU ---
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // --- SELESAI KODE BARU ---
}