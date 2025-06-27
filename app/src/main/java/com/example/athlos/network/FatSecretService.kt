package com.example.athlos.api

import com.example.athlos.ui.models.FoodsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FatSecretApiService {
    @GET("search/foods")
    suspend fun searchFoods(@Query("query") query: String): FoodsResponse
}
