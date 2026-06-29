package com.app.lokalmarket.v1.data.model

import com.google.gson.annotations.SerializedName

data class CheckoutRequest (
    @SerializedName("idPengguna")
    val idPengguna: Int,
    
    @SerializedName("totalHarga")
    val totalHarga: Int,
    
    @SerializedName("alamat")
    val alamatPengiriman: String,
    
    @SerializedName("keranjang")
    val keranjang: List<DetailPesanan>
)