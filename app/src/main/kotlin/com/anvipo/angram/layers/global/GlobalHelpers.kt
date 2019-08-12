package com.anvipo.angram.layers.global

import com.anvipo.angram.layers.core.CoreHelpers.IS_IN_DEBUG_MODE
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType

@Suppress("unused")
object GlobalHelpers {

    const val USE_TEST_ENVIRONMENT: Boolean = true

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
