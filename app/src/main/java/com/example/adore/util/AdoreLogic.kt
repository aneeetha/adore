package com.example.adore.util

import com.example.adore.models.enums.CustomLabel

class AdoreLogic {
    companion object{
        fun getDiscount(labels: List<String>): Int = labels.maxByOrNull { code ->
            CustomLabel.values().first { it.code == code }.discountInPercentage
        }?.let {code->
            val label:CustomLabel = CustomLabel.values().first{ it.code == code }
            label.discountInPercentage
        }?:0

        fun getDeliveryCharge(price: Float): Int = when{
            price<1 -> 0
            price<500 -> 50
            price<1000 -> 30
            else -> 0
        }
    }
}