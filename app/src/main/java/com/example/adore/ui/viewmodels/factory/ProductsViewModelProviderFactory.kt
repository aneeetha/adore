package com.example.adore.ui.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.repository.ProductsRepository
import com.example.adore.ui.viewmodels.ProductsViewModel

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