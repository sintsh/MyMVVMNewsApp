package com.example.mymvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymvvmnewsapp.R
import com.example.mymvvmnewsapp.models.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    // Simple holder that exposes the inflated row views
    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            // Use the article URL as a stable identifier
            return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean {
            return oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ArticleViewHolder,
        position: Int
    ) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            // Look up row views manually because we are not using ViewBinding here
            val ivArticleImage: ImageView = findViewById(R.id.ivArticleImage)
            val tvSource: TextView = findViewById(R.id.tvSource)
            val tvTitle: TextView = findViewById(R.id.tvTitle)
            val tvDescription: TextView = findViewById(R.id.tvDescription)
            val tvPublishedAt: TextView = findViewById(R.id.tvPublishedAt)

            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name ?: "Unknown Source"
            tvTitle.text = article.title ?: ""
            tvDescription.text = article.description ?: ""
            tvPublishedAt.text = article.publishedAt ?: ""
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener:((Article)->Unit)?= null

    fun setOnItemClickListener(listener: (Article)-> Unit){
        // Fragments register callbacks to navigate when an item is tapped
        onItemClickListener = listener
    }

}