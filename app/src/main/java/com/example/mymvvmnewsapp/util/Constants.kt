package com.example.mymvvmnewsapp.util

/**
 * Holds compile-time constants that are shared across multiple layers, such as API keys,
 * base URLs, pagination size, and debounce delays.
 */
class Constants {

    companion object{
        // Replace with your own NewsAPI key for production apps
        const val API_KEY= "aadbaee2bd4f44b09ce42f4d91b4e659"
        const val BASE_URL = "https://newsapi.org"
        // Debounce interval (ms) between keystrokes before firing a search
        const val SEARCH_NEWS_TIME_DELAY = 500L

        const val QUERY_PAGE_SIZE = 20
    }
}
