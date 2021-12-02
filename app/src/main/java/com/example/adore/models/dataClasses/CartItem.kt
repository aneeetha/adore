package com.example.adore.models.dataClasses

import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.example.adore.models.enums.DressSize

data class CartItem(
    @PrimaryKey
    val _id: String,
    val sku: String,
    @Embedded
    val productDetails: CartProductDetails,
    val sellingPrice: Float,
    val discount: Int,
    val selectedSize: DressSize,
    val quantity: Int
)

data class CartProductDetails(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val availableCount: Int
)
