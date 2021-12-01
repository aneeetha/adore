package com.example.adore.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.adore.models.enums.Category
import com.example.adore.models.enums.Gender
import com.example.adore.models.enums.ProductType
import com.example.adore.repository.AdoreRepository

class SharedCategoryViewModel(app: Application, repository: AdoreRepository): AndroidViewModel(app) {
    var currentProductType: ProductType? =null
    var currentGender: Gender? = null

    private val _navigateToCategoryFragment = MutableLiveData<Boolean?>()
    val navigateToCategoryFragment
        get() = _navigateToCategoryFragment

    fun navigateToCategoryListFragment(productType: ProductType, gender: Gender){
        currentProductType = productType
        currentGender = gender
        _navigateToCategoryFragment.value = true
    }

    fun doneNavigatingToCategoryListFragment(){
        currentProductType = null
        currentGender = null
        _navigateToCategoryFragment.value = null
    }

}