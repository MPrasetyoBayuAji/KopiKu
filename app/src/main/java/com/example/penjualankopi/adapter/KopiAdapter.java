package com.example.penjualankopi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjualankopi.R;
import com.example.penjualankopi.db.DatabaseHelper;
import com.example.penjualankopi.model.Kopi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KopiAdapter extends RecyclerView.Adapter<KopiAdapter.KopiViewHolder> {

    private Context context;
    private List<Kopi> kopiList;
    private DatabaseHelper db;
    private boolean isAdmin; // ⬅️ Tambahkan untuk deteksi role

    public KopiAdapter(Context context, List<Kopi> kopiList, boolean isAdmin) {
        this.context = context;
        this.kopiList = kopiList;
        this.isAdmin = isAdmin;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public KopiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kopi, parent, false);
        return new KopiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KopiViewHolder holder, int position) {
        Kopi kopi = kopiList.get(position);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.tvNama.setText(kopi.getNama());
        holder.tvHarga.setText(formatRupiah.format(kopi.getHarga()));
        holder.tvStok.setText("Stok: " + kopi.getStok());

        int imageResId = context.getResources().getIdentifier(kopi.getGambar(), "drawable", context.getPackageName());
        holder.ivGambar.setImageResource(imageResId);

        // ⬇️ Ganti teks tombol tergantung role
        holder.btnAksi.setText(isAdmin ? "Edit" : "Pesan");

        holder.btnAksi.setOnClickListener(v -> {
            if (isAdmin) {
                showEditDialog(kopi);
            } else {
                if (kopi.getStok() > 0) {
                    showOrderDialog(kopi);
                } else {
                    Toast.makeText(context, "Stok " + kopi.getNama() + " habis!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kopiList.size();
    }

    private void showOrderDialog(Kopi kopi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pesan " + kopi.getNama());
        builder.setMessage("Stok tersedia: " + kopi.getStok());

        // Inflate layout custom
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_pickup, null);
        final EditText etNamaPemesan = view.findViewById(R.id.etNamaPemesan);
        final EditText etJadwalPickup = view.findViewById(R.id.etJadwalPickup);
        final EditText etJumlah = view.findViewById(R.id.etJumlahPesanan);
        builder.setView(view);

        builder.setPositiveButton("Pesan", (dialog, which) -> {
            String namaPemesan = etNamaPemesan.getText().toString().trim();
            String jadwal = etJadwalPickup.getText().toString().trim();
            String jumlahStr = etJumlah.getText().toString().trim();

            if (namaPemesan.isEmpty() || jadwal.isEmpty() || jumlahStr.isEmpty()) {
                Toast.makeText(context, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int jumlah = Integer.parseInt(jumlahStr);
            if (jumlah > 0 && jumlah <= kopi.getStok()) {
                boolean sukses = db.updateStok(kopi.getId(), jumlah);
                if (sukses) {
                    // --- MULAI KODE BARU ---
                    try {
                        // Memutar suara notifikasi dari folder res/raw
                        android.media.MediaPlayer mediaPlayer = android.media.MediaPlayer.create(context, R.raw.notification);
                        mediaPlayer.start();
                        // Melepaskan resource setelah suara selesai diputar (praktik terbaik)
                        mediaPlayer.setOnCompletionListener(mp -> mp.release());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // --- SELESAI KODE BARU ---

                    kopi.setStok(kopi.getStok() - jumlah);
                    notifyItemChanged(kopiList.indexOf(kopi));

                    // Tampilkan Nota
                    showNotaDialog(kopi, namaPemesan, jadwal, jumlah);

                    // Broadcast (opsional)
                    Intent intent = new Intent("com.example.penjualankopi.ORDER_PLACED");
                    intent.putExtra("message", "Pesanan baru dari " + namaPemesan);
                    context.sendBroadcast(intent);
                } else {
                    Toast.makeText(context, "Gagal memesan, stok mungkin sudah berubah", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Jumlah pesanan tidak valid atau melebihi stok", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showNotaDialog(Kopi kopi, String namaPemesan, String jadwal, int jumlah) {
        long totalPembayaran = (long) kopi.getHarga() * jumlah;
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        String notaMessage = "Nama Pemesan: " + namaPemesan + "\n" +
                "Jadwal Pengambilan: " + jadwal + "\n\n" +
                "Pesanan:\n" +
                "- " + kopi.getNama() + " (x" + jumlah + ")\n\n" +
                "Total Pembayaran: " + formatRupiah.format(totalPembayaran);

        new AlertDialog.Builder(context)
                .setTitle("✅ Pesanan Berhasil (Nota Pick Up)")
                .setMessage(notaMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showEditDialog(Kopi kopi) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit " + kopi.getNama());

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_kopi, null);
        EditText etHarga = view.findViewById(R.id.etHargaEdit);
        EditText etStok = view.findViewById(R.id.etStokEdit);

        etHarga.setText(String.valueOf(kopi.getHarga()));
        etStok.setText(String.valueOf(kopi.getStok()));

        builder.setView(view);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            int hargaBaru = Integer.parseInt(etHarga.getText().toString());
            int stokBaru = Integer.parseInt(etStok.getText().toString());

            kopi.setHarga(hargaBaru);
            kopi.setStok(stokBaru);
            boolean sukses = db.updateKopi(kopi);

            if (sukses) {
                notifyItemChanged(kopiList.indexOf(kopi));
                Toast.makeText(context, "Data kopi berhasil diupdate", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Gagal update kopi", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", null);
        builder.show();
    }


    static class KopiViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGambar;
        TextView tvNama, tvHarga, tvStok;
        Button btnAksi;

        public KopiViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGambar = itemView.findViewById(R.id.ivKopiGambar);
            tvNama = itemView.findViewById(R.id.tvKopiNama);
            tvHarga = itemView.findViewById(R.id.tvKopiHarga);
            tvStok = itemView.findViewById(R.id.tvKopiStok);
            btnAksi = itemView.findViewById(R.id.btnPesan);
        }
    }
}
