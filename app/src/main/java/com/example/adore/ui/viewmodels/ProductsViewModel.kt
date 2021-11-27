package com.example.adore.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.adore.AdoreApplication
import com.example.adore.models.CartItem
import com.example.adore.models.responses.ApiTransactionResponse
import com.example.adore.models.responses.CartResponse
import com.example.adore.models.responses.CurrentUserResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductsViewModel(
    val app: Application,
    private val adoreRepository: AdoreRepository
) : AndroidViewModel(app) {

    private val _currentUser: MutableLiveData<Resource<CurrentUserResponse>> = MutableLiveData()
    val currentUser: LiveData<Resource<CurrentUserResponse>>
        get() = _currentUser

    private val _allProducts: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val allProducts: LiveData<Resource<ProductResponse>>
        get() = _allProducts

//    private val _productsWithLabel: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
//    val productsWithLabel: LiveData<Resource<ProductResponse>>
//        get() = _productsWithLabel

    private val _searchResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val searchResult: LiveData<Resource<ProductResponse>>
        get() = _searchResult

    private val _favlistResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val favlistResult
        get() = _favlistResult

    private val _cartItems: MutableLiveData<Resource<CartResponse>> = MutableLiveData()
    val cartItems
        get() = _cartItems

    private val _totalPrice: MutableLiveData<Float> = MutableLiveData()
    val totalPrice
        get() = _totalPrice


    private val _navigateToProductDetails = MutableLiveData<String>()
    val navigateToProductDetails
        get() = _navigateToProductDetails

    private val _cartSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val cartSnackBarMessage
        get() = _cartSnackBarMessage

    private val _favoSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val favoSnackBarMessage
        get() = _favoSnackBarMessage


    fun onProductClicked(id: String) {
        _navigateToProductDetails.value = id
    }

//    fun onProductDetailsNavigated(){
//        _navigateToProductDetails.value = null
//    }

    init {
        getAllProducts()
        //getProductsWithCustomLabel()
    }

//    fun getCurrentUser() = viewModelScope.launch {
//        val response = adoreRepository.getCurrentUser()
//        _currentUser.value = handleCurrentUserResponse(response)
//    }

    private fun getAllProducts() = viewModelScope.launch {
        safeGetAllProductsCall()
    }

    fun getSearchResult(searchQuery: String) = viewModelScope.launch {
        safeSearchForProductsCall(searchQuery)
    }

    fun getFavlist() = viewModelScope.launch {
        safeGetFavlistCall()
    }

    fun getCartItem() = viewModelScope.launch {
        safeGetCartItemsCall()
    }

    fun cartItemQuantityChanged(quantity: Int, cartItemId: String) = viewModelScope.launch {
        _cartSnackBarMessage.value =
            handleApiTransactionResponse(adoreRepository.updateCart(cartItemId, quantity))
        getCartItem()
    }

    fun removeCartItem(cartItemId: String) = viewModelScope.launch {
        _cartSnackBarMessage.value =
            handleApiTransactionResponse(adoreRepository.removeCartItem(cartItemId))
        getCartItem()
    }

    fun removeFavoItem(productId: String) = viewModelScope.launch {
        _favoSnackBarMessage.value =
            handleApiTransactionResponse(adoreRepository.removeFavoItem(productId))
        getFavlist()
    }

    fun getTotalPrice(cartItems: List<CartItem>) {
        var sum = 0F
        cartItems.forEach {
            sum += it.quantity * it.productDetails.price
        }
        _totalPrice.value = sum
    }

    fun doneShowingSnackBarInCart(){
        _cartSnackBarMessage.value=null
    }

    fun doneShowingSnackBarInFavo(){
        _favoSnackBarMessage.value=null
    }

    private suspend fun safeSearchForProductsCall(searchQuery: String){
        _searchResult.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.searchForProducts(searchQuery) //Seasonal
                _searchResult.value = handleProductResponse(response)
            }else{
                _searchResult.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _searchResult.postValue(Resource.Error("Network Failure!"))
                else -> _searchResult.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetAllProductsCall(){
        _allProducts.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getProducts() //Seasonal
                _allProducts.value = handleProductResponse(response)
            }else{
                _allProducts.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _allProducts.postValue(Resource.Error("Network Failure!"))
                else -> _allProducts.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetFavlistCall(){
        _favlistResult.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getFavlist() //Seasonal
                _favlistResult.value = handleProductResponse(response)
            }else{
                _favlistResult.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _favlistResult.postValue(Resource.Error("Network Failure!"))
                else -> _favlistResult.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetCartItemsCall(){
        _cartItems.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getCartItems() //Seasonal
                _cartItems.value = handleCartResponse(response)
            }else{
                _cartItems.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _cartItems.postValue(Resource.Error("Network Failure!"))
                else -> _cartItems.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }


    private fun handleProductResponse(response: Response<ProductResponse>): Resource<ProductResponse> =
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
            val message = "An error occurred: " + when (response.code()) {
                404 -> "404! Resource not found"
                500 -> "Server broken"
                502 -> "Bad Gateway"
                else -> "Unknown error"
            }
            Resource.Error(message)
        }


    private fun handleCartResponse(response: Response<CartResponse>): Resource<CartResponse> =
        if (response.isSuccessful) {
            when (response.code()) {
                200 -> {
                    response.body()!!.let {
                        Resource.Success(it)
                    }
                }
                else -> {
                    Log.e("Code", "${response.code()}")
                    Resource.Error("Add items to your cart!")
                }
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

}


//
//    fun setDoneShowingSnackBarWithMessageInCart(){
//        _showSnackBarWithMessageInCart?.value = null
//    }
//
//    fun setDoneShowingSnackBarWithMessageInFavo(){
//        _showSnackBarWithMessageInFavo?.value = null
//    }
//private val _showSnackBarWithMessageInCart: MutableLiveData<Boolean>? = null
//val showSnackBarWithMessageInCart
//    get() = _showSnackBarWithMessageInCart
//
//private val _showSnackBarWithMessageInFavo: MutableLiveData<Boolean>? = null
//val showSnackBarWithMessageInFavo
//    get() = _showSnackBarWithMessageInFavo