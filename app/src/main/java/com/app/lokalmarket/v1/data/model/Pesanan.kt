package com.app.lokalmarket.v1.data.model

data class Pesanan(
    val id: Int,
    val tanggalPesanan: String,
    val alamat: String,
    val totalHarga: Int,
    val idProduk: Int? = 1,
    val namaProduk: String? = "Produk Lokal"
)