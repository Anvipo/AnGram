package com.anvipo.angram.coreLayer.message

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

class SystemMessageNotifier {

    private val broadcastChannel = BroadcastChannel<SystemMessage>(1)

    val receiveChannel: ReceiveChannel<SystemMessage>
        get() {
            return broadcastChannel.openSubscription()
        }

    fun send(systemMessage: SystemMessage) {
        broadcastChannel.offer(systemMessage)
    }

    fun send(
        text: String,
        type: SystemMessageType = SystemMessageType.ALERT,
        showToUser: Boolean = false,
        log: Boolean = false
    ) {
        val result = SystemMessage(
            text = text,
            type = type,
            shouldBeShownToUser = showToUser,
            shouldBeShownInLogs = log
        )

        broadcastChannel.offer(result)
    }

}
