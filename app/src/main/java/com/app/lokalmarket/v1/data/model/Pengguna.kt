package com.app.lokalmarket.v1.data.model

import com.google.gson.annotations.SerializedName

data class Pengguna (
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nama") // Pastikan ini sesuai dengan kolom di database/JSON backend
    val nama: String?,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("password")
    val password: String?
)