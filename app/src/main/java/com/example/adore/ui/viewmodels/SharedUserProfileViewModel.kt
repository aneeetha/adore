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

    private val _currentUser = MutableLiveData<Resource<CurrentUserResponse>>()
    val currentUser
        get() = _currentUser

    private val _navigateToUserProfile = MutableLiveData<Boolean?>()
    val navigateToUserProfile
        get() = _navigateToUserProfile

    private val _showSnackBarMessage = MutableLiveData<String?>()
    val showSnackBarMessage
        get() = _showSnackBarMessage

    private val _showActionViews = MutableLiveData<Boolean?>()
    val showActionViews
        get() = _showActionViews

    var dob: Date? = null
    var gender: Gender? = null

    init {
        getCurrentUser()
    }

    fun logoutCurrentUser()=viewModelScope.launch{
        adoreRepository.setCurrentUser(0)
    }

    fun insertNewAddress(address: Address)= viewModelScope.launch {
        adoreRepository.addNewAddressToUser(address)
    }

    fun deleteAddress(address: Address) = viewModelScope.launch {
        adoreRepository.deleteAddress(address)
    }

    fun getLastInsertedAddress() = adoreRepository.getLastInsertedAddress()


    fun getAddressesOfCurrentUser(userId: Long) = adoreRepository.getAddressesOfUser(userId)

    fun updateAddress(address: AddressDetailUpdate) = viewModelScope.launch {
        adoreRepository.updateAddress(address)
        navigateToUserProfile.value = true
    }


    fun getUserDetails(userId: Long) = adoreRepository.getUser(userId)


    fun saveUserDetails(userId: Long, email: String?)=viewModelScope.launch {
        adoreRepository.updateUserDetails(UserDetailUpdate(userId, email, dob, gender))
    }

    fun doneShowingSnackBar(){
        _showSnackBarMessage.value = null
    }

    fun doneShowingActionViews(){
        _showActionViews.value = null
    }


    fun getCurrentUser() = viewModelScope.launch{
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

    fun validateUser(mobileNo:String, password:String) = viewModelScope.launch {
        adoreRepository.getUserWithMobileNo(mobileNo)?.let {
            if (it.password == password) {
                _showSnackBarMessage.value = "Logged in!"
                _showActionViews.value = true
                setCurrentUser(it.userId)
            }else{
                _showSnackBarMessage.value = "Incorrect password!"
                getCurrentUser()
            }
            Log.e("AA", "${it.userId}")
        }?:run{
            _showSnackBarMessage.postValue("Mobile no is not registered!")
            getCurrentUser()
        }
    }

    private fun setCurrentUser(userId: Long) = viewModelScope.launch {
        adoreRepository.setCurrentUser(userId)
    }


    fun doneNavigatingToUserProfile(){
        _navigateToUserProfile.value = null
    }

}