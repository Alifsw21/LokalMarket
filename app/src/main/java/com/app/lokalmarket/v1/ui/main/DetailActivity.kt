package com.app.lokalmarket.v1.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_detail)

        // Pasang Icon Back (Gunakan ic_back yang sudah kamu buat)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.navigationIcon?.setTint(android.graphics.Color.parseColor("#333333"))

        toolbar.setNavigationOnClickListener {
            finish() // Kembali ke MainActivity
        }
        // 1. Ambil data dari Intent
        val name = intent.getStringExtra("EXTRA_NAME")

        // CATATAN: Pastikan di ProductItem.kt tipe 'price' itu Double atau Int.
        // Jika Int, ganti getDoubleExtra menjadi getIntExtra
        val price = intent.getIntExtra("EXTRA_PRICE", 0)

        val rupiah = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))
        findViewById<TextView>(R.id.tv_price_detail).text = rupiah.format(price)

        // 2. Tampilkan data ke UI
        findViewById<TextView>(R.id.tv_name_detail).text = name
        findViewById<TextView>(R.id.tv_price_detail).text = rupiah.format(price)

        val btnCheckout = findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_checkout)

        btnCheckout.setOnClickListener {
            val intent = android.content.Intent(this, CheckoutActivity::class.java)

            intent.putExtra("EXTRA_NAME", name)  // 'name' adalah variabel yang kamu ambil di atas
            intent.putExtra("EXTRA_PRICE", price) // 'price' adalah variabel yang kamu ambil di atas


            startActivity(intent)
        }
    }
}