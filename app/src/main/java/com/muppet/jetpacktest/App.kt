package com.muppet.jetpacktest

import android.app.Application
import android.content.Context

class App : Application() {
    companion object{
        //全局context
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}