package com.anvipo.angram.layers.core.message

data class SystemMessage(
    val text: String,
    val type: SystemMessageType = SystemMessageType.ALERT,
    val shouldBeShownToUser: Boolean = false,
    val shouldBeShownInLogs: Boolean = false
)
