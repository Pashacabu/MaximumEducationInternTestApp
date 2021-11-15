package com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreListener(private val callback: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val adapter = recyclerView.adapter as NewsListAdapter
        val manager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisible = manager.findLastVisibleItemPosition()
        val totalItems = adapter.itemCount
        if(dy>0 && lastVisible>=totalItems-5 && !adapter.openLoading){
            callback()
        }
    }
}