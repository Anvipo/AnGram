package com.anvipo.angram.layers.core

import android.util.Log
import com.anvipo.angram.BuildConfig

@Suppress("unused")
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
        text: String? = null,
        invokationPlace: String
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

val Throwable.errorMessage: String
    get() {
        val firstStackTraceElement = this.stackTrace?.firstOrNull()
        val nullableErrorMessage = this.message

        return when {
            nullableErrorMessage != null -> nullableErrorMessage
            firstStackTraceElement != null -> "Error in ${firstStackTraceElement.className} class" +
                    " in ${firstStackTraceElement.fileName} file" +
                    " in ${firstStackTraceElement.methodName} method" +
                    " in ${firstStackTraceElement.lineNumber} line"
            else -> "Undefined error: $this"
        }
    }