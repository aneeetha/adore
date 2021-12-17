package com.example.adore.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.adore.AdoreApplication
import com.example.adore.databsae.SessionManager
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.entities.User
import com.example.adore.models.entities.UserDetailUpdate
import com.example.adore.models.enums.Gender
import com.example.adore.models.responses.CurrentUserResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.util.*

class SharedUserProfileViewModel(app: Application, val adoreRepository: AdoreRepository): AndroidViewModel(app) {

    var dob: Date? = null
    var gender: Gender? = null

    private val sessionManager = SessionManager(app)

    fun logoutCurrentUser()=viewModelScope.launch{
        val sessionManager = SessionManager(getApplication())
        sessionManager.logoutUserFromSession()
    }

    fun insertNewAddress(address: Address)= viewModelScope.launch {
        adoreRepository.addNewAddressToUser(address)
    }

    fun deleteAddress(address: Address) = viewModelScope.launch {
        adoreRepository.deleteAddress(address)
    }
    fun getAddressesOfCurrentUser() = adoreRepository.getAddressesOfUser(sessionManager.getUserId())

    fun updateAddress(address: AddressDetailUpdate) = viewModelScope.launch {
        adoreRepository.updateAddress(address)
    }


    fun getUserDetails() = adoreRepository.getUser(sessionManager.getUserId())


    fun saveUserDetails(userId: Long, email: String?)=viewModelScope.launch {
        adoreRepository.updateUserDetails(UserDetailUpdate(userId, email, dob, gender))
    }

}