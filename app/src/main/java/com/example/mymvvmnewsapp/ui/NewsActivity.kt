package com.example.mymvvmnewsapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mymvvmnewsapp.R
import com.example.mymvvmnewsapp.db.ArticleDatabase
import com.example.mymvvmnewsapp.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

    // Shared ViewModel instance that fragments retrieve from this activity
    lateinit var  viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)

        // Build the single repository/data layer used throughout the app
        val newsRepository = NewsRepository(ArticleDatabase(this))

        // Provide Application context + repository to the ViewModel factory
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)

        // Lazily create the ViewModel so child fragments can share the same instance
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // 1. Find the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment // Use the ID of your NavHostFragment from the XML

        // 2. Find the BottomNavigationView using its ID from the XML
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // 3. Set up the NavController
        bottomNavigationView.setupWithNavController(navHostFragment.navController)



    }
}