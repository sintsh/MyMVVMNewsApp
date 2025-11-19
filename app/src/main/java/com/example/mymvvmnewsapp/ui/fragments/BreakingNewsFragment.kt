package com.example.mymvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymvvmnewsapp.R
import com.example.mymvvmnewsapp.adapters.NewsAdapter
import com.example.mymvvmnewsapp.ui.NewsActivity
import com.example.mymvvmnewsapp.ui.NewsViewModel
import com.example.mymvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mymvvmnewsapp.util.Resource

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {
    // Shared ViewModel retrieved from host activity
    lateinit var  viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter

    private lateinit var rvBreakingNews: RecyclerView

    private lateinit var paginationProgressBar: ProgressBar


    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        rvBreakingNews = view.findViewById(R.id.rvBreakingNews)

        paginationProgressBar = view.findViewById(R.id.paginationProgressBar)

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {


            val bundle = Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleNewsFragment,
                bundle
            )
        }


        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        // submitList clones the array, so call toList() preserves pagination diffing
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())
                        val totalPages = newsResponse.totalResults?.div(QUERY_PAGE_SIZE)?.plus(2)
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let { message->
                        //Log.e(TAG, "An error occured: $message")
                        Toast.makeText(activity,"An error occured: $message",Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading-> {
                    showProgressBar()
                }
            }



        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading =true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount>=totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                // Trigger ViewModel pagination when the user reaches the end
                viewModel.getBreakingNews("us")
                isScrolling = false
            }



        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            // Tie the endless-scroll listener to this RecyclerView instance
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}