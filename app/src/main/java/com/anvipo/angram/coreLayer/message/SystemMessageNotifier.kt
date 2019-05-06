package com.anvipo.angram.coreLayer.message

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

class DataNotifier<DataType> : ISentDataNotifier<DataType>, IReceiveDataNotifier<DataType> {

    private val broadcastChannel = BroadcastChannel<DataType>(1)

    override val receiveChannel: ReceiveChannel<DataType>
        get() {
            return broadcastChannel.openSubscription()
        }

    override fun send(systemMessage: DataType) {
        broadcastChannel.offer(systemMessage)
    }

}
