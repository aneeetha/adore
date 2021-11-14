package com.example.adore.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.adore.databsae.ProductDatabaseDao
import com.example.adore.models.DressSize
import com.example.adore.models.Product

class ProductDetailsViewModel(val product: Product, dataSource: ProductDatabaseDao): ViewModel() {
    private var chosenSize: DressSize? = null

    fun setChosenProductSize(size: DressSize){
        chosenSize = size
    }

    private var _navigateToCart = MutableLiveData<Boolean>()
    val navigateToCart
        get() = _navigateToCart

    private var _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar
        get() = _showSnackBar


    fun onAddToCartClicked(){
        chosenSize?.let {
            _navigateToCart.value = true
        }?:setShowSnackBar()
    }

    fun setNavigatedToCart(){
        _navigateToCart.value = false
    }

    private fun setShowSnackBar(){
            _showSnackBar.value = true
    }

    fun doneShowingSnackBar(){
        _showSnackBar.value = false
    }


}