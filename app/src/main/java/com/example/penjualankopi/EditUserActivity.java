package com.example.penjualankopi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.penjualankopi.db.DatabaseHelper;

public class EditUserActivity extends AppCompatActivity {

    // 1. Deklarasikan EditText untuk password
    private EditText etEmail, etPassword, etRole;
    private Button btnUpdate;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // 2. Hubungkan EditText password dengan layout
        etEmail = findViewById(R.id.etEditUserEmail);
        etPassword = findViewById(R.id.etEditUserPassword); // <-- BARIS INI DITAMBAHKAN
        etRole = findViewById(R.id.etEditUserRole);
        btnUpdate = findViewById(R.id.btnUpdateUser);
        db = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("USER_ID");
            String email = extras.getString("USER_EMAIL");
            String role = extras.getString("USER_ROLE");

            etEmail.setText(email);
            etRole.setText(role);
            // Kita tidak menampilkan password lama untuk alasan keamanan.
            // Admin akan mengetik password baru jika ingin mengubahnya.
        }

        btnUpdate.setOnClickListener(v -> {
            // 3. Ambil teks dari EditText password
            String newEmail = etEmail.getText().toString().trim();
            String newPassword = etPassword.getText().toString().trim(); // <-- BARIS INI DITAMBAHKAN
            String newRole = etRole.getText().toString().trim();

            // Password sekarang wajib diisi saat mengedit
            if (newEmail.isEmpty() || newPassword.isEmpty() || newRole.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Panggil method updateUser dengan menyertakan password
            boolean isUpdated = db.updateUser(userId, newEmail, newPassword, newRole);

            if (isUpdated) {
                Toast.makeText(this, "Data pengguna berhasil diperbarui", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}