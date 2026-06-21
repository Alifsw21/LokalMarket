package com.app.lokalmarket.v1.data.model

data class CheckoutRequest (
    val idPengguna: Int,
    val totalHarga: Int,
    val alamatPengiriman: String,
    val keranjang: List<DetailPesanan>
)