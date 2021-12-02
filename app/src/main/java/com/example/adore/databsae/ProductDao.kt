package com.example.adore.databsae

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.adore.models.dataClasses.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * from products WHERE _id = :key")
    fun getProductWithId(key: String): LiveData<Product>

    @Delete
    suspend fun deleteProduct(product: Product)
}