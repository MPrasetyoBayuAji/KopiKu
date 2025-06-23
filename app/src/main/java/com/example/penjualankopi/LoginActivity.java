package com.example.penjualankopi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.penjualankopi.db.DatabaseHelper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvGoToRegister = findViewById(R.id.tvGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUser(email, password)) {
                String role = db.getUserRole(email);
                int userId = db.getUserId(email); // <- TAMBAHKAN INI

                // simpan role dan ID ke SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("role", role);
                editor.putInt("user_id", userId); // <- TAMBAHKAN INI
                editor.putString("user_email", email); // Simpan juga email untuk ditampilkan
                editor.apply();

                Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                if (role.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show();
            }
        });

        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}