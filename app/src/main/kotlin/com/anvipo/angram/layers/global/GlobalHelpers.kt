package com.anvipo.angram.layers.global

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.layers.core.message.SystemMessage
import com.anvipo.angram.layers.core.message.SystemMessageType

object GlobalHelpers {

    fun createTGSystemMessageWithLogging(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = BuildConfig.DEBUG,
        shouldBeShownInLogs = true
    )

    fun createTGSystemMessage(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = BuildConfig.DEBUG,
        shouldBeShownInLogs = false
    )

}
