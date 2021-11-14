package com.example.adore.models

import androidx.room.ColumnInfo

data class Stock(
    @ColumnInfo(name = "_id")
    val id: String,
    val size: DressSize,
    var availableCount: Int
)