package com.example.adore.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.adore.databsae.CategoryTypeConverter
import com.example.adore.databsae.ColorTypeConverter
import com.example.adore.databsae.InStockCountTypeConverter
import java.io.Serializable

@Entity(
    tableName = "products"
)
data class Product(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: String,
    val productId: Int,
    val name: String,
    val price: Float,
    val category: Category,
    val color: List<String>,
    val description: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val inStockCount: List<Stock>
): Serializable
