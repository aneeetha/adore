package com.example.adore.api

import com.example.adore.models.responses.*
import retrofit2.Response
import retrofit2.http.*

interface AdoreApi {

    @GET("api/user")
    suspend fun getCurrentUser():Response<CurrentUserResponse>

    @POST("api/user/{id}")
    suspend fun setCurrentUser(@Path("id")id: Long):Response<ApiTransactionResponse>

    @GET("api/products")
    suspend fun getProducts():Response<ProductResponse>

    @GET("api/search")
    suspend fun searchForProducts(
        @Query("q")
        searchQuery:String
    ):Response<ProductResponse>

    @GET("/api/favo")
    suspend fun getFavlist(
        @Query("userId")
        userId: Long
    ):Response<ProductResponse>

    @POST("/api/favo/")
    suspend fun addProductToFav(
        @Query("id")
        id: String,
        @Query("userId")
        userId: Long
    ):Response<ApiTransactionResponse>

    @DELETE("api/favo/{id}")
    suspend fun removeFavoItem(
        @Path("id")id: String,
        @Query("userId")
        userId: Long
    ): Response<ApiTransactionResponse>

    @GET("api/cart")
    suspend fun getCartItems(
        @Query("userId")
        userId: Long): Response<CartResponse>

    @GET("api/orders")
    suspend fun getOrders(
        @Query("userId")
        userId: Long): Response<OrderResponse>

    @POST("api/placeorder")
    suspend fun placeOrder(
        @Query("addressId")
        addressId: Int,
        @Query("userId")
        userId: Long
    ): Response<ApiTransactionResponse>

    @POST("api/cart")
    suspend fun addItemToCart(
        @Query("sku")
        productId: String,
        @Query("size")
        size: String,
        @Query("quantity")
        quantity: Int,
        @Query("discount")
        discount: Int,
        @Query("userId")
        userId: Long
    ):Response<ApiTransactionResponse>

    @PATCH("api/cart/{id}")
    suspend fun updateQuantityInCart(
        @Path("id") id: String,
        @Query("quantity")quantity: Int,
        @Query("userId")
        userId: Long
    ): Response<ApiTransactionResponse>

    @DELETE("api/cart/{id}")
    suspend fun removeCartItem(
        @Path("id")id: String,
        @Query("userId")
        userId: Long
    ): Response<ApiTransactionResponse>

    @GET("api/products/customlabels/{id}")
    suspend fun getProductsWithLabel(
        @Path("id")
        labelId: String
    ): Response<ProductResponse>

    @GET("api/filter/sub")
    suspend fun getProductsOfCategory(
        @Query("gender")
        gender: String,
        @Query("productType")
        productType: String,
        @Query("category")
        category: String
    ): Response<ProductResponse>

    @GET("api/filter/main")
    suspend fun getProductsOfType(
        @Query("gender")
        gender: String,
        @Query("productType")
        productType: String
    ): Response<ProductResponse>
}
