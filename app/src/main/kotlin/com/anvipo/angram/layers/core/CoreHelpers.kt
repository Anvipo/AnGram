package com.anvipo.angram.layers.core

import android.util.Log
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.APP_TAG
import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.SHOULD_LOG

object CoreHelpers {

    fun debugLog(message: String) {
        if (BuildConfig.DEBUG) {
            logIfShould(message)
        }
    }

    fun logIfShould(message: String) {
        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            Log.d(APP_TAG, message)
        }
    }

    fun assertionFailure(message: String = "Debug error") {
        if (BuildConfig.DEBUG) {
            throw CoreErrors.DebugError(message)
        }
    }

}