package com.app.lokalmarket.v1.data.remote

import com.app.lokalmarket.v1.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("pengguna/login")
    fun login(
        @Body request: LoginRequest
    ): Call<ApiResponse<Pengguna>>

    @POST("pengguna/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<ApiResponse<Any>>

    @GET("produk")
    fun getAllProduk(): Call<ApiResponse<List<Produk>>>

    @GET("produk/{id}")
    fun getDetailProduk(
        @Path("id") id: Int
    ): Call<ApiResponse<Produk>>

    @GET("produk/cari")
    fun cariProduk(
        @Query("keyword") keyword: String
    ): Call<ApiResponse<List<Produk>>>

    @GET("produk/kategori/{idKategori}")
    fun getProdukByKategori(
        @Path("idKategori") idKategori: Int
    ): Call<ApiResponse<List<Produk>>>

    @GET("kategori")
    fun getAllKategori(): Call<ApiResponse<List<Kategori>>>

    @GET("kategori/{id}")
    fun getDetailKategori(
        @Path("id") id: Int
    ): Call<ApiResponse<Kategori>>

    @GET("promo")
    fun getAllPromo(): Call<ApiResponse<List<Promo>>>

    @GET("promo/{id}")
    fun getDetailPromo(
        @Path("id") id: Int
    ): Call<ApiResponse<Promo>>

    @POST("pesanan")
    fun buatPesanan(
        @Body request: CheckoutRequest
    ): Call<ApiResponse<Any>>

    @GET("pesanan")
    fun getRiwayatPesanan(@Query("idPengguna") idPengguna: Int): Call<ApiResponse<List<Pesanan>>>

    @DELETE("pesanan/{id}")
    fun hapusPesanan(
        @Path("id") id: Int
    ): Call<ApiResponse<Any>>

    @GET("pesanan/{id}")
    fun getDetailPesanan(
        @Path("id") id: Int
    ): Call<ApiResponse<List<DetailPesananResponse>>>
}