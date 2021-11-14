package com.example.adore.databsae

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.adore.models.Product

@Dao
interface ProductDatabaseDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * from products WHERE _id = :key")
    fun getProductWithId(key: String): LiveData<Product>

    @Delete
    fun deleteProduct(product: Product)
}