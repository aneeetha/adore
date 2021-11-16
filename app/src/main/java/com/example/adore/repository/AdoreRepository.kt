package com.example.adore.repository

import com.example.adore.api.RetrofitInstance
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.Favo

class AdoreRepository(
    private val db: AdoreDatabase
) {
    suspend fun getProducts() = RetrofitInstance.api.getProducts()

    suspend fun searchForProducts(searchQuery: String) = RetrofitInstance.api.searchForProducts(searchQuery)

    suspend fun getFavlist() = RetrofitInstance.api.getFavlist()

    suspend fun addProductToFav(id: String) = RetrofitInstance.api.addProductToFav(id)

//    suspend fun insertNewUser(favo: Favo) = db.getFavoDao().insert(favo)
//
//    suspend fun getFavlist(userId:String) = db.getFavoDao().getFavlist(userId)
//
//    suspend fun updateFavlist(userId: String, favlist:List<String>) = db.getFavoDao().update(userId, favlist)
}
