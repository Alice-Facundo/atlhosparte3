package com.example.athlos.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FatSecretApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://your-fatsecret-api-base-url.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: FatSecretApiService = retrofit.create(FatSecretApiService::class.java)
}
