package com.pashacabu.maximumeducationinterntestapp.model.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.pashacabu.maximumeducationinterntestapp.model.db.data_classes.RoomNewsItem

@Database(
   version = 1,
   entities = [RoomNewsItem::class]
)
abstract class MEDatabase : RoomDatabase() {

   abstract fun newsDAO() : NewsDAO

   companion object {
      const val MEDB_NAME = "MaximumEducationDB"
      fun createMEDB(context: Context) : MEDatabase{
         return Room.databaseBuilder(
            context,
            MEDatabase::class.java,
            MEDB_NAME
         ).build()
      }
   }


}