package com.example.adore.ui.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.models.dataClasses.Product
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.ProductDetailsViewModel

class ProductDetailsViewModelProviderFactory (
    val app: Application,
    private val product: Product,
    private val repository: AdoreRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(app, product, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}