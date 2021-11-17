package com.example.adore.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adore.models.enums.Category
import com.example.adore.models.enums.Color
import com.example.adore.models.enums.Gender
import com.example.adore.models.enums.ProductType
import java.io.Serializable

@Entity(
    tableName = "products"
)
data class Product(
    @PrimaryKey
    val _id: String,
    val itemGroupId: Int,
    val name: String,
    val price: Float,
    val color: List<Color>,
    val gender: Gender,
    val category: List<Category>,
    val productType: List<ProductType>,
    val description: String,
    val imageUrl: String,
    val isAvailable: Boolean,
    val stock: List<Stock>,
    val sizeChartUrl: String,
    val customLabels: List<String>
): Serializable



