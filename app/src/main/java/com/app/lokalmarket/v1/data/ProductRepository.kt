package com.app.lokalmarket.v1.data

import com.app.lokalmarket.v1.model.ProductItem

object ProductRepository {

    fun getProducts(): List<ProductItem> {
        return listOf(
            ProductItem(
                id = 1,
                name = "Sepatu Lari",
                price = 500000,
                discount = "Discount 10%"
            )
        )
    }

    fun getProductById(id: Int): ProductItem? = getProducts().find { it.id == id }
}