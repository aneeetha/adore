package com.example.adore.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.adore.AdoreApplication
import com.example.adore.databsae.SessionManager
import com.example.adore.models.enums.DressSize
import com.example.adore.models.dataClasses.Product
import com.example.adore.models.responses.ApiTransactionResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.AdoreLogic
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductDetailsViewModel(
    app: Application,
    val product: Product,
    private val repository: AdoreRepository
) :
    AndroidViewModel(app) {

    private val _favlistResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val favlistResult
        get() = _favlistResult

    private val sessionManager = SessionManager(app)

    init {
        getFavlist(sessionManager.getUserId())
    }

    private var chosenSize: DressSize? = null
    fun setChosenProductSize(size: DressSize) {
        chosenSize = size
        _addToCartClicked.value = false
    }

    private var _addToCartClicked = MutableLiveData<Boolean?>()
    val addToCartClicked
        get() = _addToCartClicked

    private var _navigateToFavoFragment = MutableLiveData<Boolean>()
    val navigateToFavoFragment
        get() = _navigateToFavoFragment

    private var _favoButtonClicked = MutableLiveData<Boolean?>()
    val favoButtonClicked
        get() = _favoButtonClicked

    private var _navigateToCartFragment = MutableLiveData<Boolean>()
    val navigateToCartFragment
        get() = _navigateToCartFragment

    private var _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment
        get() = _navigateToSearchFragment

    private var _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar

    private var _snackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val snackBarMessage: LiveData<Resource<ApiTransactionResponse>?>
        get() = _snackBarMessage


    fun addToFavlistClicked() {
        _favoButtonClicked.value = true
    }

    fun addToFavlist() {
        viewModelScope.launch {
            _snackBarMessage.value =
                handleApiTransactionResponse(repository.addProductToFav(product._id, sessionManager.getUserId()))
            doneActionForFavoButton()
        }
    }

    fun onAddToCartClicked() {
        chosenSize?.let {
            if (addToCartClicked.value == true) {
                goToCartFragmentClicked()
            } else {
                _addToCartClicked.value = true
                viewModelScope.launch {
                    _snackBarMessage.value = handleApiTransactionResponse(
                        repository.addItemToCart(
                            product._id,
                            it.name,
                            1,
                            AdoreLogic.getDiscount(product.customLabels),
                            sessionManager.getUserId()
                        )
                    )
                }
            }
        } ?: setShowSnackBar()
    }


    fun doneShowingSnackBarWithMessage() {
        _snackBarMessage.value = null
    }

    private fun doneActionForFavoButton() {
        favoButtonClicked.value = null
    }


    fun goToCartFragmentClicked() {
        _navigateToCartFragment.value = true
    }

    fun goToSearchFragmentClicked() {
        _navigateToSearchFragment.value = true
    }

    fun goToFavoFragmentClicked() {
        viewModelScope.launch {
            Log.i("FavoFragment", "Clicked!!!")
            _navigateToFavoFragment.value = true
        }
    }

    fun setNavigatedToCartFragment() {
        _navigateToCartFragment.value = false
    }

    fun setNavigatedToFavoFragment() {
        _navigateToFavoFragment.value = false
    }

    fun setNavigatedToSearchFragment() {
        _navigateToSearchFragment.value = false
    }

    private fun setShowSnackBar() {
        _showSnackBar.value = true
    }

    fun doneShowingSnackBar() {
        _showSnackBar.value = null
    }


    private fun handleApiTransactionResponse(response: Response<ApiTransactionResponse>): Resource<ApiTransactionResponse> =
        if (response.isSuccessful) {
            response.body()!!.let {
                Resource.Success(it)
            }
        } else {
            val message = "Error: " + when (response.code()) {
                404 -> "Not found"
                500 -> "Server broken"
                502 -> "Bad Gateway"
                else -> "Unknown error"
            }
            Resource.Error(message)
        }

    fun getFavlist(userId: Long) = viewModelScope.launch {
        safeGetFavlistCall(userId)
    }

    private suspend fun safeGetFavlistCall(userId: Long) {
        _favlistResult.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = repository.getFavlist(userId)
                _favlistResult.value = handleResponse(response)
            } else {
                _favlistResult.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _favlistResult.postValue(Resource.Error("Network Failure!"))
                else -> _favlistResult.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    fun removeFavoItem() = viewModelScope.launch {
        _snackBarMessage.value =
            handleApiTransactionResponse(repository.removeFavoItem(product._id, sessionManager.getUserId()))
        doneActionForFavoButton()
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
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}