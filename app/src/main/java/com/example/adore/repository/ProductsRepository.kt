package com.example.adore.repository

import com.example.adore.api.RetrofitInstance
import com.example.adore.databsae.ProductDatabase

class ProductsRepository(
    val db: ProductDatabase
) {
    suspend fun getProducts() = RetrofitInstance.api.getProducts()

    suspend fun searchForProducts(searchQuery: String) = RetrofitInstance.api.searchForProducts(searchQuery)
}
