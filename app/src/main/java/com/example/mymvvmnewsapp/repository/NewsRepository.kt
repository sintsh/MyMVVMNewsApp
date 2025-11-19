package com.example.mymvvmnewsapp.repository

import androidx.room.Query
import com.example.mymvvmnewsapp.api.RetrofitInstance
import com.example.mymvvmnewsapp.db.ArticleDatabase
import com.example.mymvvmnewsapp.models.Article

/**
 * Repository layer that abstracts both remote (Retrofit) and local (Room) data sources.
 * Fragments/ViewModels talk to this class so swapping implementations or adding caching
 * in the future requires minimal changes outside this file.
 */
class NewsRepository(
    val db: ArticleDatabase
) {

    // Remote data ------------------------------------------------------------
    suspend fun getBreakingNews(countryCode: String, pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)


    // Local persistence ------------------------------------------------------
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)
}