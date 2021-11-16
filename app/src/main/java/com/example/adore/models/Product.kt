package com.example.adore.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "products"
)
data class Product(
    @PrimaryKey
    val _id: String,
    val productId: Int,
    val name: String,
    val price: Float,
    val category: Category,
    val color: List<String>,
    val description: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val inStockCount: List<Stock>
): Serializable
