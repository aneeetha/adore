package com.example.adore.ui.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.ProductsViewModel

class ProductsViewModelProviderFactory(
    val app: Application,
    private val adoreRepository: AdoreRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            return ProductsViewModel(app, adoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}