package com.example.adore.models

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.example.adore.models.enums.DressSize

data class CartItem(
    @PrimaryKey
    val _id: String,
    val sku: String,
    @Embedded
    val productDetails: ProductDetails,
    val selectedSize: DressSize,
    val quantity: Int
)

data class ProductDetails(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float
)
