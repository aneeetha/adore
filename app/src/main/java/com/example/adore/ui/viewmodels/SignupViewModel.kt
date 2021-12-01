package com.example.adore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.models.entities.User
import com.example.adore.repository.AdoreRepository
import kotlinx.coroutines.launch

class SignupViewModel(val repository: AdoreRepository): ViewModel() {

    private val _navigateToHomeFragment = MutableLiveData<Boolean?>()
    val navigateToHomeFragment
        get() = _navigateToHomeFragment

    private val _showSnackBarMessage = MutableLiveData<String?>()
    val showSnackBarMessage
        get() = _showSnackBarMessage


    fun insertNewUser(user: User) = viewModelScope.launch {
        val a = repository.addNewUser(user)
        setCurrentUser(user.userId)
        Log.e("Activity", a.toString())

        _showSnackBarMessage.value = "SignUp Successful :)"
        _navigateToHomeFragment.value = true
    }

    private fun setCurrentUser(userId: Long) = viewModelScope.launch { repository.setCurrentUser(userId) }

    fun doneNavigatingToHomeFragment(){
        _navigateToHomeFragment.value = null
    }

    fun doneShowingSnackBar(){
        _showSnackBarMessage.value = null
    }

}