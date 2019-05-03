package com.anvipo.angram.global

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.anvipo.angram.BuildConfig
import com.anvipo.angram.applicationLayer.launchSystem.App

fun debugLog(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(App.TAG, message)
    }
}

fun Activity.showDebugToast(
    message: String,
    duration: Int = Toast.LENGTH_LONG
) {
    runOnUiThread {
        this.applicationContext.showDebugToast(message, duration)
    }
}

fun Context.showDebugToast(
    message: String,
    duration: Int = Toast.LENGTH_LONG
) {
    if (BuildConfig.DEBUG) {
        this.mainLooper
        Toast.makeText(this, message, duration).show()
    }
}

fun assertionFailure(message: String = "Debug error") {
    if (BuildConfig.DEBUG) {
        throw DebugError(message)
    }
}