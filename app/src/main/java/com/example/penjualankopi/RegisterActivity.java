package com.example.penjualankopi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.penjualankopi.db.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Database Helper
        db = new DatabaseHelper(this);

        // Hubungkan variabel dengan komponen di layout
        etEmail = findViewById(R.id.etEmailRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Atur listener untuk tombol Register
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input tidak boleh kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil metode addUser dari DatabaseHelper
            boolean isInserted = db.addUser(email, password);

            if (isInserted) {
                // Jika berhasil, tampilkan pesan dan tutup activity
                Toast.makeText(RegisterActivity.this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke halaman login setelah berhasil
            } else {
                // Jika gagal (kemungkinan email sudah ada)
                Toast.makeText(RegisterActivity.this, "Registrasi Gagal, Email mungkin sudah terdaftar", Toast.LENGTH_SHORT).show();
            }
        });

        // Atur listener untuk teks kembali ke Login
        tvGoToLogin.setOnClickListener(v -> {
            // Tutup activity ini untuk kembali ke activity sebelumnya (Login)
            finish();
        });
    }
}