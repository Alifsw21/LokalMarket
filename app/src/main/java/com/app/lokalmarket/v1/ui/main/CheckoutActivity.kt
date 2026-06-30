package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.ActivityCheckoutBinding
import com.app.lokalmarket.v1.data.local.KeranjangDbHelper
import com.app.lokalmarket.v1.data.model.Keranjang
import com.app.lokalmarket.v1.ui.adapter.KeranjangListAdapter
import com.app.lokalmarket.v1.utils.BroadcastAction
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var dbHelper: KeranjangDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = KeranjangDbHelper(this)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val buyerName = sharedPref.getString("SAVED_NAME", "Pembeli")
        binding.tvNamaPembeli.text = buyerName

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val isFromCart = intent.getBooleanExtra("EXTRA_FROM_CART", false)

        val items = if (isFromCart) {
            dbHelper.getAllKeranjang()
        } else {
            val productId = intent.getIntExtra("EXTRA_ID", 0)
            val productName = intent.getStringExtra("EXTRA_NAME") ?: "Produk"
            val productPrice = intent.getIntExtra("EXTRA_PRICE", 0)
            listOf(
                Keranjang(
                    idProduk = productId,
                    namaProduk = productName,
                    hargaSatuan = productPrice,
                    jumlahBarang = 1
                )
            )
        }

        val totalHarga = items.sumOf { it.hargaSatuan * it.jumlahBarang }
        binding.tvFinalPrice.text = rupiah.format(totalHarga)

        binding.rvCheckoutItems.layoutManager = LinearLayoutManager(this)
        binding.rvCheckoutItems.adapter = KeranjangListAdapter(
            items = items.toMutableList(),
            showDeleteButton = false
        ) {
            // onItemClick (optional)
        }

        binding.btnPay.setOnClickListener {
            kirimBroadcastPesanan(buyerName ?: "Pembeli")
            
            if (isFromCart) {
                Toast.makeText(this, "Pesanan untuk $buyerName berhasil dibuat!", Toast.LENGTH_LONG).show()
                dbHelper.clearKeranjang()
            } else {
                val productName = intent.getStringExtra("EXTRA_NAME") ?: "Produk"
                Toast.makeText(this, "Pesanan $productName untuk $buyerName Berhasil!", Toast.LENGTH_LONG).show()
            }
            finish()
        }

        binding.toolbarCheckout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun kirimBroadcastPesanan(namaPembeli: String) {
        val intentBroadcast = Intent(BroadcastAction.ACTION_TAMBAH_PESANAN)
        intentBroadcast.putExtra("EXTRA_PEMBELI", namaPembeli)
        sendBroadcast(intentBroadcast)
    }
}