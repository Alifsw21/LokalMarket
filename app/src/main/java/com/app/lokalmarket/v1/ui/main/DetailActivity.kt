package com.app.lokalmarket.v1.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val name = intent.getStringExtra("EXTRA_NAME")
        val price = intent.getDoubleExtra("EXTRA_PRICE", 0.0)

        val rupiah = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID"))

        findViewById<TextView>(R.id.tv_name_detail).text = name
        findViewById<TextView>(R.id.tv_price_detail).text = rupiah.format(price)

        val btnCheckout = findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_checkout)

        btnCheckout.setOnClickListener {
            val intent = android.content.Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }
}