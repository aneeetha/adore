package com.example.adore.models.responses

import com.example.adore.models.dataClasses.CartItem

data class CartResponse(
    val cartItems: List<CartItem>
)
