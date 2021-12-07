package com.example.adore.repository

import android.content.Context
import com.example.adore.api.RetrofitInstance
import com.example.adore.databsae.AdoreDatabase
import com.example.adore.databsae.SessionManager
import com.example.adore.models.entities.Address
import com.example.adore.models.entities.AddressDetailUpdate
import com.example.adore.models.entities.User
import com.example.adore.models.entities.UserDetailUpdate
import com.example.adore.models.enums.Category
import com.example.adore.models.enums.Gender
import com.example.adore.models.enums.ProductType

class AdoreRepository(
    private val db: AdoreDatabase,
) {
    //SessionManager(context).getUserDetailsFromSession()[SessionManager.KEY_USER_ID] as Long

    //suspend fun setCurrentUser(userId: Long) = RetrofitInstance.api.setCurrentUser(userId)

    //suspend fun getCurrentUser() = RetrofitInstance.api.getCurrentUser()

    suspend fun getProducts() = RetrofitInstance.api.getProducts()

    suspend fun getProductsWithLabel(labelId: String) = RetrofitInstance.api.getProductsWithLabel(labelId)

    suspend fun getProductsOfCategory(gender: Gender, productType: ProductType, category: Category) = RetrofitInstance.api.getProductsOfCategory(gender.name, productType.name, category.name)

    suspend fun getProductsOfType(gender: Gender, productType: ProductType) = RetrofitInstance.api.getProductsOfType(gender.name, productType.name)

    suspend fun searchForProducts(searchQuery: String) = RetrofitInstance.api.searchForProducts(searchQuery)

    suspend fun getFavlist(userId: Long) = RetrofitInstance.api.getFavlist(userId)

    suspend fun addProductToFav(id: String, userId: Long) = RetrofitInstance.api.addProductToFav(id, userId)

    suspend fun removeFavoItem(productId: String, userId: Long) = RetrofitInstance.api.removeFavoItem(productId, userId)

    suspend fun getCartItems(userId: Long) = RetrofitInstance.api.getCartItems(userId)

    suspend fun placeOrder(addressId: Int, userId: Long) = RetrofitInstance.api.placeOrder(addressId, userId)

    suspend fun getOrders(userId: Long) = RetrofitInstance.api.getOrders(userId)

    suspend fun addItemToCart(id: String, size: String, quantity: Int, discount: Int, userId: Long) = RetrofitInstance.api.addItemToCart(id, size, quantity, discount, userId)

    suspend fun updateCart(cartItemId: String, quantity:Int, userId: Long) = RetrofitInstance.api.updateQuantityInCart(cartItemId, quantity, userId)

    suspend fun removeCartItem(cartItemId: String, userId: Long) = RetrofitInstance.api.removeCartItem(cartItemId, userId)

    suspend fun addNewUser(user: User) = db.getUserDao().insertUser(user)

    suspend fun deleteAllUsers() = db.getUserDao().deleteAll()

    suspend fun addNewAddressToUser(address: Address) = db.getUserDao().insertAddress(address)

    suspend fun deleteAddress(address: Address) = db.getUserDao().deleteAddress(address)

    fun getLastInsertedAddress() = db.getUserDao().getLastInsertedAddress()

    fun getUser(userId: Long) = db.getUserDao().getUser(userId)

    suspend fun getUserWithMobileNo(mobileNo: String) = db.getUserDao().getUserWithMobileNo(mobileNo)

    fun getAllUsers() = db.getUserDao().getAllUsers()

    suspend fun updateUserDetails(userDetail: UserDetailUpdate) = db.getUserDao().updateUserDetails(userDetail)

    suspend fun updateAddress(addressDetail: AddressDetailUpdate) = db.getUserDao().updateAddress(addressDetail)

    fun getAddressesOfUser(userId: Long) = db.getUserDao().getUserWithAddresses(userId)

//    suspend fun insertNewUser(favo: Favo) = db.getFavoDao().insert(favo)
//
//    suspend fun getFavlist(userId:String) = db.getFavoDao().getFavlist(userId)
//
//    suspend fun updateFavlist(userId: String, favlist:List<String>) = db.getFavoDao().update(userId, favlist)
}
