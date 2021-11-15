package com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list

import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.pashacabu.maximumeducationinterntestapp.R
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem

class NewsListAdapter(private val newsDetailsInterface: ShowDetailsInterface) :
    ListAdapter<DataItem, NewsListViewHolder>(NewsDiffCallback()) {
    private var loading = false

    val openLoading: Boolean get() = loading

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val orientation = parent.context.resources.configuration.orientation
        val displayHeight = parent.context.resources.displayMetrics.heightPixels
        val container =
            LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false)
        val layoutParams = container.layoutParams
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                layoutParams.height = (displayHeight * 0.15).toInt()
            }
            else -> {
                layoutParams.height = (displayHeight * 0.3).toInt()
            }
        }
        container.layoutParams = layoutParams
        return NewsListViewHolder(container)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val news = getItem(position)
        holder.bindData(news)
        holder.image.transitionName = news.url
        holder.itemView.setOnClickListener {
            Log.d("Adapter", "host = ${it.transitionName}")
            Log.d("Adapter", "image = ${holder.image.transitionName}")
            newsDetailsInterface.showDetails(
                news,
                holder.image
            )
        }
    }

    fun setLoading(isLoading: Boolean) {
        loading = isLoading
    }
}

