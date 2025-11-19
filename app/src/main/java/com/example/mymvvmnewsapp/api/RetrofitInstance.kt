package com.example.mymvvmnewsapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.example.mymvvmnewsapp.util.Constants.Companion.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Lazily creates the single Retrofit client used across the app, wiring in logging,
 * the NewsAPI base URL, and the GSON converter. Provides a typed [NewsAPI] service.
 */
class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            // Log every request/response to aid debugging during development
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor (logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}