package com.example.adore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.models.CartItem
import com.example.adore.models.responses.CartResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductsViewModel(
    private val adoreRepository: AdoreRepository
): ViewModel() {

    private val _allProducts: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val allProducts: LiveData<Resource<ProductResponse>>
        get() = _allProducts

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

    fun onProductClicked(id: String){
        _navigateToProductDetails.value = id
    }

//    fun onProductDetailsNavigated(){
//        _navigateToProductDetails.value = null
//    }

    init {
        getAllProducts()
    }

    private fun getAllProducts() = viewModelScope.launch {
        _allProducts.value = Resource.Loading()
        val response = adoreRepository.getProducts()
        _allProducts.value = handleProductResponse(response)
    }

    fun getSearchResult(searchQuery: String) =  viewModelScope.launch {
        _searchResult.value = Resource.Loading()
        val response = adoreRepository.searchForProducts(searchQuery)
        _searchResult.value = handleProductResponse(response)
    }

    fun getFavlist() = viewModelScope.launch {
        _favlistResult.value = Resource.Loading()
        val response = adoreRepository.getFavlist()
        Log.i("FavoFragment", response.message())
        _favlistResult.value = handleProductResponse(response)
    }

    fun getCartItem() = viewModelScope.launch {
        _cartItems.value = Resource.Loading()
        val response = adoreRepository.getCartItems()
        Log.i("FavoFragment", response.message())
        _cartItems.value = handleCartResponse(response)
    }

    fun cartItemQuantityChanged(quantity: Int, cartItemId: String) = viewModelScope.launch {
        adoreRepository.updateCart(cartItemId, quantity)
        getCartItem()
    }

    fun removeCartItem(cartItemId: String) = viewModelScope.launch {
        adoreRepository.removeCartItem(cartItemId)
        getCartItem()
    }

    fun removeFavoItem(productId: String) = viewModelScope.launch {
        adoreRepository.removeFavoItem(productId)
        //getCartItem()
    }

    fun getTotalPrice(cartItems: List<CartItem>){
        var sum = 0F
        cartItems.forEach {
            sum += it.quantity * it.productDetails.price
        }
        _totalPrice.value = sum
    }


    private fun handleProductResponse(response: Response<ProductResponse>): Resource<ProductResponse>{
        if(response.isSuccessful){
            response.body()!!.let {
                 return Resource.Success(it)
            }
        }else{
           return Resource.Error(response.message())
        }
    }

    private fun handleCartResponse(response: Response<CartResponse>): Resource<CartResponse>{
        if(response.isSuccessful){
            response.body()!!.let {
                return Resource.Success(it)
            }
        }else{
            return Resource.Error(response.message())
        }
    }
}

