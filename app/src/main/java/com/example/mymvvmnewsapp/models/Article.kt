package com.example.mymvvmnewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymvvmnewsapp.models.Source
import java.io.Serializable

/**
 * Room entity + API DTO that represents a news article. Acts as the single source
 * of truth shared between Retrofit responses, the database, and UI adapters.
 */
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable