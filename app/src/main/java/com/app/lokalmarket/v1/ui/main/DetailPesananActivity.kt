package com.app.lokalmarket.v1.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.ActivityDetailPesananBinding
import com.app.lokalmarket.v1.data.model.Keranjang
import com.app.lokalmarket.v1.ui.adapter.KeranjangListAdapter
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
        val totalHarga = intent.getIntExtra("EXTRA_TOTAL", 15000000)
        val alamat = intent.getStringExtra("EXTRA_ALAMAT") ?: "Alamat tidak tersedia"

        // 2. Tangkap Data Detail Produk (Intinya di sini!)
        val idProduk = intent.getIntExtra("EXTRA_ID_PRODUK", 1) // Berguna untuk nampilin gambar
        val namaProduk = intent.getStringExtra("EXTRA_NAMA_PRODUK") ?: "Sepatu Anti Mainstream"
        val hargaProduk = intent.getIntExtra("EXTRA_HARGA_PRODUK", totalHarga)
        val qty = intent.getIntExtra("EXTRA_QTY", 1)

        if (idPesanan == -1) {
            finish()
            return
        }

        setupUI(idPesanan, tanggal, totalHarga, alamat)

        // 3. Langsung tampilkan ke RecyclerView tanpa Web Service / API
        tampilkanItemPesanan(idProduk, namaProduk, hargaProduk, qty)
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

    private fun tampilkanItemPesanan(idProduk: Int, nama: String, harga: Int, qty: Int) {
        // Buat objek Keranjang bayangan (dummy) dari data Intent untuk dikirim ke Adapter
        val itemProduk = Keranjang(
            id = 0,
            idProduk = idProduk,
            namaProduk = nama,
            hargaSatuan = harga,
            jumlahBarang = qty
        )

        // Masukkan ke adapter (jadikan list berisi 1 item)
        adapter = KeranjangListAdapter(mutableListOf(itemProduk), showDeleteButton = false) {
            // Kosongkan karena tombol hapus tidak ada
        }

        binding.rvDetailItems.layoutManager = LinearLayoutManager(this)
        binding.rvDetailItems.adapter = adapter
    }
}