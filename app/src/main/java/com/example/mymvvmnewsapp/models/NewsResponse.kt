package com.example.mymvvmnewsapp.models

/**
 * GSON-mapped wrapper returned by NewsAPI.org when requesting headlines or searches.
 * Contains a mutable list so pagination can append additional pages inside the ViewModel.
 */
data class NewsResponse(
    val articles: MutableList<Article>?,
    val status: String?,
    val totalResults: Int?
)