package com.anvipo.angram.layers.core

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.APP_TAG

object CoreHelpers {

    fun debugLog(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(APP_TAG, message)
        }
    }

    fun assertionFailure(message: String = "Debug error") {
        if (BuildConfig.DEBUG) {
            throw CoreErrors.DebugError(message)
        }
    }

}