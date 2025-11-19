package com.example.mymvvmnewsapp.models

import java.io.Serializable

/**
 * Simple serializable model representing the originating outlet of an article.
 * Serialized in Room via the [Converters] helper.
 */
data class Source(
    val id: String?,
    val name: String?
): Serializable