package com.anvipo.angram.layers.core

import android.util.Log
import com.anvipo.angram.BuildConfig

object CoreHelpers {

    val IS_IN_DEBUG_MODE: Boolean = BuildConfig.DEBUG

    const val SHOULD_LOG: Boolean = true

    private const val APP_TAG: String = "AnGram"

    fun debugLog(
        text: String? = null,
        invokationPlace: String
    ) {
        if (IS_IN_DEBUG_MODE) {
            logIfShould(
                invokationPlace = invokationPlace,
                text = text
            )
        }
    }

    fun logIfShould(
        invokationPlace: String,
        text: String? = null
    ) {
        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            val mAdditionalText = if (text == null || text.isEmpty()) "" else ": $text"
            val mText = "$invokationPlace is logging$mAdditionalText"
            Log.d(APP_TAG, mText)
        }
    }

    fun assertionFailure(message: String) {
        if (IS_IN_DEBUG_MODE) {
            throw CoreErrors.DebugError(message)
        }
    }

}