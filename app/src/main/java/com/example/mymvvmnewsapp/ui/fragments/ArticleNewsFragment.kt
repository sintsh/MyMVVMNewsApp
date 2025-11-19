package com.example.mymvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

import com.example.mymvvmnewsapp.R
import com.example.mymvvmnewsapp.ui.NewsActivity
import com.example.mymvvmnewsapp.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Detail screen that renders the selected [Article] inside a WebView and exposes a FAB
 * for persisting the article locally. Receives arguments via SafeArgs from any list fragment.
 */
class ArticleNewsFragment: Fragment(R.layout.fragment_article) {

    lateinit var  viewModel: NewsViewModel
    lateinit var webView: WebView

    lateinit var fab : FloatingActionButton

    // SafeArgs exposes the strongly typed Article passed from the list screens
    val args: ArticleNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.fab)
        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)

            Snackbar.make(view, "Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}