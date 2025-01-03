package com.example.internassignment.network

import com.example.internassignment.model.ApiResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://api.jsonbin.io/v3/b/"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface ItemsApiService {
    @GET("6773a474ad19ca34f8e37d1a")
    suspend fun getItems(): ApiResponse
}

object ItemsApi {
    val retrofitService: ItemsApiService by lazy {
        retrofit.create(ItemsApiService::class.java)
    }
}