package com.example.adore.ui.viewmodels.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adore.repository.AdoreRepository
import com.example.adore.ui.viewmodels.SharedUserProfileViewModel

class UserProfileViewModelProviderFactory (val app: Application, val repository: AdoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedUserProfileViewModel::class.java)) {
            return SharedUserProfileViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}