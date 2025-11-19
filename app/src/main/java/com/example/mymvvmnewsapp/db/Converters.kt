package com.example.mymvvmnewsapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mymvvmnewsapp.models.Source

/**
 * Room type converters used to serialize complex objects (like [Source]) into primitive
 * values that SQLite understands, and to reconstruct them when reading from disk.
 */
class Converters {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        // Persist the human-readable source name in the DB
        return source?.name
    }

    @TypeConverter
    fun toSource(name: String?): Source? {
        // Rehydrate Source objects when reading from Room
        return name?.let { Source(it, it) }
    }
}