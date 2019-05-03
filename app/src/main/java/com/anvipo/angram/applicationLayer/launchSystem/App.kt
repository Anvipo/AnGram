package com.anvipo.angram.applicationLayer.launchSystem

import android.app.Application
import java.util.*

class App : Application() {

    companion object {
        lateinit var appCode: String
            private set

        val TAG = "AnGram"
    }

    override fun onCreate() {
        super.onCreate()
        appCode = UUID.randomUUID().toString()

        initDependecies()
    }


    /// PRIVATE


    private fun initDependecies() {}

}