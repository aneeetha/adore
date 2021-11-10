package com.example.adore.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VarietiesViewModelFactory(
    private val chosenCategory: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VarietiesViewModel::class.java)) {
            return VarietiesViewModel(chosenCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}