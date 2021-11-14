package com.example.adore.ui.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.databsae.ProductDatabaseDao
import com.example.adore.models.Product
import com.example.adore.ui.viewmodels.ProductDetailsViewModel

class ProductDetailsViewModelProviderFactory (
    private val product: Product,
    private val dataSource: ProductDatabaseDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(product, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}