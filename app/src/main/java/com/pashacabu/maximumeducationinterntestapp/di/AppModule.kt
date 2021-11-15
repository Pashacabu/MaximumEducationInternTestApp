package com.pashacabu.maximumeducationinterntestapp.di

import android.content.Context
import com.pashacabu.maximumeducationinterntestapp.model.db.DBHelper
import com.pashacabu.maximumeducationinterntestapp.model.db.MEDatabase
import com.pashacabu.maximumeducationinterntestapp.model.db.NewsConverter
import com.pashacabu.maximumeducationinterntestapp.model.network.ConnectionChecker
import com.pashacabu.maximumeducationinterntestapp.model.network.MENetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDBHelper(
        db : MEDatabase,
        converter: NewsConverter
    ) = DBHelper(db, converter)

    @Provides
    @Singleton
    fun providesDB(
        @ApplicationContext context: Context
    ) = MEDatabase.createMEDB(context)

    @Provides
    @Singleton
    fun provideConverter() = NewsConverter()

    @Provides
    @Singleton
    fun provideNetwork() = MENetwork()

    @Provides
    @Singleton
    fun provideConnectionChecker(
    @ApplicationContext context : Context
    ) = ConnectionChecker(context)


}