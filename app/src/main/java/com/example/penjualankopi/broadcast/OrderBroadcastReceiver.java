package com.example.penjualankopi.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OrderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.example.penjualankopi.ORDER_PLACED".equals(intent.getAction())) {
            String message = intent.getStringExtra("message");
            // Untuk demo, kita tampilkan Toast. Di aplikasi nyata, ini bisa jadi notifikasi.
            Toast.makeText(context, "Notifikasi Sistem: " + message, Toast.LENGTH_LONG).show();
        }
    }
}