package com.example.spirelab_pankajverma.data.retrofit

import com.example.spirelab_pankajverma.data.item.ProductDataItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductRetrofitServices {
    @GET("products")
    suspend fun getProducts(): ProductDataItem

    @GET("products/search")
    suspend fun getSearchProduct(@Query("q") query: String): ProductDataItem
}