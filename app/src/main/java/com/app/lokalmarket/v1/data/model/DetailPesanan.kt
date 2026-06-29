package com.app.lokalmarket.v1.data.model

import com.google.gson.annotations.SerializedName

data class DetailPesanan (
    @SerializedName("idProduk")
    val idProduk: Int,
    @SerializedName("jumlahBarang")
    val jumlahBarang: Int,
    @SerializedName("hargaSatuan")
    val hargaSatuan: Int
)