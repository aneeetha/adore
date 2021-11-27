package com.example.adore.models

import com.example.adore.models.enums.DressSize

data class Stock(
    val id: String,
    val size: DressSize,
    var availableCount: Int
)