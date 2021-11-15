package com.pashacabu.maximumeducationinterntestapp

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MaximumEducationInternTestApp : Application() {

companion object {
    lateinit var instance: MaximumEducationInternTestApp private set
}

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}