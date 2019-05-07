package com.anvipo.angram.coreLayer.message

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

class DataNotifier<DataType> : ISentDataNotifier<DataType>, IReceiveDataNotifier<DataType> {

    override fun closeChannel(cause: CancellationException?) {
        broadcastChannel.close(cause)
    }

    private val broadcastChannel = BroadcastChannel<DataType>(1)

    override val receiveChannel: ReceiveChannel<DataType>
        get() {
            return broadcastChannel.openSubscription()
        }

    override fun send(systemMessage: DataType) {
        broadcastChannel.offer(systemMessage)
    }

}
