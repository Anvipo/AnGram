package com.anvipo.angram.layers.global

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType

object GlobalHelpers {

    const val SHOULD_LOG: Boolean = true

    const val USE_TEST_ENVIRONMENT: Boolean = true

    const val APP_TAG: String = "AnGram"

    val IS_IN_DEBUG_MODE: Boolean = BuildConfig.DEBUG

    fun createTGSystemMessageWithLogging(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = IS_IN_DEBUG_MODE,
        shouldBeShownInLogs = true
    )

    fun createTGSystemMessage(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = IS_IN_DEBUG_MODE,
        shouldBeShownInLogs = false
    )

}
