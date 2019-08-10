package com.anvipo.angram.coreLayer

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.application.launchSystem.App

object CoreHelpers {

    fun debugLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(App.TAG, message)
        }
    }

    fun assertionFailure(message: String = "Debug error") {
        if (BuildConfig.DEBUG) {
            throw CoreErrors.DebugError(message)
        }
    }

}