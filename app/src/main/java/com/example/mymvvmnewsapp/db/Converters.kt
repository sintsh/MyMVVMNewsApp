package com.example.mymvvmnewsapp.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mymvvmnewsapp.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return source?.name
    }

    @TypeConverter
    fun toSource(name: String?): Source? {
        return name?.let { Source(it, it) }
    }
}