package com.example.adore.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.models.ProductResponse
import com.example.adore.repository.ProductsRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductsViewModel(
    private val productsRepository: ProductsRepository
): ViewModel() {

    private val _allProducts: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val allProducts: LiveData<Resource<ProductResponse>>
        get() = _allProducts

    private val _searchResult: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    val searchResult: LiveData<Resource<ProductResponse>>
        get() = _searchResult

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
        val response = productsRepository.getProducts()
        _allProducts.value = handleProductResponse(response)
    }

    fun getSearchResult(searchQuery: String) =  viewModelScope.launch {
        _searchResult.value = Resource.Loading()
        val response = productsRepository.searchForProducts(searchQuery)
        _searchResult.value = handleProductResponse(response)
    }

    private fun handleProductResponse(response: Response<ProductResponse>): Resource<ProductResponse>{
        if(response.isSuccessful){
            response.body()!!.let { response ->
                 return Resource.Success(response)
            }
        }else{
           return Resource.Error(response.message())
        }
    }
}

