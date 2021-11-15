package com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list

import androidx.recyclerview.widget.DiffUtil
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem

class NewsDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.image == newItem.image &&
                oldItem.description == newItem.description
    }

}