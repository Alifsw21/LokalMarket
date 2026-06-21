package com.app.lokalmarket.v1.data.model

data class Promo (
    val id: Int,
    val judulPromo: String,
    val gambarPromo: String,
    val statusAktif: String,
    val diskon: Int
)