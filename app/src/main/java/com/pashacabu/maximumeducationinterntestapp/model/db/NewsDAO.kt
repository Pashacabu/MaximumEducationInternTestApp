package com.pashacabu.maximumeducationinterntestapp.model.db

import androidx.room.*
import com.pashacabu.maximumeducationinterntestapp.model.db.data_classes.RoomNewsItem
import retrofit2.http.GET

@Dao
interface NewsDAO {

    @Insert(entity = RoomNewsItem::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news : List<RoomNewsItem>)

    @Query("SELECT * FROM RoomNewsItem")
    suspend fun getNews() : List<RoomNewsItem>

    @Query("SELECT * FROM RoomNewsItem WHERE url= :url")
    suspend fun getNewsDetails(
        url : String
    ) : RoomNewsItem

    @Query("DELETE FROM RoomNewsItem")
    suspend fun deleteAll()
}