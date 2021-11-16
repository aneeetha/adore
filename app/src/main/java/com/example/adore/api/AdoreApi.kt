package com.example.adore.api

import com.example.adore.models.Product
import com.example.adore.models.ProductResponse
import com.example.adore.models.SuccessResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
//    @GET("api/products/:id")
//    suspend fun getProductWithId(
//        @Query("id")
//        id:String
//    ):Response<Product>
}