package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.ActivityCheckoutBinding
import com.app.lokalmarket.v1.data.local.KeranjangDbHelper
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.CheckoutRequest
import com.app.lokalmarket.v1.data.model.DetailPesanan
import com.app.lokalmarket.v1.data.remote.ApiClient
import com.app.lokalmarket.v1.ui.adapter.KeranjangListAdapter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        val buyerName = sharedPref.getString("USER_NAME", "Pembeli")
        val userId = sharedPref.getInt("USER_ID", -1)
        binding.tvNamaPembeli.text = buyerName

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val isFromCart = intent.getBooleanExtra("EXTRA_FROM_CART", false)

        if (isFromCart) {
            val items = dbHelper.getAllKeranjang()
            val totalHarga = items.sumOf { it.hargaSatuan * it.jumlahBarang }
            binding.tvFinalPrice.text = rupiah.format(totalHarga)

            binding.rvCheckoutItems.layoutManager = LinearLayoutManager(this)
            binding.rvCheckoutItems.adapter = KeranjangListAdapter(items.toMutableList()) { item ->
                Toast.makeText(this, "Item dihapus", Toast.LENGTH_SHORT).show()
            }

            binding.btnPay.setOnClickListener {
                if (userId == -1) {
                    Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val details = items.map { 
                    DetailPesanan(it.idProduk, it.jumlahBarang, it.hargaSatuan)
                }
                
                val request = CheckoutRequest(
                    idPengguna = userId,
                    totalHarga = totalHarga,
                    alamatPengiriman = "Jl. RS. Fatmawati Raya, Pd. Labu, Jakarta Selatan, 12450",
                    keranjang = details
                )

                buatPesanan(request, true)
            }
        } else {
            val productPrice = intent.getIntExtra("EXTRA_PRICE", 0)
            val productId = intent.getIntExtra("EXTRA_ID", 0)
            binding.tvFinalPrice.text = rupiah.format(productPrice)

            binding.btnPay.setOnClickListener {
                if (userId == -1) {
                    Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val details = listOf(DetailPesanan(productId, 1, productPrice))
                val request = CheckoutRequest(
                    idPengguna = userId,
                    totalHarga = productPrice,
                    alamatPengiriman = "Jl. RS. Fatmawati Raya, Pd. Labu, Jakarta Selatan, 12450",
                    keranjang = details
                )

                buatPesanan(request, false)
            }
        }

        binding.toolbarCheckout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun buatPesanan(request: CheckoutRequest, fromCart: Boolean) {
        binding.btnPay.isEnabled = false
        binding.btnPay.text = "Memproses..."

        ApiClient.apiService.buatPesanan(request).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                binding.btnPay.isEnabled = true
                binding.btnPay.text = "Buat Pesanan"

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        Toast.makeText(this@CheckoutActivity, "Pesanan Berhasil Dibuat!", Toast.LENGTH_LONG).show()
                        if (fromCart) dbHelper.clearKeranjang()
                        finish()
                    } else {
                        Toast.makeText(this@CheckoutActivity, "Gagal: ${body?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = try {
                        val jObjError = JSONObject(response.errorBody()?.string() ?: "{}")
                        jObjError.getString("message")
                    } catch (e: Exception) {
                        "Terjadi kesalahan server (${response.code()})"
                    }
                    Toast.makeText(this@CheckoutActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                binding.btnPay.isEnabled = true
                binding.btnPay.text = "Buat Pesanan"
                Toast.makeText(this@CheckoutActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}