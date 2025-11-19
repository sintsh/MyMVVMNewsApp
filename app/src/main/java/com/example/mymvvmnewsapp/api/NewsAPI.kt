package com.example.mymvvmnewsapp.api

import android.R

import com.example.mymvvmnewsapp.models.NewsResponse
import com.example.mymvvmnewsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query

/**
 * Retrofit definition of the NewsAPI.org REST endpoints leveraged by the app.
 * Provides both the top-headlines and keyword search operations with paging support.
 */
interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String ="us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}