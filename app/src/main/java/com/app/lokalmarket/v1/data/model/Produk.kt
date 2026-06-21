package com.app.lokalmarket.v1.data.model

data class Produk(
    val id: Int,
    val namaProduk: String,
    val deskripsi: String,
    val harga: Int,
    val gambarProduk: String,
    val kategori: String? = null,
    val diskon: Int? = null
)