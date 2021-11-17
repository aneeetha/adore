package com.example.adore.api

import com.example.adore.models.enums.DressSize
import com.example.adore.models.responses.CartResponse
import com.example.adore.models.responses.ProductResponse
import com.example.adore.models.responses.SuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface AdoreApi {

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
    ):Response<SuccessResponse>

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
    ):Response<SuccessResponse>

    @PATCH("api/cart/{id}")
    suspend fun updateQuantityInCart(
        @Path("id") id: String,
        @Query("quantity")quantity: Int
    ): Response<SuccessResponse>
}