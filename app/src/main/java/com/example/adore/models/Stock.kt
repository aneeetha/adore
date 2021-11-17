package com.example.adore.models

import androidx.room.ColumnInfo
import com.example.adore.models.enums.DressSize

data class Stock(
    @ColumnInfo(name = "_id")
    val id: String,
    val size: DressSize,
    var availableCount: Int
)