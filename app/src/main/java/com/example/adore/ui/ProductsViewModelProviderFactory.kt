package com.example.adore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.repository.ProductsRepository

class ProductsViewModelProviderFactory(
    private val productsRepository: ProductsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            return ProductsViewModel(productsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}