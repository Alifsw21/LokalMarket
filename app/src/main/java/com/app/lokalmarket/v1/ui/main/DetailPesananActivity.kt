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

        val idPesanan = intent.getIntExtra("EXTRA_ID_PESANAN", -1)
        val tanggal = intent.getStringExtra("EXTRA_TANGGAL") ?: ""
        val totalHarga = intent.getIntExtra("EXTRA_TOTAL", 0)
        val alamat = intent.getStringExtra("EXTRA_ALAMAT") ?: ""

        if (idPesanan == -1) {
            finish()
            return
        }

        setupUI(idPesanan, tanggal, totalHarga, alamat)
        loadDetailPesanan(idPesanan)
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

        // Gunakan showDeleteButton = false agar tombol hapus tidak muncul di detail pesanan
        adapter = KeranjangListAdapter(mutableListOf(), showDeleteButton = false) {
            // Tombol hapus tidak ada di halaman detail
        }
        binding.rvDetailItems.layoutManager = LinearLayoutManager(this)
        binding.rvDetailItems.adapter = adapter
    }

    private fun loadDetailPesanan(id: Int) {
        ApiClient.apiService.getDetailPesanan(id).enqueue(object : Callback<ApiResponse<List<DetailPesananResponse>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<DetailPesananResponse>>>,
                response: Response<ApiResponse<List<DetailPesananResponse>>>
            ) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val details = response.body()?.data ?: emptyList()
                    
                    // Melakukan mapping dengan null safety menggunakan Elvis operator
                    val keranjangItems = details.map {
                        Keranjang(
                            id = it.id ?: 0,
                            idProduk = it.idProduk ?: 0,
                            namaProduk = it.namaProduk ?: "Produk Unknown",
                            hargaSatuan = it.hargaSatuan ?: 0,
                            jumlahBarang = it.jumlahBarang ?: 1
                        )
                    }
                    
                    if (keranjangItems.isEmpty()) {
                        Toast.makeText(this@DetailPesananActivity, "Detail pesanan kosong", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.updateData(keranjangItems)
                    }
                } else {
                    Toast.makeText(this@DetailPesananActivity, "Gagal memuat detail pesanan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<DetailPesananResponse>>>, t: Throwable) {
                Toast.makeText(this@DetailPesananActivity, "Error koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
