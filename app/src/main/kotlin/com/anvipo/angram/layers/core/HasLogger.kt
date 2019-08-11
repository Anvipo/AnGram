package com.anvipo.angram.layers.core

import com.anvipo.angram.layers.core.CoreHelpers.logIfShould
import com.anvipo.angram.layers.global.GlobalHelpers.SHOULD_LOG

interface HasLogger {

    val className: String

    fun log(
        invokationPlace: String,
        text: String = "",
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