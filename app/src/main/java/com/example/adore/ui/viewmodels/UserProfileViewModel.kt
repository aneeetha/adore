package com.example.adore.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.repository.AdoreRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(app: Application, val repository: AdoreRepository): AndroidViewModel(app) {

    fun logoutCurrentUser()=viewModelScope.launch{
        repository.setCurrentUser(0)
    }
}