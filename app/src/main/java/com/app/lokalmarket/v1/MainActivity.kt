package com.app.lokalmarket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.v1.adapter.ProductAdapter
import com.app.lokalmarket.v1.data.ProductRepository
import com.app.lokalmarket.v1.ui.main.CartActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvProducts = findViewById<RecyclerView>(R.id.rv_products)

        val list = ProductRepository.getProducts()

        rvProducts.layoutManager = GridLayoutManager(this, 2)

        val adapter = ProductAdapter(list)
        rvProducts.adapter = adapter

        rvProducts.isNestedScrollingEnabled = false

        // Tombol keranjang di toolbar
        val icCart = findViewById<android.widget.ImageView>(R.id.ic_cart)
        icCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}