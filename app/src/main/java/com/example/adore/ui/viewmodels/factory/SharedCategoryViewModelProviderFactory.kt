package com.example.adore.ui.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedCategoryViewModel

class SharedCategoryViewModelProviderFactory(val app: Application, val repository: AdoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedCategoryViewModel::class.java)) {
            return SharedCategoryViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}