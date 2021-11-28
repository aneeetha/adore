package com.example.adore.repository

import com.example.adore.api.RetrofitInstance
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.models.Favo
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.entities.User
import com.example.adore.models.entities.UserDetailUpdate

class AdoreRepository(
    private val db: AdoreDatabase
) {
    suspend fun setCurrentUser(userId: Int) = RetrofitInstance.api.setCurrentUser(userId)

    suspend fun getCurrentUser() = RetrofitInstance.api.getCurrentUser()

    suspend fun getProducts() = RetrofitInstance.api.getProducts()

    suspend fun getProductsWithLabel(labelId: String) = RetrofitInstance.api.getProductsWithLabel(labelId)

    suspend fun searchForProducts(searchQuery: String) = RetrofitInstance.api.searchForProducts(searchQuery)

    suspend fun getFavlist() = RetrofitInstance.api.getFavlist()

    suspend fun addProductToFav(id: String) = RetrofitInstance.api.addProductToFav(id)

    suspend fun removeFavoItem(productId: String) = RetrofitInstance.api.removeFavoItem(productId)

    suspend fun getCartItems() = RetrofitInstance.api.getCartItems()

    suspend fun addItemToCart(id: String, size: String, quantity: Int) = RetrofitInstance.api.addItemToCart(id, size, quantity)

    suspend fun updateCart(cartItemId: String, quantity:Int) = RetrofitInstance.api.updateQuantityInCart(cartItemId, quantity)

    suspend fun removeCartItem(cartItemId: String) = RetrofitInstance.api.removeCartItem(cartItemId)

    suspend fun addNewUser(user: User) = db.getUserDao().insertUser(user)

    suspend fun deleteAllUsers() = db.getUserDao().deleteAll()

    suspend fun addNewAddressToUser(address: Address) = db.getUserDao().insertAddress(address)

    fun getLastInsertedAddress() = db.getUserDao().getLastInsertedAddress()

    fun getUser(userId: Int) = db.getUserDao().getUser(userId)

    suspend fun getUserWithMobileNo(mobileNo: String) = db.getUserDao().getUserWithMobileNo(mobileNo)

    fun getAllUsers() = db.getUserDao().getAllUsers()

    suspend fun updateUserDetails(userDetail: UserDetailUpdate) = db.getUserDao().updateUserDetails(userDetail)

    suspend fun updateAddress(addressDetail: AddressDetailUpdate) = db.getUserDao().updateAddress(addressDetail)

    fun getAddressesOfUser(userId: Int) = db.getUserDao().getUserWithAddresses(userId)

//    suspend fun insertNewUser(favo: Favo) = db.getFavoDao().insert(favo)
//
//    suspend fun getFavlist(userId:String) = db.getFavoDao().getFavlist(userId)
//
//    suspend fun updateFavlist(userId: String, favlist:List<String>) = db.getFavoDao().update(userId, favlist)
}
