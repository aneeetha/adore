package com.example.adore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.models.entities.User
import com.example.adore.repository.AdoreRepository
import kotlinx.coroutines.launch

class SignupViewModel(val repository: AdoreRepository): ViewModel() {
    fun insertNewUser(user: User) = viewModelScope.launch {
        val a = repository.addNewUser(user)
        Log.e("Activity", a.toString())
    }

    fun setCurrentUser(userId: Int?) = viewModelScope.launch {
        userId?.let{ repository.setCurrentUser(it) }
    }
}