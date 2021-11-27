package com.example.adore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adore.models.enums.DressSize
import com.example.adore.models.entities.Product
import com.example.adore.models.responses.ApiTransactionResponse
import com.example.adore.repository.AdoreRepository
import com.example.adore.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductDetailsViewModel(val product: Product, private val repository: AdoreRepository) :
    ViewModel() {


    private var chosenSize: DressSize? = null
    fun setChosenProductSize(size: DressSize) {
        chosenSize = size
        _addToCartClicked.value = false

    }


    private var _addToCartClicked = MutableLiveData<Boolean?>()
    val addToCartClicked
        get() = _addToCartClicked

    private var _goBackPressed = MutableLiveData<Boolean>()
    val goBackPressed
        get() = _goBackPressed

    private var _navigateToFavoFragment = MutableLiveData<Boolean>()
    val navigateToFavoFragment
        get() = _navigateToFavoFragment

    private var _addedToFavlist = MutableLiveData<Boolean?>()
    val addedToFavlist
        get() = _addedToFavlist

    private var _navigateToCartFragment = MutableLiveData<Boolean>()
    val navigateToCartFragment
        get() = _navigateToCartFragment

    private var _navigateToSearchFragment = MutableLiveData<Boolean>()
    val navigateToSearchFragment
        get() = _navigateToSearchFragment

    private var _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar
        get() = _showSnackBar

    private var _snackBarMessage = MutableLiveData<Resource<ApiTransactionResponse>?>()
    val snackBarMessage:LiveData<Resource<ApiTransactionResponse>?>
        get() = _snackBarMessage


//    private val _showSnackBarWithMessage = MutableLiveData<Boolean>()
//    val showSnackBarWithMessage
//        get() = _showSnackBarWithMessage


    fun addToFavlistClicked() {
        viewModelScope.launch {
            _addedToFavlist.value = true
            _snackBarMessage.value = handleApiTransactionResponse(repository.addProductToFav(product._id))
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
                            1
                        )
                    )
                }
            }
        } ?: setShowSnackBar()
    }

    fun doneShowingSnackBarWithMessage(){
        _snackBarMessage.value = null
    }

    fun doneAddingItemToFavo(){
        addedToFavlist.value = null
    }

    fun goBackAction() {
        _goBackPressed.value = true
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
        _showSnackBar.value = false
    }


    private fun handleApiTransactionResponse(response: Response<ApiTransactionResponse>): Resource<ApiTransactionResponse> =
        if (response.isSuccessful) {
            response.body()!!.let {
                Resource.Success(it)
            }
        } else {
            val message = "Error: " + when(response.code()){
                404 -> "Not found"
                500 -> "Server broken"
                502 -> "Bad Gateway"
                else -> "Unknown error"
            }
            Resource.Error(message)
        }

}