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
import com.example.adore.databsae.SessionManager
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

    private val _showLoginDialog = MutableLiveData<Boolean?>()
    val showLoginDialog
        get() = _showLoginDialog

    private val _showToastMessage = MutableLiveData<String?>()
    val showToastMessage
        get() = _showToastMessage

    init {
        getProductsWithCustomLabel()
        _showLoginDialog.value = true
    }

    private fun getProductsWithCustomLabel() = viewModelScope.launch {
        safeProductsWithLabelCall("100")
    }

    private suspend fun safeProductsWithLabelCall(labelId: String) {
        _productsWithLabel.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = adoreRepository.getProductsWithLabel(labelId) //Seasonal
                _productsWithLabel.value = handleResponse(response)
            } else {
                _productsWithLabel.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _productsWithLabel.postValue(Resource.Error("Network Failure!"))
                else -> _productsWithLabel.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun <T : Any> handleResponse(response: Response<T>): Resource<T> =
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


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<AdoreApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    fun validateUser(mobileNo: String, password: String) = viewModelScope.launch {
        adoreRepository.getUserWithMobileNo(mobileNo)?.let {
            if (it.password == password) {
                _showToastMessage.value = "Logged in!"
                it.apply {
                    val sessionManager = SessionManager(getApplication())
                    sessionManager.createLoginSession(
                        userName,
                        mobileNo,
                        password,
                        userId
                    )
                    doneShowingLoginDialog()
                    Log.e("HomeViewModel", "${sessionManager.getUserDetailsFromSession()}")
                }
            } else {
                _showToastMessage.value = "Incorrect password!"
                _showLoginDialog.value = true
            }
        } ?: run {
            _showToastMessage.postValue("Mobile no is not registered!")
            _showLoginDialog.value = true
        }
    }

    private fun doneShowingLoginDialog() {
        _showLoginDialog.value = null
    }

    fun doneShowingToastMessage() {
        _showToastMessage.value = null
    }
}