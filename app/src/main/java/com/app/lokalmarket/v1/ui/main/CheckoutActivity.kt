package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.content.Intent
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
import java.text.SimpleDateFormat
import java.util.Date
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
        val buyerName = sharedPref.getString("SAVED_NAME", "Pembeli") ?: "Pembeli"
        val userId = sharedPref.getInt("USER_ID", -1) // Memunculkan kembali userId yang terhapus
        binding.tvNamaPembeli.text = buyerName

        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val isFromCart = intent.getBooleanExtra("EXTRA_FROM_CART", false)

        // Memperbaiki struktur if-else yang berantakan
        if (isFromCart) {
            val items = dbHelper.getAllKeranjang()
            val totalHarga = items.sumOf { it.hargaSatuan * it.jumlahBarang }
            binding.tvFinalPrice.text = rupiah.format(totalHarga)

            binding.rvCheckoutItems.layoutManager = LinearLayoutManager(this)

            // Mengirim showDeleteButton = false agar tidak bisa menghapus item di halaman checkout
            binding.rvCheckoutItems.adapter = KeranjangListAdapter(
                items.toMutableList(),          // Parameter 1: list item
                showDeleteButton = false,       // Parameter 2: sembunyikan tombol hapus
                onDeleteClick = { _ -> },       // Parameter 3: aksi klik hapus (kosongkan)
                onItemClick = { _ -> }          // Parameter 4: aksi klik item (kosongkan)
            )

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

                // Mengambil data item pertama untuk diteruskan ke Detail Pesanan
                val firstItem = items.firstOrNull()
                val pId = firstItem?.idProduk ?: 1
                val pName = firstItem?.namaProduk ?: "Produk Lokal"
                val pPrice = firstItem?.hargaSatuan ?: 0
                val pQty = firstItem?.jumlahBarang ?: 1

                // Menjalankan fitur broadcast dari branch database
                kirimBroadcastPesanan(buyerName)

                // Menjalankan pemanggilan API pesanan
                buatPesanan(request, true, pId, pName, pPrice, pQty)
            }
        } else {
            val productPrice = intent.getIntExtra("EXTRA_PRICE", 0)
            val productId = intent.getIntExtra("EXTRA_ID", 0)
            val productName = intent.getStringExtra("EXTRA_NAME") ?: "Produk Lokal"

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

                // Menjalankan fitur broadcast dari branch database
                kirimBroadcastPesanan(buyerName)

                // Menjalankan pemanggilan API pesanan
                buatPesanan(request, false, productId, productName, productPrice, 1)
            }
        }

        binding.toolbarCheckout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun kirimBroadcastPesanan(namaPembeli: String) {
        try {
            // Memastikan action string aman jika class BroadcastAction terhapus saat merge
            val intentBroadcast = Intent("com.app.lokalmarket.ACTION_TAMBAH_PESANAN")
            intentBroadcast.putExtra("EXTRA_PEMBELI", namaPembeli)
            sendBroadcast(intentBroadcast)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } // Menambahkan kurung tutup yang hilang saat merge

    private fun buatPesanan(
        request: CheckoutRequest,
        fromCart: Boolean,
        pId: Int,
        pName: String,
        pPrice: Int,
        pQty: Int
    ) {
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

                        // Menjalankan intent untuk pindah ke halaman Detail Pesanan (fitur branch history)
                        val intent = Intent(this@CheckoutActivity, DetailPesananActivity::class.java)

                        val dummyOrderId = (100..999).random()
                        val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                        val currentDate = sdf.format(Date())

                        intent.putExtra("EXTRA_ID_PESANAN", dummyOrderId)
                        intent.putExtra("EXTRA_TANGGAL", currentDate)
                        intent.putExtra("EXTRA_TOTAL", request.totalHarga)
                        intent.putExtra("EXTRA_ALAMAT", request.alamatPengiriman)

                        intent.putExtra("EXTRA_ID_PRODUK", pId)
                        intent.putExtra("EXTRA_NAMA_PRODUK", pName)
                        intent.putExtra("EXTRA_HARGA_PRODUK", pPrice)
                        intent.putExtra("EXTRA_QTY", pQty)

                        startActivity(intent)
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