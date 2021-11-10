package com.example.adore.ui.category

import androidx.lifecycle.ViewModel
import com.example.adore.R

class VarietiesViewModel(private val chosenCategory: String): ViewModel() {

    val varieties = VarietiesViewModel.getVarieties(chosenCategory)
    val varietiesImages = VarietiesViewModel.getImages(chosenCategory)



        companion object{
        private val menVarieties = listOf("Shirts", "T-Shirts", "Jeans", "Trackpants", "Ethnicwear", "Sportswear")
        private val womenVarieties = listOf("Tops & Tees", "Bottomwear", "Dresses", "Kurtas", "Ethnicwear", "Sportswear")

        private val menVarietiesImages = listOf(R.drawable.men_shirt, R.drawable.men_tshirt, R.drawable.men_jeans, R.drawable.men_ethic_wears, R.drawable.men_sports)
        private val womenVarietiesImages = listOf(R.drawable.men_shirt, R.drawable.men_tshirt, R.drawable.men_jeans, R.drawable.men_ethic_wears, R.drawable.men_sports)

        fun getImages(chosenCategory:String) = when(chosenCategory){
            "men" ->  menVarietiesImages
            else -> womenVarietiesImages
        }

        fun getVarieties(chosenCategory:String) = when(chosenCategory){
            "men" ->  menVarieties
            else -> womenVarieties
        }
    }
}