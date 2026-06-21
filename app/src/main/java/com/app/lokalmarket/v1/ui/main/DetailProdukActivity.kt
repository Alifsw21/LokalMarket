package com.app.lokalmarket.v1.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.databinding.ActivityDetailBinding
import com.app.lokalmarket.v1.data.local.KeranjangDbHelper
import com.app.lokalmarket.v1.data.model.Keranjang
import com.app.lokalmarket.v1.ui.main.CheckoutActivity
import java.text.NumberFormat
import java.util.Locale

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: KeranjangDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = KeranjangDbHelper(this)

        val productId = intent.getIntExtra("EXTRA_ID", 0)
        val name = intent.getStringExtra("EXTRA_NAME") ?: "Produk"
        val price = intent.getIntExtra("EXTRA_PRICE", 0)

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        binding.tvNameDetail.text = name
        binding.tvPriceDetail.text = rupiah.format(price)

        binding.toolbarDetail.setNavigationOnClickListener {
            finish()
        }

        binding.btnAddToCart.setOnClickListener {
            val itemKeranjang = Keranjang(
                id = 0,
                idProduk = productId,
                namaProduk = name,
                hargaSatuan = price,
                jumlahBarang = 1
            )

            val isSuccess = dbHelper.insertKeranjang(itemKeranjang)

            if (isSuccess) {
                Toast.makeText(this, "$name berhasil masuk keranjang", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("EXTRA_ID", productId)
            intent.putExtra("EXTRA_NAME", name)
            intent.putExtra("EXTRA_PRICE", price)
            startActivity(intent)
        }
    }
}