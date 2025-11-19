package com.example.mymvvmnewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymvvmnewsapp.NewsApplication
import com.example.mymvvmnewsapp.repository.NewsRepository

class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Inject both the Application context and repository into NewsViewModel
        return NewsViewModel(app,newsRepository ) as T
    }
}