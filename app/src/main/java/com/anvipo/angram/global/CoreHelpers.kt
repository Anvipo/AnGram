package com.anvipo.angram.global

import com.anvipo.angram.BuildConfig
import com.anvipo.angram.coreLayer.message.SystemMessage
import com.anvipo.angram.coreLayer.message.SystemMessageType

object CoreHelpers {

    fun createTGSystemMessage(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = BuildConfig.DEBUG,
        shouldBeShownInLogs = true
    )

    fun createTGSystemMessageFromApp(text: String): SystemMessage = SystemMessage(
        text = text,
        type = SystemMessageType.TOAST,
        shouldBeShownToUser = BuildConfig.DEBUG,
        shouldBeShownInLogs = false
    )

}
