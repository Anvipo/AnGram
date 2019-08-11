package com.anvipo.angram.layers.core

import com.anvipo.angram.layers.core.CoreHelpers.SHOULD_LOG
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould

interface HasLogger {

    val className: String

    fun myLog(
        invokationPlace: String,
        currentParameters: String? = null,
        customLogging: (() -> Unit)? = null
    ) {
        val mInvokationPlace = "$className::$invokationPlace"

        logIfShould(
            invokationPlace = mInvokationPlace,
            text = currentParameters
        )

        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            if (currentParameters != null) {
                additionalLogging(currentParameters)
            }
            customLogging?.invoke()
        }
    }

    fun <T : Any> additionalLogging(logObj: T): Unit = Unit

}