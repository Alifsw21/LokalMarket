package com.app.lokalmarket.v1.ui.main

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

        val productName = intent.getStringExtra("EXTRA_NAME")
        val productPrice = intent.getDoubleExtra("EXTRA_PRICE", 0.0)

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val tvFinalPrice = findViewById<TextView>(R.id.tv_final_price)
        tvFinalPrice.text = rupiah.format(productPrice)

        val btnPay = findViewById<MaterialButton>(R.id.btn_pay)
        btnPay.setOnClickListener {
            Toast.makeText(this, "Pesanan $productName Berhasil Dibuat!", Toast.LENGTH_LONG).show()

            finish()
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_checkout)
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert) // Icon back
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}