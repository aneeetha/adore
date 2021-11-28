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
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.responses.CurrentUserResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class SharedUserProfileViewModel(app: Application, val adoreRepository: AdoreRepository): AndroidViewModel(app) {

    private val _currentUser = MutableLiveData<Resource<CurrentUserResponse>>()
    val currentUser
        get() = _currentUser

    private val _navigateToAddressFragment = MutableLiveData<Boolean?>()
    val navigateToAddressFragment
        get() = _navigateToAddressFragment

    var currentAddress: Address? =null

    init {
        getCurrentUser()
    }

    fun logoutCurrentUser()=viewModelScope.launch{
        adoreRepository.setCurrentUser(0)
    }

    fun insertNewAddress(address: Address)= viewModelScope.launch {
        adoreRepository.addNewAddressToUser(address)
    }

    fun getLastInsertedAddress() = adoreRepository.getLastInsertedAddress()


    fun getAddressesOfCurrentUser(userId: Int) = adoreRepository.getAddressesOfUser(userId)

    fun updateAddress(address: AddressDetailUpdate) = viewModelScope.launch {
        adoreRepository.updateAddress(address)
        navigateToAddressFragment.value = true
    }

    private fun getCurrentUser()=viewModelScope.launch{
        safeGetCurrentUserCall()
    }

    private suspend fun safeGetCurrentUserCall(){
        _currentUser.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getCurrentUser()
                _currentUser.value = handleResponse(response)
                Log.e("Homee", "${currentUser.value?.data?.userId}")
            }else{
                _currentUser.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _currentUser.postValue(Resource.Error("Network Failure!"))
                else -> _currentUser.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun <T: Any> handleResponse(response: Response<T>): Resource<T> =
        if (response.isSuccessful) {
            when (response.code()) {
                200 -> {
                    response.body()!!.let {
                        Resource.Success(it)
                    }
                }
                else -> {
                    Resource.Error("Sorry! No result found :(")
                }
            }
        } else {
            val message = "An Error Occurred: " + when (response.code()) {
                404 -> "404! Resource Not found"
                500 -> "Server broken"
                502 -> "Bad Gateway"
                else -> "Unknown error"
            }
            Resource.Error(message)
        }


    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<AdoreApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun doneNavigatingToAddressFragment(){
        _navigateToAddressFragment.value = null
    }

}