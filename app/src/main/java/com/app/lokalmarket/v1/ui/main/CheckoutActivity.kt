package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.R
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // --- 1. AMBIL DATA DARI SHAREDPREFERENCES (NAMA LOGIN) ---
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val buyerName = sharedPref.getString("SAVED_NAME", "Pembeli")

        // Tampilkan ke TextView Nama Pembeli (Pastikan ID di XML adalah tv_buyer_name)
        // Jika ID di XML kamu berbeda, sesuaikan R.id-nya di sini
        val tvbuyerName = findViewById<TextView>(R.id.tv_nama_pembeli)
        tvbuyerName.text = buyerName


        // --- 2. AMBIL DATA DARI INTENT (NAMA PRODUK & HARGA) ---
        val productName = intent.getStringExtra("EXTRA_NAME")
        val productPrice = intent.getIntExtra("EXTRA_PRICE", 0)

        // --- 3. FORMAT DAN TAMPILKAN HARGA ---
        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val tvFinalPrice = findViewById<TextView>(R.id.tv_final_price)
        tvFinalPrice.text = rupiah.format(productPrice)


        // --- 4. LOGIKA TOMBOL BAYAR ---
        val btnPay = findViewById<MaterialButton>(R.id.btn_pay)
        btnPay.setOnClickListener {
            // Toast sekarang menampilkan nama produk yang dibeli
            Toast.makeText(this, "Pesanan $productName untuk $buyerName Berhasil!", Toast.LENGTH_LONG).show()
            finish()
        }


        // --- 5. TOOLBAR & NAVIGASI ---
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_checkout)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.navigationIcon?.setTint(android.graphics.Color.parseColor("#333333"))
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}