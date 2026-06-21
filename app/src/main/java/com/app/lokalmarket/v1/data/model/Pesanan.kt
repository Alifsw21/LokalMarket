package com.app.lokalmarket.v1.data.model

data class Pesanan (
    val id: Int,
    val idPengguna: Int,
    val tanggalPesanan: String,
    val totalHarga: Int,
    val alamatPengiriman: String,
    val statusPesanan: String
)