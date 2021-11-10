package com.example.adore.databsae

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.adore.models.Product

@Dao
interface ProductDatabaseDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Delete
    fun deleteProduct(product: Product)
}