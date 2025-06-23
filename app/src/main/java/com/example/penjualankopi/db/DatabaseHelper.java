package com.example.penjualankopi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.penjualankopi.model.Kopi;
import com.example.penjualankopi.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "penjualankopi.db";
    public static final int DATABASE_VERSION = 2;

    // Tabel Pengguna
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "ID";
    public static final String COL_USER_EMAIL = "EMAIL";
    public static final String COL_USER_PASSWORD = "PASSWORD";
    public static final String COL_USER_ROLE = "role";

    // Tabel Kopi
    public static final String TABLE_KOPI = "kopi";
    public static final String COL_KOPI_ID = "ID";
    public static final String COL_KOPI_NAMA = "NAMA";
    public static final String COL_KOPI_HARGA = "HARGA";
    public static final String COL_KOPI_STOK = "STOK";
    public static final String COL_KOPI_GAMBAR = "GAMBAR";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel pengguna
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PASSWORD + " TEXT, " +
                COL_USER_ROLE + " TEXT DEFAULT 'user')";
        db.execSQL(createUserTable);

        // Tambah akun admin default
        ContentValues admin = new ContentValues();
        admin.put(COL_USER_EMAIL, "admin@gmail.com");
        admin.put(COL_USER_PASSWORD, "admin123");
        admin.put(COL_USER_ROLE, "admin");
        db.insert(TABLE_USERS, null, admin);

        // Buat tabel kopi
        String createKopiTable = "CREATE TABLE " + TABLE_KOPI + " (" +
                COL_KOPI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_KOPI_NAMA + " TEXT, " +
                COL_KOPI_HARGA + " INTEGER, " +
                COL_KOPI_STOK + " INTEGER, " +
                COL_KOPI_GAMBAR + " TEXT)";
        db.execSQL(createKopiTable);

        // Tambahkan kopi awal
        addInitialKopi(db);
    }

    private void addInitialKopi(SQLiteDatabase db) {
        tambahKopi(db, "Espresso", 18000, 30, "espresso");
        tambahKopi(db, "Latte", 25000, 25, "latte");
        tambahKopi(db, "Cappuccino", 25000, 28, "cappuccino");
        tambahKopi(db, "Americano", 22000, 35, "americano");
    }

    private void tambahKopi(SQLiteDatabase db, String nama, int harga, int stok, String gambar) {
        ContentValues values = new ContentValues();
        values.put(COL_KOPI_NAMA, nama);
        values.put(COL_KOPI_HARGA, harga);
        values.put(COL_KOPI_STOK, stok);
        values.put(COL_KOPI_GAMBAR, gambar);
        db.insert(TABLE_KOPI, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KOPI);
        onCreate(db);
    }

    // Login user
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Ambil role user
    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ROLE + " FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        return "user";
    }

    // Tambah user baru
    public boolean addUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_EMAIL, email);
        contentValues.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Ambil semua user
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE));
                userList.add(new User(id, email, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    // Hapus user berdasarkan ID
    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    // Update data user
    // Method update data user yang BARU (dengan password)
    public boolean updateUser(int userId, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password); // <-- BARIS INI DITAMBAHKAN
        values.put(COL_USER_ROLE, role);
        int result = db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        return result > 0;
    }

    // Ambil semua data kopi
    public List<Kopi> getAllKopi() {
        List<Kopi> kopiList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_KOPI, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KOPI_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COL_KOPI_NAMA));
                int harga = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KOPI_HARGA));
                int stok = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KOPI_STOK));
                String gambar = cursor.getString(cursor.getColumnIndexOrThrow(COL_KOPI_GAMBAR));

                kopiList.add(new Kopi(id, nama, harga, stok, gambar));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return kopiList;
    }


    // Update stok setelah pembelian
    public boolean updateStok(int id, int jumlahDipesan) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_KOPI_STOK + " FROM " + TABLE_KOPI + " WHERE ID = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            int stokSekarang = cursor.getInt(0);
            if (stokSekarang >= jumlahDipesan) {
                int stokBaru = stokSekarang - jumlahDipesan;
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_KOPI_STOK, stokBaru);
                db.update(TABLE_KOPI, contentValues, "ID = ?", new String[]{String.valueOf(id)});
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean updateKopi(Kopi kopi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_KOPI_HARGA, kopi.getHarga());
        values.put(COL_KOPI_STOK, kopi.getStok());
        int result = db.update(TABLE_KOPI, values, COL_KOPI_ID + "=?", new String[]{String.valueOf(kopi.getId())});
        return result > 0;
    }

    // Method untuk mengambil ID user berdasarkan email
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ID + " FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + " = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            cursor.close();
            return userId;
        }
        return -1; // Mengembalikan -1 jika tidak ditemukan
    }

    // Method untuk user mengupdate profilnya sendiri
    public boolean updateUserProfile(int id, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASSWORD, password); // Tambahkan update password
        int result = db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }



}
