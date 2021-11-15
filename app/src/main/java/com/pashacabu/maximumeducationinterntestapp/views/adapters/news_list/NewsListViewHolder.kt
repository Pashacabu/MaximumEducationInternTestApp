package com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pashacabu.maximumeducationinterntestapp.R
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem

class NewsListViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {

    val image: ImageView = item.findViewById(R.id.newsImage)
    private val title: TextView = item.findViewById(R.id.newsTitle)

    fun bindData(news: DataItem?) {

        title.text = news?.title ?: Activity().getText(R.string.no_title)

        Glide.with(item)
            .load(news?.image)
            .placeholder(R.drawable.news_placeholder)
            .into(image)

    }


}