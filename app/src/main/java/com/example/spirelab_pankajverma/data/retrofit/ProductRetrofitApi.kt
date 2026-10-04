package com.example.spirelab_pankajverma.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductRetrofitApi {
//    https://dummyjson.com/
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productService = retrofit.create(ProductRetrofitServices::class.java)!!
}