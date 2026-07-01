package com.app.lokalmarket.v1.data.model

data class Produk(
    val id: Int,
    val idKategori: Int,
    val imageFileName: String,
    val namaProduk: String,
    val deskripsi: String,
    val harga: Int,
    val diskon: Int? = null
)