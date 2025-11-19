package com.example.mymvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.mymvvmnewsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var instance: ArticleDatabase? =null
        private val LOCK = Any()


        // Ensure only one Room database instance lives inside the process
        operator fun invoke(context: Context)= instance?:synchronized(LOCK){
            instance ?: createDatabase(context).also{instance=it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}