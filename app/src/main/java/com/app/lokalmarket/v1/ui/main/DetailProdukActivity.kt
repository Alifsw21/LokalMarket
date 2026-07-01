package com.app.lokalmarket.v1.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.lokalmarket.R
import com.app.lokalmarket.databinding.ActivityDetailBinding
import com.app.lokalmarket.v1.data.local.KeranjangDbHelper
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.Keranjang
import com.app.lokalmarket.v1.data.model.Produk
import com.app.lokalmarket.v1.data.remote.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class DetailProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var dbHelper: KeranjangDbHelper
    private var currentProduk: Produk? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        applyStatusBarPadding()
        dbHelper = KeranjangDbHelper(this)

        val productId = intent.getIntExtra("EXTRA_ID", -1)

        if (productId != -1) {
            loadDetailProduk(productId)
        }

        binding.toolbarDetail.setNavigationOnClickListener { finish() }

        binding.btnAddToCart.setOnClickListener {
            currentProduk?.let { produk ->
                val itemKeranjang = Keranjang(
                    id = 0,
                    idProduk = produk.id,
                    namaProduk = produk.namaProduk,
                    hargaSatuan = produk.harga,
                    jumlahBarang = 1
                )
                if (dbHelper.insertKeranjang(itemKeranjang)) {
                    Toast.makeText(this, "${produk.namaProduk} masuk keranjang", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCheckout.setOnClickListener {
            currentProduk?.let { produk ->
                val intent = Intent(this, CheckoutActivity::class.java).apply {
                    putExtra("EXTRA_ID", produk.id)
                    putExtra("EXTRA_NAME", produk.namaProduk)
                    putExtra("EXTRA_PRICE", produk.harga)
                }
                startActivity(intent)
            }
        }
    }

    private fun loadDetailProduk(id: Int) {
        ApiClient.apiService.getDetailProduk(id).enqueue(object : Callback<ApiResponse<Produk>> {
            override fun onResponse(call: Call<ApiResponse<Produk>>, response: Response<ApiResponse<Produk>>) {
                val produk = response.body()?.data
                if (response.isSuccessful && produk != null) {
                    currentProduk = produk
                    updateUI(produk)
                } else {
                    Toast.makeText(this@DetailProdukActivity, "Gagal memuat detail", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Produk>>, t: Throwable) {
                Toast.makeText(this@DetailProdukActivity, "Error koneksi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(produk: Produk) {
        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        binding.tvNameDetail.text = produk.namaProduk
        binding.tvPriceDetail.text = rupiah.format(produk.harga)
        binding.tvDeskripsiDetail.text = produk.deskripsi

        val fileName = produk.imageFileName

        if (!fileName.isNullOrEmpty()) {
            val imageId = resources.getIdentifier(fileName, "drawable", packageName)

            if (imageId != 0) {
                binding.imgDetail.setImageResource(imageId)
            } else {
                binding.imgDetail.setImageResource(R.drawable.ic_launcher_background)
            }
        } else {
            binding.imgDetail.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    private fun applyStatusBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootDetail) { v, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, statusBarInset.top, v.paddingRight, v.paddingBottom)
            insets
        }
    }
}