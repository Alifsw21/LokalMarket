package com.app.lokalmarket.v1.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.ActivityDetailPesananBinding
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.DetailPesananResponse
import com.app.lokalmarket.v1.data.model.Keranjang
import com.app.lokalmarket.v1.data.remote.ApiClient
import com.app.lokalmarket.v1.ui.adapter.KeranjangListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.Locale

class DetailPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPesananBinding
    private lateinit var adapter: KeranjangListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Tangkap Data Umum Pesanan
        val idPesanan = intent.getIntExtra("EXTRA_ID_PESANAN", -1)
        val tanggal = intent.getStringExtra("EXTRA_TANGGAL") ?: "20 Mei 2024"
        val totalHarga = intent.getIntExtra("EXTRA_TOTAL", 0)
        val alamat = intent.getStringExtra("EXTRA_ALAMAT") ?: "Alamat tidak tersedia"

        if (idPesanan == -1) {
            finish()
            return
        }

        setupUI(idPesanan, tanggal, totalHarga, alamat)
        setupRecyclerView()
        ambilDetailPesanan(idPesanan)
    }

    private fun setupUI(id: Int, tanggal: String, total: Int, alamat: String) {
        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        binding.tvDetailIdPesanan.text = "Pesanan #$id"
        binding.tvDetailTanggal.text = tanggal
        binding.tvDetailAlamat.text = alamat
        binding.tvDetailTotalHarga.text = rupiah.format(total)

        binding.toolbarDetailPesanan.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = KeranjangListAdapter(
            items = mutableListOf(),
            showDeleteButton = false
        )
        binding.rvDetailItems.layoutManager = LinearLayoutManager(this)
        binding.rvDetailItems.adapter = adapter
    }

    private fun ambilDetailPesanan(idPesanan: Int) {
        ApiClient.apiService.getDetailPesanan(idPesanan).enqueue(object : Callback<ApiResponse<List<DetailPesananResponse>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<DetailPesananResponse>>>,
                response: Response<ApiResponse<List<DetailPesananResponse>>>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val detailList = response.body()?.data ?: emptyList()
                    val items = detailList.map { detail ->
                        Keranjang(
                            id = detail.id ?: 0,
                            idProduk = detail.idProduk ?: 0,
                            namaProduk = detail.namaProduk ?: "Produk",
                            hargaSatuan = detail.hargaSatuan ?: 0,
                            jumlahBarang = detail.jumlahBarang ?: 1
                        )
                    }
                    adapter.updateData(items)
                } else {
                    Toast.makeText(this@DetailPesananActivity, "Gagal memuat detail pesanan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<DetailPesananResponse>>>, t: Throwable) {
                Toast.makeText(this@DetailPesananActivity, "Koneksi gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}