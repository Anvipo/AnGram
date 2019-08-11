package com.anvipo.angram.layers.core

import android.util.Log
import com.anvipo.angram.layers.global.GlobalHelpers.APP_TAG
import com.anvipo.angram.layers.global.GlobalHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.global.GlobalHelpers.SHOULD_LOG

object CoreHelpers {

    fun debugLog(message: String) {
        if (IS_IN_DEBUG_MODE) {
            logIfShould(message)
        }
    }

    fun logIfShould(
        invokationPlace: String,
        text: String = ""
    ) {
        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            val mAdditionalText = if (text.isNotEmpty()) ": $text" else ""
            val mText = "$invokationPlace is logging$mAdditionalText"
            Log.d(APP_TAG, mText)
        }
    }

    fun assertionFailure(message: String = "Debug error") {
        if (IS_IN_DEBUG_MODE) {
            throw CoreErrors.DebugError(message)
        }
    }

}