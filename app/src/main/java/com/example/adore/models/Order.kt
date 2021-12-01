package com.example.adore.models

import com.example.adore.models.enums.DressSize
import java.io.Serializable

data class Order(
    val _id: String,
    val products: List<OrderProductDetails>,
    val addressId: Int,
    val deliveryCharge: Float,
    val totalPrice: Float
):Serializable

data class OrderProductDetails(
    val sku: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val sellingPrice: Float,
    val selectedSize: DressSize,
    val quantity: Int,
    val discount: Int,
    val orderStatus: String
):Serializable