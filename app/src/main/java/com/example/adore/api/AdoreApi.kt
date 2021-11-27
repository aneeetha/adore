package com.example.adore.api

import com.example.adore.models.responses.CartResponse
import com.example.adore.models.responses.CurrentUserResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.models.responses.ApiTransactionResponse
import retrofit2.Response
import retrofit2.http.*

interface AdoreApi {

    @GET("api/user")
    suspend fun getCurrentUser():Response<CurrentUserResponse>

    @POST("api/user/{id}")
    suspend fun setCurrentUser(@Path("id")id: Int):Response<ApiTransactionResponse>

    @GET("api/products")
    suspend fun getProducts():Response<ProductResponse>

    @GET("api/search")
    suspend fun searchForProducts(
        @Query("q")
        searchQuery:String
    ):Response<ProductResponse>

    @GET("/api/favo")
    suspend fun getFavlist():Response<ProductResponse>

    @POST("/api/favo/")
    suspend fun addProductToFav(
        @Query("id")
        id: String
    ):Response<ApiTransactionResponse>

    @DELETE("api/favo/{id}")
    suspend fun removeFavoItem(
        @Path("id")id: String
    ): Response<ApiTransactionResponse>

    @GET("api/cart")
    suspend fun getCartItems(): Response<CartResponse>

    @POST("api/cart")
    suspend fun addItemToCart(
        @Query("sku")
        productId: String,
        @Query("size")
        size: String,
        @Query("quantity")
        quantity: Int
    ):Response<ApiTransactionResponse>

    @PATCH("api/cart/{id}")
    suspend fun updateQuantityInCart(
        @Path("id") id: String,
        @Query("quantity")quantity: Int
    ): Response<ApiTransactionResponse>

    @DELETE("api/cart/{id}")
    suspend fun removeCartItem(
        @Path("id")id: String
    ): Response<ApiTransactionResponse>

    @GET("api/products/customlabels/{id}")
    suspend fun getProductsWithLabel(
        @Path("id")
        labelId: String): Response<ProductResponse>
}