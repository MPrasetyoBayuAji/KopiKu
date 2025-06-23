package com.example.penjualankopi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- IMPORT INI
import com.google.android.material.card.MaterialCardView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // --- MULAI KODE BARU ---
        // Setup Toolbar sebagai Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Kita tidak menambahkan tombol kembali di sini karena ini adalah halaman dashboard utama
        // --- SELESAI KODE BARU ---

        // Kode lama Anda yang menggunakan ID dari CardView baru
        MaterialCardView cardKelolaUser = findViewById(R.id.cardKelolaUser);
        MaterialCardView cardKelolaProduk = findViewById(R.id.cardKelolaProduk);

        cardKelolaUser.setOnClickListener(v -> {
            startActivity(new Intent(this, UserListActivity.class));
        });

        cardKelolaProduk.setOnClickListener(v -> {
            startActivity(new Intent(this, KopiListActivity.class));
        });
    }
}