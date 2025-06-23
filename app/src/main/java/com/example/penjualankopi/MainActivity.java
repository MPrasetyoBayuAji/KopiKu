package com.example.penjualankopi;

import android.content.Intent; // Pastikan ini ada
import android.os.Bundle;
import android.widget.Button; // <-- TAMBAHKAN BARIS INI
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.penjualankopi.adapter.KopiAdapter;
import com.example.penjualankopi.db.DatabaseHelper;
import com.example.penjualankopi.fragment.DashboardInfoFragment;
import com.example.penjualankopi.model.Kopi;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvKopi;
    private KopiAdapter kopiAdapter;
    private List<Kopi> kopiList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DashboardInfoFragment())
                    .commit();
        }

        db = new DatabaseHelper(this);
        rvKopi = findViewById(R.id.rvKopi);
        rvKopi.setLayoutManager(new LinearLayoutManager(this));
        Button btnEditProfile = findViewById(R.id.btnToEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
        });


        loadKopiData();
    }

    private void loadKopiData() {
        kopiList = db.getAllKopi();  // Langsung ambil List<Kopi> dari method baru
        kopiAdapter = new KopiAdapter(this, kopiList, false);

        rvKopi.setAdapter(kopiAdapter);
    }

}