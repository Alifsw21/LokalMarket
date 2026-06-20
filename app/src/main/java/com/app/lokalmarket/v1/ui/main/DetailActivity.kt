package com.app.lokalmarket.v1.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.R
import com.app.lokalmarket.v1.data.CartRepository
import com.app.lokalmarket.v1.model.ProductItem
import com.google.android.material.button.MaterialButton

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Inisialisasi Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_detail)

        // Pasang Icon Back
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.navigationIcon?.setTint(android.graphics.Color.parseColor("#333333"))

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Ambil data dari Intent
        val name = intent.getStringExtra("EXTRA_NAME")
        val price = intent.getIntExtra("EXTRA_PRICE", 0)

        val rupiah = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))

        // Tampilkan data ke UI
        findViewById<TextView>(R.id.tv_name_detail).text = name
        findViewById<TextView>(R.id.tv_price_detail).text = rupiah.format(price)

        // Tombol Masukkan ke Keranjang
        val btnAddToCart = findViewById<MaterialButton>(R.id.btn_add_to_cart)
        btnAddToCart.setOnClickListener {
            val product = ProductItem(
                id = 0,
                name = name ?: "",
                price = price,
                discount = ""
            )
            CartRepository.addItem(product)
            Toast.makeText(this, "$name berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
        }

        // Tombol Checkout langsung (tetap dipertahankan)
        val btnCheckout = findViewById<MaterialButton>(R.id.btn_checkout)
        btnCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("EXTRA_NAME", name)
            intent.putExtra("EXTRA_PRICE", price)
            startActivity(intent)
        }
    }
}