package com.example.penjualankopi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- IMPORT INI
import com.example.penjualankopi.db.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSimpan;
    private DatabaseHelper db;
    private int userId;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // --- MULAI KODE BARU ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Aktifkan tombol kembali (panah)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // --- SELESAI KODE BARU ---

        db = new DatabaseHelper(this);
        // ID EditText tidak berubah meskipun dibungkus TextInputLayout
        etEmail = findViewById(R.id.etEditEmail);
        etPassword = findViewById(R.id.etEditPassword);
        btnSimpan = findViewById(R.id.btnSimpanProfil);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getInt("user_id", -1);
        userEmail = prefs.getString("user_email", "");

        etEmail.setText(userEmail);

        btnSimpan.setOnClickListener(v -> {
            String emailBaru = etEmail.getText().toString().trim();
            String passwordBaru = etPassword.getText().toString().trim();

            if (emailBaru.isEmpty() || passwordBaru.isEmpty()) {
                Toast.makeText(this, "Email dan Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userId != -1) {
                boolean sukses = db.updateUserProfile(userId, emailBaru, passwordBaru);
                if (sukses) {
                    prefs.edit().putString("user_email", emailBaru).apply();
                    Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // --- MULAI KODE BARU ---
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    // --- SELESAI KODE BARU ---
}