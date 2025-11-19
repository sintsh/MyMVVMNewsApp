package com.example.mymvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymvvmnewsapp.models.Article

/**
 * Data-access layer for the local `articles` table. Exposes CRUD operations used by the
 * ViewModel to persist users' saved stories and observe updates reactively.
 */
@Dao
interface ArticleDao {

    // Insert or update an article; replaces duplicates by URL/id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("Select * from articles")
    fun getAllArticles(): LiveData<List<Article>>


    @Delete
    suspend fun deleteArticle(article: Article)

}