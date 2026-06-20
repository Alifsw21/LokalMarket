package com.app.lokalmarket.v1.data

import com.app.lokalmarket.v1.model.ProductItem

object CartRepository {

    private val cartItems = mutableListOf<ProductItem>()

    fun addItem(product: ProductItem) {
        cartItems.add(product)
    }

    fun getItems(): List<ProductItem> {
        return cartItems.toList()
    }

    fun getTotalPrice(): Int {
        return cartItems.sumOf { it.price }
    }

    fun clearCart() {
        cartItems.clear()
    }
}