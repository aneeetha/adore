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
import com.example.adore.models.dataClasses.CartItem
import com.example.adore.models.enums.Category
import com.example.adore.models.enums.Gender
import com.example.adore.models.enums.ProductType
import com.example.adore.models.responses.*
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductsViewModel(
    val app: Application,
    private val adoreRepository: AdoreRepository
) : AndroidViewModel(app) {


    private val _productsResult: MutableLiveData<Resource<ProductResponse>?> = MutableLiveData()
    val productsResult
        get() = _productsResult

    private val _searchResult: MutableLiveData<Resource<ProductResponse>?> = MutableLiveData()
    val searchResult
        get() = _searchResult

    private val _favlistResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val favlistResult
        get() = _favlistResult

    private val _cartItems: MutableLiveData<Resource<CartResponse>> = MutableLiveData()
    val cartItems
        get() = _cartItems

    private val _orders: MutableLiveData<Resource<OrderResponse>> = MutableLiveData()
    val orders
        get() = _orders

    private val _totalPrice: MutableLiveData<Float> = MutableLiveData()
    val totalPrice
        get() = _totalPrice

    private val _navigateToOrderSuccessPage = MutableLiveData<Boolean?>()
    val navigateToOrderSuccessPage
        get() = _navigateToOrderSuccessPage

    private val _cartSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val cartSnackBarMessage
        get() = _cartSnackBarMessage

    private val _favoSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val favoSnackBarMessage
        get() = _favoSnackBarMessage

    private val sessionManager = SessionManager(app)


    fun getAddressesOfCurrentUser() = adoreRepository.getAddressesOfUser(sessionManager.getUserId())

    fun getSearchResult(searchQuery: String) = viewModelScope.launch {
        safeSearchForProductsCall(searchQuery)
    }

    fun getFavlist() = viewModelScope.launch {
        safeGetFavlistCall()
    }

    fun getCartItem() = viewModelScope.launch {
        safeGetCartItemsCall()
    }

    fun getOrders() = viewModelScope.launch {
        safeGetOrdersCall()
    }

    fun getProducts(gender: Gender, productType: ProductType, category: Category? = null) =
        viewModelScope.launch {
            safeGetProducts(gender, productType, category)
        }

    fun cartItemQuantityChanged(quantity: Int, cartItemId: String) = viewModelScope.launch {
        _cartSnackBarMessage.value =
            handleApiTransactionResponse(
                adoreRepository.updateCart(
                    cartItemId,
                    quantity,
                    sessionManager.getUserId()
                )
            )
        getCartItem()
    }

    fun removeCartItem(cartItemId: String) = viewModelScope.launch {
        _cartSnackBarMessage.value =
            handleApiTransactionResponse(
                adoreRepository.removeCartItem(
                    cartItemId,
                    sessionManager.getUserId()
                )
            )
        getCartItem()
    }

    fun placeOrder(addressId: Int) = viewModelScope.launch {
        _cartSnackBarMessage.value = handleApiTransactionResponse(
            adoreRepository.placeOrder(
                addressId,
                sessionManager.getUserId()
            )
        )
        _navigateToOrderSuccessPage.value = true
    }

    fun removeFavoItem(productId: String) = viewModelScope.launch {
        _favoSnackBarMessage.value =
            handleApiTransactionResponse(
                adoreRepository.removeFavoItem(
                    productId,
                    sessionManager.getUserId()
                )
            )
        getFavlist()
    }

    fun getTotalPrice(cartItems: List<CartItem>) {
        var sum = 0F
        cartItems.forEach {
            sum += it.quantity * it.sellingPrice
        }
        _totalPrice.value = sum
    }

    fun doneShowingSnackBarInCart() {
        _cartSnackBarMessage.value = null
    }

    fun doneShowingSnackBarInFavo() {
        _favoSnackBarMessage.value = null
    }

    fun doneNavigatingToOrderSuccessPage() {
        _navigateToOrderSuccessPage.value = null
    }

    fun doneShowingSearchResults() {
        _searchResult.value = null
    }

    fun doneShowingProductsResult() {
        _productsResult.value = null
    }

    private suspend fun safeSearchForProductsCall(searchQuery: String) {
        _searchResult.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = adoreRepository.searchForProducts(searchQuery) //Seasonal
                _searchResult.value = handleResponse(response)
            } else {
                _searchResult.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchResult.postValue(Resource.Error("Network Failure!"))
                else -> _searchResult.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetProducts(
        gender: Gender,
        productType: ProductType,
        category: Category?
    ) {
        _productsResult.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = category?.let {
                    adoreRepository.getProductsOfCategory(gender, productType, it)
                } ?: adoreRepository.getProductsOfType(gender, productType)
                _productsResult.value = handleResponse(response)
            } else {
                _productsResult.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _productsResult.postValue(Resource.Error("Network Failure!"))
                else -> _productsResult.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetFavlistCall() {
        _favlistResult.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = adoreRepository.getFavlist(sessionManager.getUserId())
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

    private suspend fun safeGetCartItemsCall() {
        _cartItems.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = adoreRepository.getCartItems(sessionManager.getUserId())
                _cartItems.value = handleResponse(response)
            } else {
                _cartItems.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _cartItems.postValue(Resource.Error("Network Failure!"))
                else -> _cartItems.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetOrdersCall() {
        _orders.value = Resource.Loading()
        try {
            if (hasInternetConnection()) {
                val response = adoreRepository.getOrders(sessionManager.getUserId())
                _orders.value = handleResponse(response)
            } else {
                _orders.value = Resource.Error("No internet connection :(")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _orders.postValue(Resource.Error("Network Failure!"))
                else -> _orders.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private fun <T : Any> handleResponse(response: Response<T>): Resource<T> =
        if (response.isSuccessful) {
            when (response.code()) {
                200 -> {
                    Log.e("ViewModel", "${response.code()}")
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


    private fun handleApiTransactionResponse(response: Response<ApiTransactionResponse>): Resource<ApiTransactionResponse> =
        if (response.isSuccessful) {
            response.body()!!.let {
                Resource.Success(it)
            }
        } else {
            val message = "An error occurred: " + when (response.code()) {
                404 -> "404! Resource not found"
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
