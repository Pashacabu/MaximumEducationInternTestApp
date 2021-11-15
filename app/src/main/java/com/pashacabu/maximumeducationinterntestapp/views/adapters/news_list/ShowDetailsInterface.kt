package com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list

import android.view.View
import android.widget.ImageView
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem

interface ShowDetailsInterface {

    fun showDetails(news: DataItem, view: View)
}