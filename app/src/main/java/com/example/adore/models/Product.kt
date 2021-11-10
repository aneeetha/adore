package com.example.adore.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "products"
)
data class Product(
    @PrimaryKey
    val _id: String,
    val productId: Int,
    val name: String,
    val price: Int,
    val type: String,//enum
    val category: String,
    val color: String,
    val description: String,
    val imageUrl: String,
    val isAvailable: Boolean,
)
