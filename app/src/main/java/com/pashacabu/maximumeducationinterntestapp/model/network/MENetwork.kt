package com.pashacabu.maximumeducationinterntestapp.model.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pashacabu.maximumeducationinterntestapp.model.network_response.NewsNetworkResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MENetwork @Inject constructor() {

    var responseCode = 0

    private val json = Json {
        prettyPrint = true
    }

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val codeInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        responseCode = response.code
        response
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor(codeInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val network : NewsInterface = retrofit.create()

    interface NewsInterface {

        @GET("news")
        suspend fun getNews(
            @Query("access_key") apiKey : String = API_KEY,
            @Query("sources") language : String = "en",
            @Query("offset") offset : Int
        ) : NewsNetworkResponse
    }


    companion object{
        const val BASE_URL = "http://api.mediastack.com/v1/"
        const val API_KEY = "a5ba4421aad27f96417e3927e1549293"
    }
}