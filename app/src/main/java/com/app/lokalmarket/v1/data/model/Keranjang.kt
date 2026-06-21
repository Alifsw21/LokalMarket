package com.app.lokalmarket.v1.data.model

data class Keranjang (
    val id: Int = 0,
    val idProduk: Int,
    val namaProduk: String,
    val hargaSatuan: Int,
    var jumlahBarang: Int
)