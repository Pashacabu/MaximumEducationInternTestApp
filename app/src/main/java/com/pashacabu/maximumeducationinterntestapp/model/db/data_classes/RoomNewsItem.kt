package com.pashacabu.maximumeducationinterntestapp.model.db.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomNewsItem(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var image: String? = null,
    var country: String? = null,
    var author: String? = null,
    var description: String? = null,
    var language: String? = null,
    var source: String? = null,
    var title: String? = null,
    var category: String? = null,
    var published_at: String? = null,
    var url: String = "null"
)
