package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R
import com.app.lokalmarket.v1.adapter.CartAdapter
import com.app.lokalmarket.v1.data.CartRepository
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // Ambil nama pembeli dari SharedPreferences
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val buyerName = sharedPref.getString("SAVED_NAME", "Pembeli")

        val tvBuyerName = findViewById<TextView>(R.id.tv_nama_pembeli)
        tvBuyerName.text = buyerName

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val tvFinalPrice = findViewById<TextView>(R.id.tv_final_price)
        val rvCheckoutItems = findViewById<RecyclerView>(R.id.rv_checkout_items)
        val btnPay = findViewById<MaterialButton>(R.id.btn_pay)

        // Cek apakah datang dari keranjang atau langsung dari detail
        val isFromCart = intent.getBooleanExtra("EXTRA_FROM_CART", false)

        if (isFromCart) {
            // Data dari keranjang
            val items = CartRepository.getItems()
            rvCheckoutItems.layoutManager = LinearLayoutManager(this)
            rvCheckoutItems.adapter = CartAdapter(items)

            tvFinalPrice.text = rupiah.format(CartRepository.getTotalPrice())

            btnPay.setOnClickListener {
                Toast.makeText(
                    this,
                    "Pesanan untuk $buyerName berhasil dibuat!",
                    Toast.LENGTH_LONG
                ).show()
                CartRepository.clearCart()
                finish()
            }
        } else {
            // Data dari detail produk langsung (alur lama tetap dipertahankan)
            val productName = intent.getStringExtra("EXTRA_NAME")
            val productPrice = intent.getIntExtra("EXTRA_PRICE", 0)

            tvFinalPrice.text = rupiah.format(productPrice)

            btnPay.setOnClickListener {
                Toast.makeText(
                    this,
                    "Pesanan $productName untuk $buyerName Berhasil!",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }

        // Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_checkout)
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.navigationIcon?.setTint(android.graphics.Color.parseColor("#333333"))
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}