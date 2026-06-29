package com.app.lokalmarket.v1.data.model

import com.google.gson.annotations.SerializedName

data class DetailPesananResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("idPesanan")
    val idPesanan: Int?,
    @SerializedName("idProduk")
    val idProduk: Int?,
    @SerializedName("namaProduk")
    val namaProduk: String?,
    @SerializedName("jumlahBarang")
    val jumlahBarang: Int?,
    @SerializedName("hargaSatuan")
    val hargaSatuan: Int?
)