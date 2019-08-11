package com.anvipo.angram.layers.core

import com.anvipo.angram.layers.core.CoreHelpers.SHOULD_LOG
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould

interface HasLogger {

    val className: String

    fun log(
        invokationPlace: String,
        text: String? = null,
        customLogging: (() -> Unit)? = null
    ) {
        val mInvokationPlace = "$className::$invokationPlace"

        logIfShould(
            invokationPlace = mInvokationPlace,
            text = text
        )

        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            additionalLogging(text)
            customLogging?.invoke()
        }
    }

    fun <T> additionalLogging(logObj: T): Unit = Unit

}