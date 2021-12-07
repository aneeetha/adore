package com.example.adore.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.adore.AdoreApplication
import com.example.adore.models.entities.User
import com.example.adore.models.responses.CurrentUserResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class HomeViewModel(
    val app: Application,
    private val adoreRepository: AdoreRepository
) : AndroidViewModel(app) {

    private val _productsWithLabel: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val productsWithLabel: LiveData<Resource<ProductResponse>>
        get() = _productsWithLabel

    private val _currentUser = MutableLiveData<Resource<CurrentUserResponse>>()
    val currentUser
        get() = _currentUser

    private val _showSnackBarMessage = MutableLiveData<String?>()
    val showSnackBarMessage
        get() = _showSnackBarMessage

    init {
        getCurrentUser()
        getProductsWithCustomLabel()
    }

    fun getProductsWithCustomLabel() = viewModelScope.launch {
        safeProductsWithLabelCall("100")
    }

    private suspend fun safeProductsWithLabelCall(labelId: String){
        _productsWithLabel.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getProductsWithLabel(labelId) //Seasonal
                _productsWithLabel.value = handleResponse(response)
            }else{
                _productsWithLabel.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _productsWithLabel.postValue(Resource.Error("Network Failure!"))
                else -> _productsWithLabel.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    fun getUser(userId: Long)= adoreRepository.getUser(userId)

    private fun getCurrentUser() = viewModelScope.launch {
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
                    Resource.Empty()
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
               capabilities.hasTransport(TRANSPORT_WIFI) -> true
               capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
               capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
               else -> false
           }
       }else{
           connectivityManager.activeNetworkInfo?.run {
               return when(type){
                   TYPE_WIFI -> true
                   TYPE_MOBILE -> true
                   TYPE_ETHERNET -> true
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
                setCurrentUser(it.userId)
            }else{
                _showSnackBarMessage.value = "Incorrect password!"
                getCurrentUser()
            }
        }?:run{
            _showSnackBarMessage.postValue("Mobile no is not registered!")
            getCurrentUser()
        }
    }

    private fun setCurrentUser(userId: Long) = viewModelScope.launch {
        adoreRepository.setCurrentUser(userId)
        getCurrentUser()
    }

    fun doneShowingSnackBarMessage(){
        _showSnackBarMessage.value = null
    }
}