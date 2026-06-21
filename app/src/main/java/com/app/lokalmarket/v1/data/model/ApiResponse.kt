package com.app.lokalmarket.v1.data.model

data class ApiResponse<T> (
    val success: Boolean,
    val message: String?,
    val data: T?
)