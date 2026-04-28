package com.app.lokalmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.lokalmarket.databinding.ActivityMainBinding
import com.app.lokalmarket.v1.data.ProductRepository
import com.app.lokalmarket.v1.adapter.ProductAdapter
import androidx.recyclerview.widget.RecyclerView
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
    }
}