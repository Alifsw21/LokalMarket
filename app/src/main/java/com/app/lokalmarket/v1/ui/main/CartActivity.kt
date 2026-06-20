package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_cart)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.navigationIcon?.setTint(android.graphics.Color.parseColor("#333333"))
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val rvCartItems = findViewById<RecyclerView>(R.id.rv_cart_items)
        val tvTotal = findViewById<TextView>(R.id.tv_total_price)
        val tvEmpty = findViewById<TextView>(R.id.tv_cart_empty)
        val btnCheckout = findViewById<MaterialButton>(R.id.btn_checkout_from_cart)

        val items = CartRepository.getItems()
        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvCartItems.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE

            rvCartItems.layoutManager = LinearLayoutManager(this)
            rvCartItems.adapter = CartAdapter(items)
        }

        tvTotal.text = rupiah.format(CartRepository.getTotalPrice())

        btnCheckout.setOnClickListener {
            if (CartRepository.getItems().isEmpty()) {
                Toast.makeText(this, "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val buyerName = sharedPref.getString("SAVED_NAME", "Pembeli")

            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("EXTRA_FROM_CART", true)
            intent.putExtra("EXTRA_TOTAL_PRICE", CartRepository.getTotalPrice())
            intent.putExtra("EXTRA_NAME", buyerName)
            startActivity(intent)
        }
    }
}