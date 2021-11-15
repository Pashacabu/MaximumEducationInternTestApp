package com.pashacabu.maximumeducationinterntestapp.model.db

import com.pashacabu.maximumeducationinterntestapp.model.db.data_classes.RoomNewsItem
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem
import javax.inject.Inject

class DBHelper @Inject constructor(
    private val db: MEDatabase,
    private val converter: NewsConverter
) {

    suspend fun saveNews(news : MutableList<DataItem?>?){
        if (!news.isNullOrEmpty()){
            db.newsDAO().insertNews(converter.dataItemListToRoomItemList(news))
        }

    }

    suspend fun getNews(): MutableList<DataItem?>{
        return converter.roomItemListToDataItemList(db.newsDAO().getNews()) as MutableList<DataItem?>
    }

    suspend fun deleteAll(){
        db.newsDAO().deleteAll()
    }

    suspend fun getNewsDetails(url : String) : DataItem{
        return converter.roomItemToDataItem(db.newsDAO().getNewsDetails(url))
    }

}

class NewsConverter @Inject constructor(){
    fun dataItemListToRoomItemList(list : List<DataItem?>?) : List<RoomNewsItem>{
        val output : MutableList<RoomNewsItem> = mutableListOf()
        list?.forEach {
            output.add(dataItemToRoomItem(it))
        }
        return output
    }

    fun roomItemListToDataItemList(list : List<RoomNewsItem>) : List<DataItem>{
        val output : MutableList<DataItem> = mutableListOf()
        list.forEach {
            output.add(roomItemToDataItem(it))
        }
        return output
    }


    private fun dataItemToRoomItem(item : DataItem?) : RoomNewsItem{
        val out = RoomNewsItem()
        out.author = item?.author
        out.category = item?.category
        out.country = item?.country
        out.description = item?.description
        out.image = item?.image
        out.language = item?.language
        out.published_at = item?.published_at
        out.source = item?.source
        out.title = item?.title
        out.url = item?.url ?: "null"
        return out
    }

    fun roomItemToDataItem(item : RoomNewsItem) : DataItem{
        val out = DataItem()
        out.author = item.author
        out.category = item.category
        out.country = item.country
        out.description = item.description
        out.image = item.image
        out.language = item.language
        out.published_at = item.published_at
        out.source = item.source
        out.title = item.title
        out.url = item.url
        return out
    }

}