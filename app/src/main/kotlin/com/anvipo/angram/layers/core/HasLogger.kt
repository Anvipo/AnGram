package com.anvipo.angram.layers.core

import com.anvipo.angram.layers.application.di.SystemInfrastructureModule.SHOULD_LOG
import com.anvipo.angram.layers.core.CoreHelpers.logIfShould

interface HasLogger {

    val className: String

    fun log(
        invokationPlace: String,
        text: String = "",
        customLogging: (() -> Unit)? = null
    ) {
        @Suppress("ConstantConditionIf")
        if (SHOULD_LOG) {
            val mInvokationPlace = "$className::$invokationPlace"
            val mAdditionalText = if (text.isNotEmpty()) ": $text" else ""
            val mText = "$mInvokationPlace is logging$mAdditionalText"
            logIfShould(mText)
            additionalLogging(mText)
            customLogging?.invoke()
        }
    }

    fun <T> additionalLogging(logObj: T): Unit = Unit

}