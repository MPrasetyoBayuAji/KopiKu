package com.example.penjualankopi.model;

public class Kopi {
    private int id;
    private String nama;
    private int harga;
    private int stok;
    private String gambar;

    public Kopi(int id, String nama, int harga, int stok, String gambar) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.gambar = gambar;
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }


    // Buat semua Getter dan Setter
    public int getId() { return id; }
    public String getNama() { return nama; }
    public int getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getGambar() { return gambar; }



}