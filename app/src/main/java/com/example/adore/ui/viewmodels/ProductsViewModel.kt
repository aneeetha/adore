package com.example.adore.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.adore.AdoreApplication
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

    private val _currentUser: MutableLiveData<Resource<CurrentUserResponse>> = MutableLiveData()
    val currentUser: LiveData<Resource<CurrentUserResponse>>
        get() = _currentUser

    private val _productsOfType: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val productsOfType: LiveData<Resource<ProductResponse>>
        get() = _productsOfType

    private val _productsOfCategory: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val productsOfCategory: LiveData<Resource<ProductResponse>>
        get() = _productsOfCategory

    private val _searchResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val searchResult: LiveData<Resource<ProductResponse>>
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

    private val _navigateToProductDetails = MutableLiveData<String>()
    val navigateToProductDetails
        get() = _navigateToProductDetails

    private val _cartSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val cartSnackBarMessage
        get() = _cartSnackBarMessage

    private val _favoSnackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val favoSnackBarMessage
        get() = _favoSnackBarMessage



    init {
        getCurrentUser()
    }

    private fun getCurrentUser() = viewModelScope.launch {
        val response = adoreRepository.getCurrentUser()
        _currentUser.value = handleResponse(response)
    }

    fun getAddressesOfCurrentUser(userId: Long) = adoreRepository.getAddressesOfUser(userId)

    fun getProductsOfType(gender: Gender, productType: ProductType) = viewModelScope.launch {
        safeGetProductsWithTypeCall(gender, productType)
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

    fun getOrders() = viewModelScope.launch {
        safeGetOrdersCall()
    }

    fun getProductWithCategories(gender: Gender, productType: ProductType, category: Category)= viewModelScope.launch{
        safeGetProductWithCategoriesCall(gender, productType, category)
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

    fun placeOrder(addressId: Int) = viewModelScope.launch {
        _cartSnackBarMessage.value = handleApiTransactionResponse(adoreRepository.placeOrder(addressId))
        _navigateToOrderSuccessPage.value = true
    }

    fun removeFavoItem(productId: String) = viewModelScope.launch {
        _favoSnackBarMessage.value =
            handleApiTransactionResponse(adoreRepository.removeFavoItem(productId))
        getFavlist()
    }

    fun getTotalPrice(cartItems: List<CartItem>) {
        var sum = 0F
        cartItems.forEach {
            sum += it.quantity * it.sellingPrice
        }
        _totalPrice.value = sum
    }

    fun doneShowingSnackBarInCart(){
        _cartSnackBarMessage.value=null
    }

    fun doneShowingSnackBarInFavo(){
        _favoSnackBarMessage.value=null
    }

    fun doneNavigatingToOrderSuccessPage(){
        _navigateToOrderSuccessPage.value = null
    }

    private suspend fun safeSearchForProductsCall(searchQuery: String){
        _searchResult.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.searchForProducts(searchQuery) //Seasonal
                _searchResult.value = handleResponse(response)
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

    private suspend fun safeGetProductWithCategoriesCall(gender: Gender, productType: ProductType, category: Category){
        _productsOfCategory.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getProductsOfCategory(gender, productType, category)
                _productsOfCategory.value = handleResponse(response)
            }else{
                _productsOfCategory.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _productsOfCategory.postValue(Resource.Error("Network Failure!"))
                else -> _productsOfCategory.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetProductsWithTypeCall(gender: Gender, productType: ProductType){
        _productsOfType.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getProductsOfType(gender, productType)
                _productsOfType.value = handleResponse(response)
            }else{
                _productsOfType.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _productsOfType.postValue(Resource.Error("Network Failure!"))
                else -> _productsOfType.postValue(Resource.Error("Conversion Error!"))
            }
        }
    }

    private suspend fun safeGetFavlistCall(){
        _favlistResult.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getFavlist() //Seasonal
                _favlistResult.value = handleResponse(response)
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
                _cartItems.value = handleResponse(response)
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

    private suspend fun safeGetOrdersCall(){
        _orders.value = Resource.Loading()
        try{
            if(hasInternetConnection()){
                val response = adoreRepository.getOrders()
                _orders.value = handleResponse(response)
            }else{
                _orders.value = Resource.Error("No internet connection :(")
            }
        }catch(t: Throwable){
            when(t){
                is IOException -> _orders.postValue(Resource.Error("Network Failure!"))
                else -> _orders.postValue(Resource.Error("Conversion Error!"))
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
