package com.example.adore.models.enums

enum class CustomLabel(val code: String, val discountInPercentage: Int) {
    Seasonal("100", 10),
    WholeSale("202", 30),
    Clearance("201", 50),
    Diwali("500", 15),
    Christmas("510", 10),
}