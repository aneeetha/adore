package com.example.adore.models.responses

import com.example.adore.models.dataClasses.Order

data class OrderResponse(
    val orders: List<Order>
)