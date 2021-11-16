package com.example.adore.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favo"
)
data class Favo(
    @PrimaryKey
    val userId: String,
    var favlist: List<String>
)
