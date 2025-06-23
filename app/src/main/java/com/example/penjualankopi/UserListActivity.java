package com.example.penjualankopi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <-- IMPORT INI
import com.example.penjualankopi.adapter.UserAdapter;
import com.example.penjualankopi.db.DatabaseHelper;
import com.example.penjualankopi.model.User;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView listView;
    private UserAdapter adapter;
    private List<User> userList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

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
        listView = findViewById(R.id.listViewUsers);

        loadUsers();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = userList.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Pilih Aksi")
                    .setMessage("Email: " + selectedUser.getEmail())
                    .setPositiveButton("Edit", (dialog, which) -> {
                        // Membuat Intent untuk pindah ke EditUserActivity
                        Intent intent = new Intent(UserListActivity.this, EditUserActivity.class);
                        // Mengirim data user yang dipilih ke activity selanjutnya
                        intent.putExtra("USER_ID", selectedUser.getId());
                        intent.putExtra("USER_EMAIL", selectedUser.getEmail());
                        intent.putExtra("USER_ROLE", selectedUser.getRole());
                        // Menggunakan startActivityForResult agar list bisa di-refresh
                        startActivityForResult(intent, 1);
                    })
                    .setNegativeButton("Hapus", (dialog, which) -> {
                        // Mencegah admin menghapus dirinya sendiri
                        if (selectedUser.getEmail().equals("admin@gmail.com")) {
                            Toast.makeText(this, "Tidak dapat menghapus akun admin utama.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.deleteUser(selectedUser.getId());
                        Toast.makeText(this, "User dihapus", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    })
                    .setNeutralButton("Batal", null)
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Cek apakah kembali dari EditUserActivity dengan hasil OK
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Jika ya, muat ulang daftar pengguna untuk menampilkan perubahan
            loadUsers();
        }
    }


    private void loadUsers() {
        userList = db.getAllUsers();
        adapter = new UserAdapter(this, userList);
        listView.setAdapter(adapter);
    }

    // --- MULAI KODE BARU ---
    // Method ini akan menangani klik pada tombol kembali di Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Kembali ke activity sebelumnya
        return true;
    }
    // --- SELESAI KODE BARU ---
}